package airhacks.qmp.accounts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;

public record AccountIdentifier(String accountNumber, String routingCode) {

    public AccountIdentifier {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new BadRequestException("accountNumber is required");
        }
        if (routingCode == null || routingCode.isBlank()) {
            throw new BadRequestException("routingCode is required");
        }
    }

    public static AccountIdentifier fromJSON(JsonObject json) {
        return new AccountIdentifier(
                json.getString("accountNumber"),
                json.getString("routingCode")
        );
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("accountNumber", this.accountNumber)
                .add("routingCode", this.routingCode)
                .build();
    }
}
