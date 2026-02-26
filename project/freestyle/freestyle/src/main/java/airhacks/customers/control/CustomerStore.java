package airhacks.customers.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.addresses.entity.Address;
import airhacks.customers.entity.Customer;

public class CustomerStore {

    ConcurrentHashMap<String, Customer> customers = new ConcurrentHashMap<>();

    public Customer create(Customer customer) {
        this.customers.put(customer.name(), customer);
        return customer;
    }

    public Customer find(String name) {
        return this.customers.get(name);
    }

    public List<Customer> findAll() {
        return List.copyOf(this.customers.values());
    }

    public void delete(String name) {
        this.customers.remove(name);
    }

    public Customer addAddress(String name, Address address) {
        return this.customers.computeIfPresent(name, (k, customer) -> {
            var addresses = new ArrayList<>(customer.addresses());
            addresses.add(address);
            return customer.withAddresses(List.copyOf(addresses));
        });
    }

    public Customer removeAddress(String name, String street) {
        return this.customers.computeIfPresent(name, (k, customer) -> {
            var addresses = customer.addresses().stream()
                    .filter(a -> !a.street().equals(street))
                    .toList();
            return customer.withAddresses(addresses);
        });
    }
}
