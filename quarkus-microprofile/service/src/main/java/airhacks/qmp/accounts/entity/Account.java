package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Account(String iban, BigDecimal balance, String owner, String currency) {

    public static Account fromJSON(JsonObject json) {
        return new Account(
                json.getString("iban"),
                json.getJsonNumber("balance").bigDecimalValue(),
                json.getString("owner"),
                json.getString("currency")
        );
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("iban", this.iban)
                .add("balance", this.balance)
                .add("owner", this.owner)
                .add("currency", this.currency)
                .build();
    }
}
