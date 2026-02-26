package airhacks.qmp.products.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.qmp.products.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductStore {

    static System.Logger LOG = System.getLogger(ProductStore.class.getName());

    List<Product> products = new CopyOnWriteArrayList<>();

    public List<Product> all() {
        LOG.log(INFO, "returning all products");
        return this.products;
    }

    public Product add(Product product) {
        LOG.log(INFO, "adding product: " + product.name());
        this.products.add(product);
        return product;
    }

    public Optional<Product> findById(String productId) {
        LOG.log(INFO, "finding product by id: " + productId);
        return this.products.stream()
                .filter(p -> p.productId().equals(productId))
                .findFirst();
    }
}
