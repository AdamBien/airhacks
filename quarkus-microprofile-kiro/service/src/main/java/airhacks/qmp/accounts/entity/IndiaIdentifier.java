package airhacks.qmp.accounts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Account identifier for Indian accounts.
 * Uses account number combined with IFSC (Indian Financial System Code).
 */
public record IndiaIdentifier(String accountNumber, String ifscCode) implements AccountIdentifier {
    
    public IndiaIdentifier {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (ifscCode == null || ifscCode.isBlank()) {
            throw new IllegalArgumentException("IFSC code is required");
        }
    }
    
    @Override
    public String value() {
        return accountNumber;
    }
    
    @Override
    public Region region() {
        return Region.INDIA;
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "INDIA")
            .add("accountNumber", accountNumber)
            .add("ifscCode", ifscCode)
            .build();
    }
}
