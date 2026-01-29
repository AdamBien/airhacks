package airhacks.qmp.accounts.entity;

import jakarta.json.JsonObject;

/**
 * Sealed interface for region-specific account identifiers.
 * Each region uses a different identification system for bank accounts.
 */
public sealed interface AccountIdentifier 
    permits IbanIdentifier, IndiaIdentifier, UsIdentifier, AsiaPacificIdentifier {
    
    /**
     * Returns the primary identifier value for the account.
     */
    String value();
    
    /**
     * Returns the geographic region this identifier belongs to.
     */
    Region region();
    
    /**
     * Serializes the identifier to JSON format.
     */
    JsonObject toJson();
}
