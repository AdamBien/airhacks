package oliver;

record Address(String street, String city) {

    String toJson() {
        return """
                {"street":"%s","city":"%s"}""".formatted(street, city);
    }

    static Address fromJson(String json) {
        var street = JsonUtil.extract(json, "street");
        var city = JsonUtil.extract(json, "city");
        return new Address(street, city);
    }
}
