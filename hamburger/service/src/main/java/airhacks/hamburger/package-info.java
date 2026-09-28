/// # Hamburger
/// > Let a customer order hamburgers from a fixed menu and receive a priced, confirmed order.
///
/// ## Components
/// - `ordering` may call `menu` to resolve and price items; never the reverse.
/// - `health` holds only application-wide probes and calls no business component.
///
/// ## Ubiquitous language
/// - MenuItem — a hamburger the shop sells, identified by name, with calories and a price in cents. Owned by `menu`.
/// - Order — a confirmed, priced intent to buy one or more hamburgers. Owned by `ordering`.
/// - OrderLine — one distinct MenuItem in an Order together with its quantity.
///
/// ## Decisions
/// - D1 — `menu` is its own BC, called by `ordering`. _(why: the menu changes independently of how orders are taken; rejected: a single `ordering` BC owning the item list, and an `ordering` + `payment` carving with the menu folded into ordering)_
///
/// ## Stack
/// - microprofile-server · base package `airhacks.hamburger`
package airhacks.hamburger;
