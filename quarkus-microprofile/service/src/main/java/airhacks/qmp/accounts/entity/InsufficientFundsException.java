package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when a withdrawal or transfer exceeds available balance.
 */
public class InsufficientFundsException extends AccountException {

    public InsufficientFundsException(BigDecimal requested, BigDecimal available) {
        super("Insufficient funds: requested " + requested + ", available " + available,
              Response.Status.BAD_REQUEST);
    }
}
