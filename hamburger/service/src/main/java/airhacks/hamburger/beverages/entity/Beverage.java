package airhacks.hamburger.beverages.entity;

/// A drink the shop sells in one fixed size. The name is its identity; the price is in integer cents.
public record Beverage(String name, int calories, int priceInCents) {
}
