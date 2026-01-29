package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Account(String accountNumber, String routingCode, BigDecimal balance, String owner, String currency) {

    public static Account fromJSON(JsonObject json) {
        return new Account(
                json.getString("accountNumber"),
                json.getString("routingCode"),
                json.getJsonNumber("balance").bigDecimalValue(),
                json.getString("owner"),
                json.getString("currency")
        );
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("accountNumber", this.accountNumber)
                .add("routingCode", this.routingCode)
                .add("balance", this.balance)
                .add("owner", this.owner)
                .add("currency", this.currency)
                .build();
    }
}
