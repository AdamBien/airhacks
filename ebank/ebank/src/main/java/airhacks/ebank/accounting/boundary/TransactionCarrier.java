package airhacks.ebank.accounting.boundary;

import java.math.BigDecimal;

public record TransactionCarrier(TransactionType type, BigDecimal amount) {
    public enum TransactionType{
        DEPOSIT, DEBIT
    }

}
