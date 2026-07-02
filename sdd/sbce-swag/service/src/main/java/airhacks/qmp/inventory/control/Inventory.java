package airhacks.qmp.inventory.control;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.qmp.inventory.entity.Stock;
import airhacks.qmp.inventory.entity.SwagItem;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Inventory {

    record Key(SwagItem item, String size) {
    }

    final Map<Key, Stock> levels = new ConcurrentHashMap<>();

    public void set(SwagItem item, String size, int quantity) {
        if (quantity < 0) {
            throw StockRejected.negative();
        }
        if (item == null || !item.offers(size)) {
            throw StockRejected.unrecognised();
        }
        this.levels.put(new Key(item, size), new Stock(item, size, quantity));
    }

    public synchronized boolean reserve(String tShirtSize, String socksSize) {
        var teeKey = new Key(SwagItem.T_SHIRT, tShirtSize);
        var socksKey = new Key(SwagItem.SOCKS, socksSize);
        var tee = this.levels.get(teeKey);
        var socks = this.levels.get(socksKey);
        if (tee == null || socks == null || !tee.available() || !socks.available()) {
            return false;
        }
        this.levels.put(teeKey, tee.reserveOne());
        this.levels.put(socksKey, socks.reserveOne());
        return true;
    }

    public List<Stock> availability() {
        return List.copyOf(this.levels.values());
    }
}
