package airhacks.qmp.orders.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.qmp.orders.entity.Order;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderStore {

    static System.Logger LOG = System.getLogger(OrderStore.class.getName());

    List<Order> orders = new CopyOnWriteArrayList<>();

    public List<Order> all() {
        LOG.log(INFO, "returning all orders");
        return this.orders;
    }

    public Order add(Order order) {
        LOG.log(INFO, "adding order: " + order.product());
        this.orders.add(order);
        return order;
    }

    public List<Order> findByCustomerId(String customerId) {
        LOG.log(INFO, "finding orders for customer: " + customerId);
        return this.orders.stream()
                .filter(o -> o.customerId().equals(customerId))
                .toList();
    }
}
