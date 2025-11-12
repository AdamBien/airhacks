package airhacks.ebank.leasing.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class LeaseTest {

    @Test
    void endDateCalculation() {
        var startDate = LocalDate.of(2025, 1, 15);
        var lease = new Lease("LEASE-001", "DE123", BigDecimal.valueOf(50000), 36, startDate);

        var expectedEndDate = LocalDate.of(2028, 1, 15);
        assertThat(lease.endDate()).isEqualTo(expectedEndDate);
    }

    @Test
    void accessors() {
        var amount = BigDecimal.valueOf(75000);
        var lease = new Lease("LEASE-002", "DE456", amount, 24, LocalDate.of(2025, 3, 1));

        assertThat(lease.contractNumber()).isEqualTo("LEASE-002");
        assertThat(lease.lesseeIban()).isEqualTo("DE456");
        assertThat(lease.amount()).isEqualTo(amount);
        assertThat(lease.durationMonths()).isEqualTo(24);
    }
}
