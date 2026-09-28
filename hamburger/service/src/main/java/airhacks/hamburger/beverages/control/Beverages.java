package airhacks.hamburger.beverages.control;

import airhacks.hamburger.beverages.entity.Beverage;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

/// The fixed set of beverages on sale, in the shop's order. Consumed by `ordering` to resolve and price beverage lines.
@ApplicationScoped
public class Beverages {

    List<Beverage> beverages = List.of(
            new Beverage("cola", 140, 299),
            new Beverage("orange-juice", 110, 349),
            new Beverage("iced-tea", 90, 279),
            new Beverage("water", 0, 199));

    public List<Beverage> all() {
        return this.beverages;
    }

    public Optional<Beverage> find(String name) {
        return this.beverages.stream()
                .filter(beverage -> beverage.name().equals(name))
                .findFirst();
    }
}
