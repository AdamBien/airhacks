package airhacks.petstore.catalog.control;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import airhacks.petstore.catalog.entity.Category;
import airhacks.petstore.catalog.entity.Item;
import airhacks.petstore.catalog.entity.Page;
import airhacks.petstore.catalog.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Read-only, seeded catalog (spec D1). Content descends from the
 * Java Pet Store 1.1.2 sample data.
 */
@ApplicationScoped
public class CatalogStore {

    static final List<Category> CATEGORIES = List.of(
            new Category("FISH", "Fish", "Swimming companions for your aquarium"),
            new Category("DOGS", "Dogs", "Loyal four-legged friends"),
            new Category("REPTILES", "Reptiles", "Cold-blooded curiosities"),
            new Category("CATS", "Cats", "Independent indoor hunters"),
            new Category("BIRDS", "Birds", "Feathered singers and talkers"));

    static final List<Product> PRODUCTS = List.of(
            new Product("FI-SW-01", "FISH", "Angelfish", "Salt water fish from Australia"),
            new Product("FI-SW-02", "FISH", "Tiger Shark", "Salt water fish from Australia"),
            new Product("FI-FW-01", "FISH", "Koi", "Fresh water fish from Japan"),
            new Product("K9-BD-01", "DOGS", "Bulldog", "Friendly dog from England"),
            new Product("K9-PO-02", "DOGS", "Poodle", "Cute dog from France"),
            new Product("RP-SN-01", "REPTILES", "Rattlesnake", "Doubles as a watch dog"),
            new Product("FL-DSH-01", "CATS", "Manx", "Great for reducing mouse populations"),
            new Product("AV-CB-01", "BIRDS", "Amazon Parrot", "Great companion for up to 75 years"));

    static final List<Item> ITEMS = List.of(
            new Item("EST-1", "FI-SW-01", "Large Angelfish", new BigDecimal("16.50")),
            new Item("EST-2", "FI-SW-01", "Small Angelfish", new BigDecimal("16.50")),
            new Item("EST-3", "FI-SW-02", "Toothless Tiger Shark", new BigDecimal("18.50")),
            new Item("EST-4", "FI-FW-01", "Spotted Koi", new BigDecimal("18.50")),
            new Item("EST-5", "FI-FW-01", "Spotless Koi", new BigDecimal("18.50")),
            new Item("EST-6", "K9-BD-01", "Male Adult Bulldog", new BigDecimal("18.50")),
            new Item("EST-7", "K9-BD-01", "Female Puppy Bulldog", new BigDecimal("18.50")),
            new Item("EST-8", "K9-PO-02", "Male Puppy Poodle", new BigDecimal("18.50")),
            new Item("EST-11", "RP-SN-01", "Venomless Rattlesnake", new BigDecimal("18.50")),
            new Item("EST-14", "FL-DSH-01", "Tailless Manx", new BigDecimal("58.50")),
            new Item("EST-18", "AV-CB-01", "Adult Male Amazon Parrot", new BigDecimal("193.50")));

    public List<Category> categories() {
        return CATEGORIES;
    }

    public Optional<Page<Product>> productsOf(String categoryId, int page, int size) {
        if (CATEGORIES.stream().noneMatch(category -> category.id().equals(categoryId))) {
            return Optional.empty();
        }
        var matching = PRODUCTS.stream()
                .filter(product -> product.categoryId().equals(categoryId))
                .toList();
        return Optional.of(pageOf(matching, page, size));
    }

    public Optional<Page<Item>> itemsOf(String productId, int page, int size) {
        if (PRODUCTS.stream().noneMatch(product -> product.id().equals(productId))) {
            return Optional.empty();
        }
        var matching = ITEMS.stream()
                .filter(item -> item.productId().equals(productId))
                .toList();
        return Optional.of(pageOf(matching, page, size));
    }

    public Optional<Item> item(String itemId) {
        return ITEMS.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst();
    }

    public Page<Product> search(String keyword, int page, int size) {
        var matching = PRODUCTS.stream()
                .filter(product -> product.matches(keyword))
                .toList();
        return pageOf(matching, page, size);
    }

    static <T> Page<T> pageOf(List<T> all, int page, int size) {
        var content = all.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();
        return new Page<>(content, all.size());
    }
}
