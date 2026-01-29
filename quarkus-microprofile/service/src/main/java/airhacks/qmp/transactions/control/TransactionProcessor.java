package airhacks.qmp.transactions.control;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import airhacks.qmp.accounts.control.AccountProcessor;
import airhacks.qmp.accounts.entity.Account;
import airhacks.qmp.accounts.entity.Currency;
import airhacks.qmp.transactions.entity.Transaction;
import airhacks.qmp.transactions.entity.TransactionStatus;
import airhacks.qmp.transactions.entity.TransactionType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Handles financial transactions including deposits, withdrawals, and transfers.
 * Ensures atomicity for transfer operations and maintains transaction history.
 */
@ApplicationScoped
public class TransactionProcessor {

    @Inject
    AccountProcessor accountProcessor;

    ConcurrentHashMap<String, Transaction> transactions = new ConcurrentHashMap<>();

    ReentrantLock transferLock = new ReentrantLock();

    /**
     * Deposits funds into an account with optional currency conversion.
     *
     * @param accountId the target account identifier
     * @param amount    the amount to deposit (must be positive)
     * @param currency  the currency of the deposit
     * @return the completed transaction
     * @throws IllegalArgumentException if account not found or amount invalid
     * @throws IllegalStateException    if account is closed
     */
    public Transaction deposit(String accountId, BigDecimal amount, Currency currency) {
        validatePositiveAmount(amount);

        var account = findAccountOrThrow(accountId);
        validateAccountNotClosed(account);

        var convertedAmount = convertIfNeeded(amount, currency, account.currency());
        var newBalance = account.balance().add(convertedAmount);

        accountProcessor.updateBalance(accountId, newBalance);

        var transaction = createTransaction(
            accountId,
            TransactionType.DEPOSIT,
            convertedAmount,
            account.currency(),
            newBalance,
            null
        );

        transactions.put(transaction.id(), transaction);
        return transaction;
    }

    /**
     * Withdraws funds from an account.
     *
     * @param accountId the source account identifier
     * @param amount    the amount to withdraw (must be positive)
     * @return the completed transaction
     * @throws IllegalArgumentException if account not found, amount invalid, or insufficient funds
     * @throws IllegalStateException    if account is closed
     */
    public Transaction withdraw(String accountId, BigDecimal amount) {
        validatePositiveAmount(amount);

        var account = findAccountOrThrow(accountId);
        validateAccountNotClosed(account);
        validateSufficientFunds(account, amount);

        var newBalance = account.balance().subtract(amount);

        accountProcessor.updateBalance(accountId, newBalance);

        var transaction = createTransaction(
            accountId,
            TransactionType.WITHDRAWAL,
            amount,
            account.currency(),
            newBalance,
            null
        );

        transactions.put(transaction.id(), transaction);
        return transaction;
    }

    /**
     * Transfers funds between two accounts atomically.
     * Creates matching transactions on both accounts with the same reference number.
     * Handles currency conversion if accounts have different currencies.
     *
     * @param sourceId      the source account identifier
     * @param destinationId the destination account identifier
     * @param amount        the amount to transfer (must be positive)
     * @return the transfer result containing both transactions
     * @throws IllegalArgumentException if accounts not found, amount invalid, or insufficient funds
     * @throws IllegalStateException    if either account is closed
     */
    public TransferResult transfer(String sourceId, String destinationId, BigDecimal amount) {
        validatePositiveAmount(amount);

        transferLock.lock();
        try {
            var sourceAccount = findAccountOrThrow(sourceId);
            var destinationAccount = findAccountOrThrow(destinationId);

            validateAccountNotClosed(sourceAccount);
            validateAccountNotClosed(destinationAccount);
            validateSufficientFunds(sourceAccount, amount);

            var convertedAmount = convertIfNeeded(amount, sourceAccount.currency(), destinationAccount.currency());

            var newSourceBalance = sourceAccount.balance().subtract(amount);
            var newDestinationBalance = destinationAccount.balance().add(convertedAmount);

            var referenceNumber = generateReferenceNumber();
            var timestamp = Instant.now();

            var sourceTransaction = new Transaction(
                UUID.randomUUID().toString(),
                sourceId,
                TransactionType.TRANSFER_OUT,
                amount,
                sourceAccount.currency(),
                newSourceBalance,
                referenceNumber,
                destinationId,
                TransactionStatus.COMPLETED,
                timestamp
            );

            var destinationTransaction = new Transaction(
                UUID.randomUUID().toString(),
                destinationId,
                TransactionType.TRANSFER_IN,
                convertedAmount,
                destinationAccount.currency(),
                newDestinationBalance,
                referenceNumber,
                sourceId,
                TransactionStatus.COMPLETED,
                timestamp
            );

            accountProcessor.updateBalance(sourceId, newSourceBalance);
            accountProcessor.updateBalance(destinationId, newDestinationBalance);

            transactions.put(sourceTransaction.id(), sourceTransaction);
            transactions.put(destinationTransaction.id(), destinationTransaction);

            return new TransferResult(sourceTransaction, destinationTransaction);
        } finally {
            transferLock.unlock();
        }
    }

    /**
     * Retrieves transaction history for an account with optional date filtering.
     * Results are ordered by timestamp descending (newest first).
     *
     * @param accountId the account identifier
     * @param from      optional start date (inclusive)
     * @param to        optional end date (inclusive)
     * @return list of transactions ordered by date descending
     * @throws IllegalArgumentException if account not found
     */
    public List<Transaction> history(String accountId, LocalDate from, LocalDate to) {
        findAccountOrThrow(accountId);

        var fromInstant = from != null ? from.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        var toInstant = to != null ? to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant() : null;

        return transactions.values().stream()
            .filter(t -> t.accountId().equals(accountId))
            .filter(t -> fromInstant == null || !t.timestamp().isBefore(fromInstant))
            .filter(t -> toInstant == null || t.timestamp().isBefore(toInstant))
            .sorted(Comparator.comparing(Transaction::timestamp).reversed())
            .toList();
    }

    Account findAccountOrThrow(String accountId) {
        return accountProcessor.find(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }

    void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }
    }

    void validateAccountNotClosed(Account account) {
        if (account.closed()) {
            throw new IllegalStateException("Account is closed: " + account.id());
        }
    }

    void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                "Insufficient funds: requested " + amount + ", available " + account.balance()
            );
        }
    }

    BigDecimal convertIfNeeded(BigDecimal amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }
        return ExchangeRates.convert(amount, from, to);
    }

    Transaction createTransaction(String accountId, TransactionType type, BigDecimal amount,
                                   Currency currency, BigDecimal balanceAfter, String relatedAccountId) {
        return new Transaction(
            UUID.randomUUID().toString(),
            accountId,
            type,
            amount,
            currency,
            balanceAfter,
            generateReferenceNumber(),
            relatedAccountId,
            TransactionStatus.COMPLETED,
            Instant.now()
        );
    }

    String generateReferenceNumber() {
        return "REF-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
