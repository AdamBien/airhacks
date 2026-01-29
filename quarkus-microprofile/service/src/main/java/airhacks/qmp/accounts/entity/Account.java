package airhacks.qmp.accounts.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Represents a bank account with balance, currency, and ownership information.
 * Supports multiple regional identification systems and multi-currency operations.
 */
public record Account(
    String id,
    AccountIdentifier identifier,
    AccountHolder holder,
    Currency currency,
    Region region,
    BigDecimal balance,
    String swiftBic,
    boolean closed,
    Instant createdAt
) {
    
    public Account {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }
    
    /**
     * Creates a new account with zero balance and current timestamp.
     */
    public static Account create(AccountHolder holder, Currency currency, Region region,
                                  AccountIdentifier identifier, String swiftBic) {
        return new Account(
            UUID.randomUUID().toString(),
            identifier,
            holder,
            currency,
            region,
            BigDecimal.ZERO,
            swiftBic,
            false,
            Instant.now()
        );
    }
    
    /**
     * Returns a copy of this account with the specified balance.
     */
    public Account withBalance(BigDecimal newBalance) {
        return new Account(id, identifier, holder, currency, region, newBalance, swiftBic, closed, createdAt);
    }
    
    /**
     * Returns a copy of this account marked as closed.
     */
    public Account asClosed() {
        return new Account(id, identifier, holder, currency, region, balance, swiftBic, true, createdAt);
    }
    
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("id", id)
            .add("identifier", identifier.toJson())
            .add("holder", holder.toJson())
            .add("currency", currency.name())
            .add("region", region.name())
            .add("balance", balance)
            .add("swiftBic", swiftBic)
            .add("closed", closed)
            .add("createdAt", createdAt.toString())
            .build();
    }
    
    public static Account fromJson(JsonObject json) {
        var id = json.getString("id");
        var identifierJson = json.getJsonObject("identifier");
        var identifier = parseIdentifier(identifierJson);
        var holder = AccountHolder.fromJson(json.getJsonObject("holder"));
        var currency = Currency.valueOf(json.getString("currency"));
        var region = Region.valueOf(json.getString("region"));
        var balance = json.getJsonNumber("balance").bigDecimalValue();
        var swiftBic = json.getString("swiftBic");
        var closed = json.getBoolean("closed");
        var createdAt = Instant.parse(json.getString("createdAt"));
        
        return new Account(id, identifier, holder, currency, region, balance, swiftBic, closed, createdAt);
    }
    
    static AccountIdentifier parseIdentifier(JsonObject json) {
        var type = json.getString("type");
        return switch (type) {
            case "IBAN" -> new IbanIdentifier(json.getString("iban"));
            case "INDIA" -> new IndiaIdentifier(json.getString("accountNumber"), json.getString("ifscCode"));
            case "US" -> new UsIdentifier(json.getString("accountNumber"), json.getString("routingNumber"));
            case "ASIA_PACIFIC" -> new AsiaPacificIdentifier(json.getString("accountNumber"), json.getString("bankCode"));
            default -> throw new IllegalArgumentException("Unknown identifier type: " + type);
        };
    }
}
