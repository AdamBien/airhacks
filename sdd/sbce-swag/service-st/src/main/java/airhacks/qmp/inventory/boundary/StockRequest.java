package airhacks.qmp.inventory.boundary;

public record StockRequest(String item, String size, int quantity) {
}
