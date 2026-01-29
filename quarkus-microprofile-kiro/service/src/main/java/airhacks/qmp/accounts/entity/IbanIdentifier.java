package airhacks.qmp.accounts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * IBAN-based account identifier for European accounts.
 * Follows ISO 13616 International Bank Account Number format.
 */
public record IbanIdentifier(String iban) implements AccountIdentifier {
    
    public IbanIdentifier {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN is required");
        }
    }
    
    @Override
    public String value() {
        return iban;
    }
    
    @Override
    public Region region() {
        return Region.EUROPE;
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "IBAN")
            .add("iban", iban)
            .build();
    }
}
