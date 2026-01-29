package airhacks.qmp.accounts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Represents a customer who owns one or more bank accounts.
 * The holder name is required for account creation.
 */
public record AccountHolder(
    String id,
    String name,
    String email,
    String address
) {
    
    public AccountHolder {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Holder name is required");
        }
    }
    
    public JsonObject toJson() {
        var builder = Json.createObjectBuilder()
            .add("name", name);
        
        if (id != null) {
            builder.add("id", id);
        }
        if (email != null) {
            builder.add("email", email);
        }
        if (address != null) {
            builder.add("address", address);
        }
        
        return builder.build();
    }
    
    public static AccountHolder fromJson(JsonObject json) {
        var id = json.containsKey("id") ? json.getString("id") : null;
        var name = json.getString("name");
        var email = json.containsKey("email") ? json.getString("email") : null;
        var address = json.containsKey("address") ? json.getString("address") : null;
        
        return new AccountHolder(id, name, email, address);
    }
}
