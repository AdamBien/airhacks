package oliver;

record Order(String product, int quantity) {

    String toJson() {
        return """
                {"product":"%s","quantity":%d}""".formatted(product, quantity);
    }

    static String toJsonArray(java.util.List<Order> orders) {
        var entries = orders.stream()
                .map(Order::toJson)
                .collect(java.util.stream.Collectors.joining(","));
        return "[" + entries + "]";
    }
}
