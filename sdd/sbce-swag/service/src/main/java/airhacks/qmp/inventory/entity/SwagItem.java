package airhacks.qmp.inventory.entity;

import java.util.Set;

public enum SwagItem {

    T_SHIRT(Set.of("XS", "S", "M", "L", "XL", "XXL")),
    SOCKS(Set.of("S", "M", "L"));

    final Set<String> sizes;

    SwagItem(Set<String> sizes) {
        this.sizes = sizes;
    }

    public boolean offers(String size) {
        return this.sizes.contains(size);
    }
}
