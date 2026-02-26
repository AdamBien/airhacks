package airhacks.qmp.customers.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Customer(String name, String email, int age) {

    public Customer {
        if (age > 90) {
            throw new CustomerTooOldException(age);
        }
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("name", this.name)
                .add("email", this.email)
                .add("age", this.age)
                .build();
    }

    public static Customer fromJSON(JsonObject json) {
        return new Customer(
                json.getString("name"),
                json.getString("email"),
                json.getInt("age"));
    }
}
