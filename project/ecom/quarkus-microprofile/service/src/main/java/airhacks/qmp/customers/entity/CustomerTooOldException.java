package airhacks.qmp.customers.entity;

import jakarta.ws.rs.BadRequestException;

public class CustomerTooOldException extends BadRequestException {

    public CustomerTooOldException(int age) {
        super("Customer age " + age + " exceeds maximum of 90");
    }
}
