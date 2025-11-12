package airhacks.ebank.accounting.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AccountTest {
    
    @Test
    void tableName() {
        var actual = Account.tableName();
        assertThat(actual).isEqualTo("account");    
    }
}
