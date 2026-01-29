package airhacks.qmp.accounts.entity;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when an unsupported currency is used.
 */
public class UnsupportedCurrencyException extends AccountException {

    public UnsupportedCurrencyException(String currency) {
        super("Unsupported currency: " + currency, Response.Status.BAD_REQUEST);
    }
}
