package airhacks.addresses.control;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.addresses.entity.Address;

public class AddressStore {

    ConcurrentHashMap<String, Address> addresses = new ConcurrentHashMap<>();

    public Address create(Address address) {
        this.addresses.put(address.street(), address);
        return address;
    }

    public Address find(String street) {
        return this.addresses.get(street);
    }

    public List<Address> findAll() {
        return List.copyOf(this.addresses.values());
    }

    public void delete(String street) {
        this.addresses.remove(street);
    }
}
