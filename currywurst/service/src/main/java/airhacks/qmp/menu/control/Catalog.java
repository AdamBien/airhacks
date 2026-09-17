package airhacks.qmp.menu.control;

import java.util.List;
import java.util.Optional;

import airhacks.qmp.menu.entity.MenuItem;
import jakarta.enterprise.context.ApplicationScoped;

/// The stand's built-in, read-only catalog. Changes happen offline, never at runtime (D1).
@ApplicationScoped
public class Catalog {

    static final List<MenuItem> ITEMS = List.of(
            new MenuItem("Currywurst mit Darm", 350),
            new MenuItem("Currywurst ohne Darm", 350),
            new MenuItem("Pommes", 300),
            new MenuItem("Currywurst mit Pommes", 600),
            new MenuItem("Cola", 250),
            new MenuItem("Wasser", 200));

    public List<MenuItem> items() {
        return ITEMS;
    }

    public Optional<MenuItem> find(String name) {
        return ITEMS.stream()
                .filter(item -> item.name().equals(name))
                .findFirst();
    }
}
