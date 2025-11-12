package airhacks.ebank.accounting.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class AccountsResourceIT {
    
    @Inject
    AccountDelegate accountDelegate;

    @Test
    @DisplayName("tests account transactions after initial creation")
    void creationDepositAndDebit() {
        var balance = 900;
        var randomIBAN = this.accountDelegate.randomIBAN();
        var account = this.accountDelegate.initialCreationAndFetch(randomIBAN, balance);
        assertThat(account.getString("iban")).isEqualTo(randomIBAN);
        assertThat(account.getInt("balance")).isEqualTo(balance);
        var balanceAfterDebit = this.accountDelegate.debit(900);
        assertThat(balanceAfterDebit).isEqualTo(balance - 900);

        var balanceAfterDeposit = this.accountDelegate.deposit(100);
        assertThat(balanceAfterDeposit).isEqualTo(balanceAfterDebit + 100);
    }


    @Test
    @DisplayName("creates account twice with the same IBAN, second creation fails")
    void doubleCreationWithValidData() {
        var iban = this.accountDelegate.randomIBAN();
        this.accountDelegate.initialCreation(iban,2);
        assertThat(this.accountDelegate.lastResponseSuccessful()).isTrue();
        this.accountDelegate.initialCreation(iban,2);
        assertThat(this.accountDelegate.lastResponseConflict()).isTrue();
    }

    @Test
    @DisplayName("creates account with negative balance with the expectation to fail")
    void createsAccountWithNegativeBalance() {
        var iban = this.accountDelegate.randomIBAN();
        this.accountDelegate.initialCreation(iban,-1);
        assertThat(this.accountDelegate.lastResponseInvalid()).isTrue();
    }
}
