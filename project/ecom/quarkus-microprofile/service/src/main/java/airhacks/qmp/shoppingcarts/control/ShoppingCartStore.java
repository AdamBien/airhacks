package airhacks.qmp.shoppingcarts.control;

import static java.lang.System.Logger.Level.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.shoppingcarts.entity.ShoppingCart;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShoppingCartStore {

    static System.Logger LOG = System.getLogger(ShoppingCartStore.class.getName());

    Map<String, ShoppingCart> carts = new ConcurrentHashMap<>();

    public List<ShoppingCart> all() {
        LOG.log(INFO, "returning all shopping carts");
        return List.copyOf(this.carts.values());
    }

    public ShoppingCart findByCustomerId(String customerId) {
        LOG.log(INFO, "finding cart for customer: " + customerId);
        return this.carts.get(customerId);
    }

    public ShoppingCart save(ShoppingCart cart) {
        LOG.log(INFO, "saving cart for customer: " + cart.customerId());
        this.carts.put(cart.customerId(), cart);
        return cart;
    }
}
