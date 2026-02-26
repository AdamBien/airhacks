package airhacks.addresses.entity;

public record Address(String street) {

    public String toJson() {
        return "{\"street\":\"" + this.street + "\"}";
    }

    public static Address fromJson(String json) {
        var street = json.replaceAll(".*\"street\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        return new Address(street);
    }
}
