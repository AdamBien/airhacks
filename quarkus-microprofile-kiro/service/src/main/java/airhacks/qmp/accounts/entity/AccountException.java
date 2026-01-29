package airhacks.qmp.accounts.entity;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Base exception for account operations.
 */
public class AccountException extends WebApplicationException {

    public AccountException(String message, Response.Status status) {
        super(message, status);
    }
}
