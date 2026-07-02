package airhacks.qmp.inventory.entity;

public record Stock(SwagItem item, String size, int quantity) {

    public boolean available() {
        return this.quantity > 0;
    }

    public Stock reserveOne() {
        return new Stock(this.item, this.size, this.quantity - 1);
    }
}
