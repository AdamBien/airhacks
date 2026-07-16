package airhacks.contacts.contacts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Contact(String id, String firstName, String lastName, String email, String phone, ContactType type) {

    static final String WELL_FORMED_EMAIL = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    public static Contact fromJSON(JsonObject json) {
        return new Contact(
                json.getString("id", ""),
                json.getString("firstName", ""),
                json.getString("lastName", ""),
                json.getString("email", ""),
                json.getString("phone", ""),
                ContactType.fromJSON(json.getString("type", "")));
    }

    public Contact withId(String id) {
        return new Contact(id, this.firstName, this.lastName, this.email, this.phone, this.type);
    }

    public boolean isValid() {
        return this.hasLastName() && this.hasWellFormedEmail() && this.hasType();
    }

    boolean hasLastName() {
        return !this.lastName.isBlank();
    }

    boolean hasWellFormedEmail() {
        return this.email.isBlank() || this.email.matches(WELL_FORMED_EMAIL);
    }

    boolean hasType() {
        return this.type != null;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("firstName", this.firstName)
                .add("lastName", this.lastName)
                .add("email", this.email)
                .add("phone", this.phone)
                .add("type", this.type.toJSON())
                .build();
    }
}
