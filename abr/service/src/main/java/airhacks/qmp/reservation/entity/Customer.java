package airhacks.qmp.reservation.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/// Contact details of the renting person. A value, not an owned entity.
public record Customer(String name, String email) {

    public boolean isComplete() {
        return present(this.name) && present(this.email);
    }

    static boolean present(String value) {
        return value != null && !value.isBlank();
    }

    public static Customer fromJSON(JsonObject json) {
        if (json == null) {
            return new Customer(null, null);
        }
        return new Customer(json.getString("name", null), json.getString("email", null));
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("name", this.name)
                .add("email", this.email)
                .build();
    }
}
