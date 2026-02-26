package airhacks.qmp.discounts.entity;

import java.math.BigDecimal;

public class InvalidDiscountPercentageException extends IllegalArgumentException {
    
    public InvalidDiscountPercentageException(BigDecimal percentage) {
        super("Invalid discount percentage: " + percentage + ". Must be between 0 and 100.");
    }
}
