package airhacks.qmp.products.control;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.products.entity.CreateProductRequest;
import airhacks.qmp.products.entity.Product;
import airhacks.qmp.products.entity.ProductStatus;
import airhacks.qmp.products.entity.UpdateProductRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductService {

    final ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

    public Product create(String sellerId, CreateProductRequest request) {
        var now = Instant.now();
        var product = new Product(
                UUID.randomUUID().toString(),
                sellerId,
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                ProductStatus.DRAFT,
                now,
                now);
        this.products.put(product.id(), product);
        return product;
    }

    public List<Product> findActive() {
        return this.products.values().stream()
                .filter(p -> p.status() == ProductStatus.ACTIVE)
                .toList();
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(this.products.get(id));
    }

    public List<Product> findBySeller(String sellerId) {
        return this.products.values().stream()
                .filter(p -> p.sellerId().equals(sellerId))
                .toList();
    }

    public Optional<Product> update(String id, String sellerId, UpdateProductRequest request) {
        var existing = this.products.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        if (!existing.sellerId().equals(sellerId)) {
            return Optional.empty();
        }
        var updated = new Product(
                existing.id(),
                existing.sellerId(),
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.status(),
                existing.createdAt(),
                Instant.now());
        this.products.put(id, updated);
        return Optional.of(updated);
    }

    public Optional<Boolean> delete(String id, String sellerId) {
        var existing = this.products.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        if (!existing.sellerId().equals(sellerId)) {
            return Optional.of(false);
        }
        this.products.remove(id);
        return Optional.of(true);
    }
}
