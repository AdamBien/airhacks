package oliver;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

record Product(long id, String name, String description, double price) {

    static final AtomicLong ID_GEN = new AtomicLong();

    static Product create(String name, String description, double price) {
        return new Product(ID_GEN.incrementAndGet(), name, description, price);
    }

    static Product fromJson(String json) {
        var name = JsonUtil.extract(json, "name");
        var description = JsonUtil.extract(json, "description");
        var price = Double.parseDouble(JsonUtil.extract(json, "price"));
        return create(name, description, price);
    }

    String toJson() {
        return """
                {"id":%d,"name":"%s","description":"%s","price":%.2f}""".formatted(
                id, name, description, price);
    }

    static String toJsonArray(java.util.List<Product> products) {
        var entries = products.stream()
                .map(Product::toJson)
                .collect(Collectors.joining(","));
        return "[" + entries + "]";
    }
}
