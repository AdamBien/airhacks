package airhacks.qmp.products.entity;

import java.math.BigDecimal;

import jakarta.ws.rs.BadRequestException;

public class InvalidProductPriceException extends BadRequestException {

    public InvalidProductPriceException(BigDecimal price) {
        super("Product price " + price + " is not valid, must be greater than zero");
    }
}
