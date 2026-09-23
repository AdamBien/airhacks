package airhacks.qmp.fleet.control;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

/// The bike exists but its current status forbids the requested change.
public class BikeConflict extends ClientErrorException {

    public BikeConflict(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
