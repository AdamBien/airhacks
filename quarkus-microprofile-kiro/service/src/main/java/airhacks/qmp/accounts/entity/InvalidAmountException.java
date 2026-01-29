package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when a transaction amount is invalid (zero or negative).
 */
public class InvalidAmountException extends AccountException {

    public InvalidAmountException(BigDecimal amount) {
        super("Invalid amount: " + amount + ". Amount must be positive.",
              Response.Status.BAD_REQUEST);
    }
}
