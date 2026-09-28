/// # Hamburger
/// > Let a customer order hamburgers from a fixed menu and receive a priced, confirmed order.
///
/// ## Components
/// - `ordering` may call `menu` to resolve and price items; never the reverse.
/// - `ordering` may call `beverages` to resolve and price beverage lines; never the reverse.
/// - `menu` and `beverages` never call each other.
/// - `health` holds only application-wide probes and calls no business component.
///
/// ## System invariants
/// - S1 — The system shall never offer a beverage and a menu item under the same name. _(why: an order line names an item only, so the name must resolve to exactly one of them)_
///
/// ## Ubiquitous language
/// - MenuItem — a hamburger the shop sells, identified by name, with calories and a price in cents. Owned by `menu`.
/// - Order — a confirmed, priced intent to buy one or more hamburgers. Owned by `ordering`.
/// - Beverage — a drink the shop sells, identified by name, with calories and a price in cents, one fixed size. Owned by `beverages`.
/// - OrderLine — one distinct MenuItem or Beverage in an Order together with its quantity.
///
/// ## Decisions
/// - D1 — `menu` is its own BC, called by `ordering`. _(why: the menu changes independently of how orders are taken; rejected: a single `ordering` BC owning the item list, and an `ordering` + `payment` carving with the menu folded into ordering)_
/// - D2 — `beverages` is its own BC, called by `ordering`. _(why: drinks and hamburgers change independently; rejected: extending `menu` with a kind per item)_
/// - D3 — Item names are unique across `menu` and `beverages`, so an order line names an item only. _(why: keeps the order line shape unchanged; rejected: a kind on every order line; resolving the menu first and beverages only on a miss)_
/// - D4 — An order may consist of beverages only. _(why: a drink is a sale on its own; rejected: at least one hamburger per order)_
///
/// ## Stack
/// - microprofile-server · base package `airhacks.hamburger`
package airhacks.hamburger;
