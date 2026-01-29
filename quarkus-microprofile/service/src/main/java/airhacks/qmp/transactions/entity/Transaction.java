package airhacks.qmp.transactions.entity;

import java.math.BigDecimal;
import java.time.Instant;

import airhacks.qmp.accounts.entity.Currency;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Represents a financial transaction that modifies an account balance.
 * Records deposits, withdrawals, and transfers with full audit trail.
 */
public record Transaction(
    String id,
    String accountId,
    TransactionType type,
    BigDecimal amount,
    Currency currency,
    BigDecimal balanceAfter,
    String referenceNumber,
    String relatedAccountId,
    TransactionStatus status,
    Instant timestamp
) {
    
    public JsonObject toJson() {
        var builder = Json.createObjectBuilder()
            .add("id", id)
            .add("accountId", accountId)
            .add("type", type.name())
            .add("amount", amount)
            .add("currency", currency.name())
            .add("balanceAfter", balanceAfter)
            .add("referenceNumber", referenceNumber)
            .add("status", status.name())
            .add("timestamp", timestamp.toString());
        
        if (relatedAccountId != null) {
            builder.add("relatedAccountId", relatedAccountId);
        }
        
        return builder.build();
    }
    
    public static Transaction fromJson(JsonObject json) {
        var id = json.getString("id");
        var accountId = json.getString("accountId");
        var type = TransactionType.valueOf(json.getString("type"));
        var amount = json.getJsonNumber("amount").bigDecimalValue();
        var currency = Currency.valueOf(json.getString("currency"));
        var balanceAfter = json.getJsonNumber("balanceAfter").bigDecimalValue();
        var referenceNumber = json.getString("referenceNumber");
        var relatedAccountId = json.containsKey("relatedAccountId") && !json.isNull("relatedAccountId")
            ? json.getString("relatedAccountId")
            : null;
        var status = TransactionStatus.valueOf(json.getString("status"));
        var timestamp = Instant.parse(json.getString("timestamp"));
        
        return new Transaction(id, accountId, type, amount, currency, balanceAfter, 
                               referenceNumber, relatedAccountId, status, timestamp);
    }
}
