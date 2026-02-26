package oliver;

record Customer(String name, String email, int age, Address address, java.util.List<Order> orders) {

    Customer {
        if (age < 0 || age > 90) {
            throw new IllegalArgumentException("Age must be between 0 and 90");
        }
    }

    String toJson() {
        return """
                {"name":"%s","email":"%s","age":%d,"address":%s,"orders":%s}""".formatted(
                name, email, age, address.toJson(), Order.toJsonArray(orders));
    }

    static Customer fromJson(String json) {
        var name = JsonUtil.extract(json, "name");
        var email = JsonUtil.extract(json, "email");
        var age = Integer.parseInt(JsonUtil.extract(json, "age"));
        var address = Address.fromJson(json);
        return new Customer(name, email, age, address, java.util.List.of());
    }

    static String toJsonArray(java.util.List<Customer> customers) {
        var entries = customers.stream()
                .map(Customer::toJson)
                .collect(java.util.stream.Collectors.joining(","));
        return "[" + entries + "]";
    }
}
