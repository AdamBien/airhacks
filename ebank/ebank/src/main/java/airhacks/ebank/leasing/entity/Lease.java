package airhacks.ebank.leasing.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Lease {

    @Id
    @GeneratedValue
    @Schema(required = true, example = "1")
    public Long id;

    @Schema(required = true, example = "LEASE-2025-001")
    public String contractNumber;

    @Schema(required = true, example = "DE89370400440532013000")
    public String lesseeIban;

    @Schema(required = true, example = "50000")
    public BigDecimal amount;

    @Schema(required = true, example = "36")
    public int durationMonths;

    @Schema(required = true, example = "2025-01-15")
    public LocalDate startDate;

    @Schema(required = true, example = "2028-01-15")
    public LocalDate endDate;

    protected Lease() {
    }

    public Lease(String contractNumber, String lesseeIban, BigDecimal amount, int durationMonths, LocalDate startDate) {
        Objects.requireNonNull(contractNumber);
        Objects.requireNonNull(lesseeIban);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(startDate);
        this.contractNumber = contractNumber;
        this.lesseeIban = lesseeIban;
        this.amount = amount;
        this.durationMonths = durationMonths;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(durationMonths);
    }

    public Long id() {
        return this.id;
    }

    public String contractNumber() {
        return this.contractNumber;
    }

    public String lesseeIban() {
        return this.lesseeIban;
    }

    public BigDecimal amount() {
        return this.amount;
    }

    public int durationMonths() {
        return this.durationMonths;
    }

    public LocalDate startDate() {
        return this.startDate;
    }

    public LocalDate endDate() {
        return this.endDate;
    }

    @Override
    public String toString() {
        return "Lease [id=" + id + ", contractNumber=" + contractNumber + ", lesseeIban=" + lesseeIban +
                ", amount=" + amount + ", durationMonths=" + durationMonths +
                ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
}
