package airhacks.ebank.accounting.entity;

import java.math.BigDecimal;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {

    @Id
    @Schema(required = true, example = "2")
    public String iban;
    @Schema(required = true, example = "42")
    public BigDecimal balance;

    protected Account() {
        this.balance = BigDecimal.ZERO;
    }

    public Account(String iban, BigDecimal balance) {
        this();
        Objects.requireNonNull(iban);
        Objects.requireNonNull(balance);
        this.iban = iban;
        this.balance = balance;
    }

    public Account debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        return this;
    }

    public Account deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        return this;
    }

    @JsonbTransient
    @Schema(hidden = true)
    public boolean isBalancePositive() {
        return this.balance.intValue() > 0;
    }

    public int balance() {
        return this.balance.intValue();
    }

    public String iban() {
        return this.iban;
    }

    public static String tableName() {
        return Account.class
                .getSimpleName()
                .toLowerCase();
    }

    @Override
    public String toString() {
        return "Account [iban=" + iban + ", balance=" + balance + "]";
    }

}
