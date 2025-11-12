package airhacks.ebank.transactions.entity;

import java.math.BigDecimal;

import airhacks.ebank.accounting.boundary.TransactionCarrier;
import airhacks.ebank.accounting.boundary.TransactionCarrier.TransactionType;



public sealed interface Transaction permits Transaction.Debit,Transaction.Deposit{
    BigDecimal amount();
    record Debit(BigDecimal amount) implements Transaction {}
    record Deposit(BigDecimal amount) implements Transaction {}

    static Transaction from(TransactionCarrier transaction) {
        var type = transaction.type();
        var amount = transaction.amount();
        return switch (type) {
            case TransactionType.DEBIT -> new Debit(amount);
            case TransactionType.DEPOSIT -> new Deposit(amount);
        };
    }
}