package airhacks.qmp.products.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.Optional;

import airhacks.qmp.products.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductStore {

    static System.Logger LOG = System.getLogger(ProductStore.class.getName());

    @Inject
    EntityManager em;

    public List<Product> all() {
        LOG.log(INFO, "returning all products");
        return this.em.createNamedQuery("all", Product.class).getResultList();
    }

    public Product add(Product product) {
        LOG.log(INFO, "adding product: " + product.name());
        this.em.persist(product);
        return product;
    }

    public Optional<Product> findById(String productId) {
        LOG.log(INFO, "finding product by id: " + productId);
        return Optional.ofNullable(this.em.find(Product.class, productId));
    }
}
