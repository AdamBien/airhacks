package airhacks.qmp.products.entity;

import java.math.BigDecimal;

public record CreateProductRequest(String name, String description, BigDecimal price, Category category) {
}
