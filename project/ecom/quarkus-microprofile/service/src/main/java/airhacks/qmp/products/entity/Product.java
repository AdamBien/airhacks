package airhacks.qmp.products.entity;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "all", query = "SELECT p FROM Product p")
public class Product {

    @Id
    String productId;
    String name;
    String description;
    BigDecimal price;
    @Enumerated(EnumType.STRING)
    Color color;

    public Product() {
    }

    public Product(String productId, String name, String description, BigDecimal price, Color color) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException(price);
        }
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.color = color;
    }

    public String productId() {
        return productId;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public BigDecimal price() {
        return price;
    }

    public Color color() {
        return color;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("productId", this.productId)
                .add("name", this.name)
                .add("description", this.description)
                .add("price", this.price)
                .add("color", this.color.name().toLowerCase())
                .build();
    }

    public static Product fromJSON(JsonObject json) {
        return new Product(
                json.getString("productId"),
                json.getString("name"),
                json.getString("description"),
                json.getJsonNumber("price").bigDecimalValue(),
                Color.valueOf(json.getString("color").toUpperCase()));
    }
}
