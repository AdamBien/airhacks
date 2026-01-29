package airhacks.qmp.accounts.entity;

import jakarta.ws.rs.core.Response;

/**
 * Thrown when an exchange rate is unavailable for currency conversion.
 */
public class ExchangeRateUnavailableException extends AccountException {

    public ExchangeRateUnavailableException(Currency from, Currency to) {
        super("Exchange rate unavailable for " + from + " to " + to,
              Response.Status.SERVICE_UNAVAILABLE);
    }
}
