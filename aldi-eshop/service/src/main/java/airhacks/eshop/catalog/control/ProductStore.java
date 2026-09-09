package airhacks.eshop.catalog.control;

import java.util.List;
import java.util.Optional;

import airhacks.eshop.catalog.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductStore {

    static final List<Product> PRODUCTS = List.of(
            new Product("p-milk", "Whole Milk 1L", 109, "rgb(250,250,245)", "dairy", "images/p-milk.png", true),
            new Product("p-gouda", "Gouda Slices", 249, "rgb(244,196,48)", "dairy", "images/p-gouda.png", true),
            new Product("p-rye", "Rye Bread", 189, "rgb(139,90,43)", "bakery", "images/p-rye.png", true),
            new Product("p-espresso", "Espresso Beans 1kg", 999, "rgb(59,47,47)", "coffee", "images/p-espresso.png", false));

    public List<Product> list(Optional<String> category) {
        return category
                .map(wanted -> PRODUCTS.stream().filter(product -> product.category().equals(wanted)).toList())
                .orElse(PRODUCTS);
    }

    public Optional<Product> find(String id) {
        return PRODUCTS.stream().filter(product -> product.id().equals(id)).findFirst();
    }
}
