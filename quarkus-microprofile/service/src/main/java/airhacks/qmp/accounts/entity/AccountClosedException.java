package airhacks.qmp.accounts.entity;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when an operation is attempted on a closed account.
 */
public class AccountClosedException extends AccountException {

    public AccountClosedException(String accountId) {
        super("Account is closed: " + accountId, Response.Status.BAD_REQUEST);
    }
}
