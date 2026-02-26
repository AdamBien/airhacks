package airhacks.qmp.products.entity;

import java.math.BigDecimal;
import java.time.Instant;

public record Product(String id, String sellerId, String name, String description, BigDecimal price, Category category,
        ProductStatus status, Instant createdAt, Instant updatedAt) {
}
