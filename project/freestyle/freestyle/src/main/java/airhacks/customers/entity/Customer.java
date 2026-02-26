package airhacks.customers.entity;

import java.util.List;
import java.util.stream.Collectors;

import airhacks.addresses.entity.Address;

public record Customer(String name, List<Address> addresses) {

    public Customer(String name) {
        this(name, List.of());
    }

    public Customer withAddresses(List<Address> addresses) {
        return new Customer(this.name, addresses);
    }

    public String toJson() {
        var addressesJson = this.addresses.stream()
                .map(Address::toJson)
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"name\":\"" + this.name + "\",\"addresses\":" + addressesJson + "}";
    }

    public static Customer fromJson(String json) {
        var name = json.replaceAll(".*\"name\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        return new Customer(name);
    }
}
