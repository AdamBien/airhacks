package airhacks.qmp.accounts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Account identifier for US accounts.
 * Uses account number combined with ABA routing number.
 */
public record UsIdentifier(String accountNumber, String routingNumber) implements AccountIdentifier {
    
    public UsIdentifier {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (routingNumber == null || routingNumber.isBlank()) {
            throw new IllegalArgumentException("Routing number is required");
        }
    }
    
    @Override
    public String value() {
        return accountNumber;
    }
    
    @Override
    public Region region() {
        return Region.US;
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "US")
            .add("accountNumber", accountNumber)
            .add("routingNumber", routingNumber)
            .build();
    }
}
