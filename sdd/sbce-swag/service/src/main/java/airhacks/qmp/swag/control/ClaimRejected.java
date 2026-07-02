package airhacks.qmp.swag.control;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class ClaimRejected extends WebApplicationException {

    ClaimRejected(String reason, Status status) {
        super(reason, status);
    }

    static ClaimRejected incomplete() {
        return new ClaimRejected("claim is missing required fields", Status.BAD_REQUEST);
    }

    static ClaimRejected malformedEmail() {
        return new ClaimRejected("email is malformed", Status.BAD_REQUEST);
    }

    static ClaimRejected invalidSize() {
        return new ClaimRejected("t-shirt or socks size is not accepted", Status.BAD_REQUEST);
    }

    static ClaimRejected outOfStock() {
        return new ClaimRejected("claimed t-shirt or socks size is out of stock", Status.BAD_REQUEST);
    }

    static ClaimRejected duplicate(String email) {
        return new ClaimRejected("a claim already exists for " + email, Status.CONFLICT);
    }
}
