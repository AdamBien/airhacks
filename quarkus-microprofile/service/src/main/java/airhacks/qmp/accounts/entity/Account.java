package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;

public record Account(AccountIdentifier identifier, BigDecimal balance, String owner, String currency) {

    public Account {
        if (identifier == null) {
            throw new BadRequestException("account identifier is required");
        }
        if (balance == null) {
            throw new BadRequestException("balance is required");
        }
        if (owner == null || owner.isBlank()) {
            throw new BadRequestException("owner is required");
        }
        if (currency == null || currency.isBlank()) {
            throw new BadRequestException("currency is required");
        }
    }

    public static Account fromJSON(JsonObject json) {
        return new Account(
                AccountIdentifier.fromJSON(json),
                json.getJsonNumber("balance").bigDecimalValue(),
                json.getString("owner"),
                json.getString("currency")
        );
    }

    public JsonObject toJSON() {
        var builder = Json.createObjectBuilder();
        this.identifier.toJSON().forEach(builder::add);
        return builder
                .add("balance", this.balance)
                .add("owner", this.owner)
                .add("currency", this.currency)
                .build();
    }
}
