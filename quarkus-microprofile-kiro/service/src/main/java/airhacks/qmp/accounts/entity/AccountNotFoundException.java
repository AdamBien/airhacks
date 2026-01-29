package airhacks.qmp.accounts.entity;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when an account cannot be found.
 */
public class AccountNotFoundException extends AccountException {

    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId, Response.Status.NOT_FOUND);
    }
}
