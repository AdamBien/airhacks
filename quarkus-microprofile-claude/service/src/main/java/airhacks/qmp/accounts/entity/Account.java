package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;

import airhacks.qmp.ValidationMessages;
import airhacks.qmp.owners.entity.Owner;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;

public record Account(AccountIdentifier identifier, BigDecimal balance, Owner owner, String currency) {

    public Account {
        if (identifier == null) {
            throw new BadRequestException(ValidationMessages.get("account.identifier.required"));
        }
        if (balance == null) {
            throw new BadRequestException(ValidationMessages.get("balance.required"));
        }
        if (owner == null) {
            throw new BadRequestException(ValidationMessages.get("owner.required"));
        }
        if (currency == null || currency.isBlank()) {
            throw new BadRequestException(ValidationMessages.get("currency.required"));
        }
    }

    public static Account fromJSON(JsonObject json) {
        return new Account(
                AccountIdentifier.fromJSON(json),
                json.getJsonNumber("balance").bigDecimalValue(),
                Owner.fromJSON(json.getJsonObject("owner")),
                json.getString("currency")
        );
    }

    public JsonObject toJSON() {
        var builder = Json.createObjectBuilder();
        this.identifier.toJSON().forEach(builder::add);
        return builder
                .add("balance", this.balance)
                .add("owner", this.owner.toJSON())
                .add("currency", this.currency)
                .build();
    }
}
