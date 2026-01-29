package airhacks.qmp.owners.entity;

import airhacks.qmp.ValidationMessages;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;

public record Address(String street, String city, String postalCode, String country) {

    public Address {
        if (street == null || street.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("street.required"));
        }
        if (city == null || city.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("city.required"));
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("postalCode.required"));
        }
        if (country == null || country.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("country.required"));
        }
    }

    public static Address fromJSON(JsonObject json) {
        return new Address(
                json.getString("street"),
                json.getString("city"),
                json.getString("postalCode"),
                json.getString("country")
        );
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("street", this.street)
                .add("city", this.city)
                .add("postalCode", this.postalCode)
                .add("country", this.country)
                .build();
    }
}
