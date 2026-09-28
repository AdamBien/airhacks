/// # Ordering
/// > Accept a customer's hamburger order, price it from the menu, and keep it retrievable.
///
/// ## Boundary
/// - `place-order` — submit one or more order lines and receive a confirmed, priced order with an id
/// - `find-order` — retrieve a previously placed order by id
///
/// ## Requirements
/// ### R1: Place an order
/// - R1.1 — When an order with at least one valid line is placed, the BC shall confirm it with a unique order id, its lines, and a total in cents.
/// - R1.2 — When an order is placed, the BC shall price each line as the menu price of its item times its quantity, and the total as the sum of all lines. _(why: prices are fixed at placement so a later menu change never reprices a confirmed order)_
/// - R1.3 — If an order has no lines, then the BC shall reject it.
/// - R1.4 — If a line names an item the menu does not know, then the BC shall reject the order.
/// - R1.5 — If a line has a quantity below 1 or above 10, then the BC shall reject the order. _(why: keeps kitchen batches bounded)_
/// - R1.6 — If two lines name the same item, then the BC shall reject the order. _(why: the customer adjusts the quantity instead)_
///
/// ### R2: Find an order
/// - R2.1 — When an existing order id is requested, the BC shall return that order as confirmed at placement.
/// - R2.2 — If an unknown order id is requested, then the BC shall report that no such order exists.
///
/// ## Entities
/// - Order, OrderLine
///
/// ## Out of scope
/// - Cancellation or any status change after placement.
/// - Payment.
/// - Composing a burger from patties and toppings; only fixed menu items are ordered.
package airhacks.hamburger.ordering;
