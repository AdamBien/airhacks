package airhacks.qmp.menu.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/// A sellable item of the stand's fixed catalog, priced in whole cents.
public record MenuItem(String name, int priceInCents) {

    public MenuItem {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("menu item name must not be blank");
        }
        if (priceInCents <= 0) {
            throw new IllegalArgumentException("menu item price must be positive: " + priceInCents);
        }
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("name", this.name)
                .add("priceInCents", this.priceInCents)
                .build();
    }

    public static MenuItem fromJSON(JsonObject json) {
        return new MenuItem(json.getString("name"), json.getInt("priceInCents"));
    }
}
