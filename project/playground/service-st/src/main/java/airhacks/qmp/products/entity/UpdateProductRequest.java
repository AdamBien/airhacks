package airhacks.qmp.products.entity;

import java.math.BigDecimal;

public record UpdateProductRequest(String name, String description, BigDecimal price, Category category,
        ProductStatus status) {
}
