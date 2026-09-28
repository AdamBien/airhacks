package airhacks.hamburger.menu.control;

import airhacks.hamburger.menu.entity.MenuItem;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

/// The fixed set of hamburgers on sale. Consumed by `ordering` to resolve and price items.
@ApplicationScoped
public class Menu {

    List<MenuItem> items = List.of(
            new MenuItem("classic", 550, 899),
            new MenuItem("cheese", 650, 999),
            new MenuItem("bacon", 720, 1099),
            new MenuItem("veggie", 480, 949));

    public List<MenuItem> items() {
        return this.items;
    }

    public Optional<MenuItem> find(String name) {
        return this.items.stream()
                .filter(item -> item.name().equals(name))
                .findFirst();
    }
}
