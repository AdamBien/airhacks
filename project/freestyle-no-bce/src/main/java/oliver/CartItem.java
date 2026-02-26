package oliver;

import java.util.stream.Collectors;

record CartItem(long productId, String productName, int quantity, double price) {

    String toJson() {
        return """
                {"productId":%d,"productName":"%s","quantity":%d,"price":%.2f}""".formatted(
                productId, productName, quantity, price);
    }

    static String toJsonArray(java.util.List<CartItem> items) {
        var entries = items.stream()
                .map(CartItem::toJson)
                .collect(Collectors.joining(","));
        return "[" + entries + "]";
    }
}
