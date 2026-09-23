package airhacks.qmp.reservation.control;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

/// The reservation exists, or the day is full, but the requested change is not allowed now.
public class ReservationConflict extends ClientErrorException {

    public ReservationConflict(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
