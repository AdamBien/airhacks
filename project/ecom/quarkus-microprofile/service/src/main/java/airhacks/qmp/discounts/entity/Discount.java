package airhacks.qmp.discounts.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Discount.all", query = "SELECT d FROM Discount d")
@NamedQuery(name = "Discount.active", query = "SELECT d FROM Discount d WHERE d.startDate <= :today AND d.endDate >= :today")
public class Discount {

    @Id
    String discountId;
    String code;
    String description;
    BigDecimal percentage;
    LocalDate startDate;
    LocalDate endDate;

    public Discount() {
    }

    public Discount(String discountId, String code, String description, BigDecimal percentage, LocalDate startDate, LocalDate endDate) {
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidDiscountPercentageException(percentage);
        }
        this.discountId = discountId;
        this.code = code;
        this.description = description;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String discountId() {
        return discountId;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    public BigDecimal percentage() {
        return percentage;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("discountId", this.discountId)
                .add("code", this.code)
                .add("description", this.description)
                .add("percentage", this.percentage)
                .add("startDate", this.startDate.toString())
                .add("endDate", this.endDate.toString())
                .build();
    }

    public static Discount fromJSON(JsonObject json) {
        return new Discount(
                json.getString("discountId", null),
                json.getString("code"),
                json.getString("description"),
                json.getJsonNumber("percentage").bigDecimalValue(),
                LocalDate.parse(json.getString("startDate")),
                LocalDate.parse(json.getString("endDate")));
    }
}
