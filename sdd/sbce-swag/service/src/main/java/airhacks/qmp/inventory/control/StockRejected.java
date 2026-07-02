package airhacks.qmp.inventory.control;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class StockRejected extends WebApplicationException {

    StockRejected(String reason) {
        super(reason, Status.BAD_REQUEST);
    }

    static StockRejected negative() {
        return new StockRejected("stock quantity must not be negative");
    }

    static StockRejected unrecognised() {
        return new StockRejected("item or size is not a recognised swag option");
    }
}
