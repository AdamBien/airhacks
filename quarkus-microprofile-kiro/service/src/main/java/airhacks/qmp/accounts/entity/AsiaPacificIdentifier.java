package airhacks.qmp.accounts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Account identifier for Asia-Pacific region accounts.
 * Uses account number combined with bank code.
 */
public record AsiaPacificIdentifier(String accountNumber, String bankCode) implements AccountIdentifier {
    
    public AsiaPacificIdentifier {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (bankCode == null || bankCode.isBlank()) {
            throw new IllegalArgumentException("Bank code is required");
        }
    }
    
    @Override
    public String value() {
        return accountNumber;
    }
    
    @Override
    public Region region() {
        return Region.ASIA_PACIFIC;
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "ASIA_PACIFIC")
            .add("accountNumber", accountNumber)
            .add("bankCode", bankCode)
            .build();
    }
}
