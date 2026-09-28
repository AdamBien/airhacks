package airhacks.hamburger.menu.entity;

/// A hamburger the shop sells. The name is its identity; calories are per item; the price is in integer cents.
public record MenuItem(String name, int calories, int priceInCents) {
}
