package airhacks.qmp.owners.entity;

import airhacks.qmp.ValidationMessages;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;

public record Owner(String name, String email, String phone, Address address) {

    public Owner {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("name.required"));
        }
        if (email == null || email.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("email.required"));
        }
        if (phone == null || phone.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("phone.required"));
        }
        if (address == null) {
            throw new BadRequestException(ValidationMessages.get("address.required"));
        }
    }

    public static Owner fromJSON(JsonObject json) {
        return new Owner(
                json.getString("name"),
                json.getString("email"),
                json.getString("phone"),
                Address.fromJSON(json.getJsonObject("address"))
        );
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("name", this.name)
                .add("email", this.email)
                .add("phone", this.phone)
                .add("address", this.address.toJSON())
                .build();
    }
}
