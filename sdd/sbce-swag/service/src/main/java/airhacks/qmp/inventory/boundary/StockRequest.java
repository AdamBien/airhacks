package airhacks.qmp.inventory.boundary;

import airhacks.qmp.inventory.entity.SwagItem;

public record StockRequest(SwagItem item, String size, int quantity) {
}
