package airhacks.qmp.accounts.control;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.accounts.entity.Account;
import airhacks.qmp.accounts.entity.AccountHolder;
import airhacks.qmp.accounts.entity.Currency;
import airhacks.qmp.accounts.entity.Region;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Handles account lifecycle operations including creation, retrieval, 
 * balance updates, and closure.
 */
@ApplicationScoped
public class AccountProcessor {
    
    static final String DEFAULT_BANK_CODE = "QMPBANK";
    static final String DEFAULT_SWIFT_BIC = "QMPBDEFF";
    
    ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    
    /**
     * Creates a new account with zero balance for the specified holder.
     * Generates a region-specific identifier and assigns a SWIFT BIC code.
     * 
     * @param holder the account holder information
     * @param currency the account currency
     * @param region the geographic region determining identifier format
     * @return a new account with zero balance
     */
    public Account create(AccountHolder holder, Currency currency, Region region) {
        var identifier = IdentifierGenerator.generate(region, DEFAULT_BANK_CODE);
        var account = Account.create(holder, currency, region, identifier, DEFAULT_SWIFT_BIC);
        accounts.put(account.id(), account);
        return account;
    }
    
    /**
     * Finds an account by its unique identifier.
     * 
     * @param accountId the account identifier
     * @return an Optional containing the account if found, empty otherwise
     */
    public Optional<Account> find(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }
    
    /**
     * Finds all accounts belonging to a specific holder.
     * 
     * @param holderId the holder identifier
     * @return a list of accounts belonging to the holder
     */
    public List<Account> findByHolder(String holderId) {
        return accounts.values().stream()
            .filter(account -> holderId.equals(account.holder().id()))
            .toList();
    }
    
    /**
     * Marks an account as closed. The account must have zero balance.
     * 
     * @param accountId the account identifier
     * @return the closed account
     * @throws IllegalArgumentException if account not found
     * @throws IllegalStateException if account has non-zero balance
     */
    public Account close(String accountId) {
        var account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
        if (account.balance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Cannot close account with non-zero balance: " + account.balance());
        }
        var closedAccount = account.asClosed();
        accounts.put(accountId, closedAccount);
        return closedAccount;
    }
    
    /**
     * Updates the balance of an account.
     * 
     * @param accountId the account identifier
     * @param newBalance the new balance value
     * @return the updated account
     * @throws IllegalArgumentException if account not found
     */
    public Account updateBalance(String accountId, BigDecimal newBalance) {
        var account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
        var updatedAccount = account.withBalance(newBalance);
        accounts.put(accountId, updatedAccount);
        return updatedAccount;
    }
}
