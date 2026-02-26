package airhacks.qmp.customers.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.qmp.customers.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerStore {

    static System.Logger LOG = System.getLogger(CustomerStore.class.getName());

    List<Customer> customers = new CopyOnWriteArrayList<>();

    public List<Customer> all() {
        LOG.log(INFO, "returning all customers");
        return this.customers;
    }

    public Customer add(Customer customer) {
        LOG.log(INFO, "adding customer: " + customer.name());
        this.customers.add(customer);
        return customer;
    }
}
