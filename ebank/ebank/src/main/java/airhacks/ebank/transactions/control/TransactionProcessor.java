
package airhacks.ebank.transactions.control;

import java.util.Optional;

import airhacks.ebank.Control;
import airhacks.ebank.accounting.control.AccountCreationResult;
import airhacks.ebank.accounting.control.AccountFinder;
import airhacks.ebank.accounting.control.AccountCreationResult.AlreadyExists;
import airhacks.ebank.accounting.control.AccountCreationResult.Created;
import airhacks.ebank.accounting.control.AccountCreationResult.Invalid;
import airhacks.ebank.accounting.entity.Account;
import airhacks.ebank.transactions.entity.Transaction;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Control
public class TransactionProcessor {
    @PersistenceContext
    EntityManager em;

    @Inject
    AccountFinder finder;

    public Optional<Account> processTransaction(String iban, Transaction transaction) {
        return this.finder.account(iban)
                .map(a -> this.applyTransaction(a, transaction))
                .map(this.em::merge);

    }

    Account applyTransaction(Account account, Transaction transaction) {
        return switch (transaction) {
            case Transaction.Debit debit -> account.debit(debit.amount());
            case Transaction.Deposit deposit -> account.deposit(deposit.amount());
        };

    }

    public AccountCreationResult initialCreation(Account account) {
        if (!this.isValidForCreation(account))
            return new AccountCreationResult.Invalid(account);
        if (this.finder.exists(account))
            return new AccountCreationResult.AlreadyExists(account);
        this.em.persist(account);
        return new AccountCreationResult.Created(account);
    }

    boolean isValidForCreation(Account account) {
        return account.isBalancePositive()
                && (account.balance() < 1000);
    }
}