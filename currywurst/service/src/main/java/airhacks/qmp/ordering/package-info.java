/// # Ordering
/// > Take a customer's order at the counter, number it for the day, and keep it until it is cancelled or handed over.
///
/// ## Boundary
/// - `place-order` — accept order lines, validate and price them against `menu`, and return the new order with its number
/// - `get-order` — return one of today's orders by its order number
/// - `cancel-order` — withdraw one of today's orders while it has not left the `placed` state
/// - `list-open-orders` — return today's orders that are neither cancelled nor handed over, oldest first
///
/// ## Requirements
/// ### R1: Place an order
/// - R1.1 — When an order with at least one valid line is placed, the BC shall create the order in state `placed` and return it with its order number.
/// - R1.2 — When an order is placed, the BC shall assign the next order number of the current day, starting at 1 for the first order of each day. _(why: a short number is called out at hand-over — D2)_
/// - R1.3 — When an order is placed, the BC shall price each line as the item's menu price times its quantity and the order total as the sum of its lines, in cents.
/// - R1.4 — If the order has no lines, then the BC shall reject it.
/// - R1.5 — If any line names an item unknown to `menu`, then the BC shall reject the whole order.
/// - R1.6 — If any line has a quantity below 1, then the BC shall reject the whole order.
///
/// ### R2: Look up an order
/// - R2.1 — When an order is requested by the number of one of today's orders, the BC shall return it with its lines, total, and state.
/// - R2.2 — If the order number matches none of today's orders, then the BC shall report the order as not found.
///
/// ### R3: Cancel an order
/// - R3.1 — While an order is in state `placed`, when it is cancelled, the BC shall move it to state `cancelled`.
/// - R3.2 — If an order that has left the `placed` state is cancelled, then the BC shall reject the cancellation and leave its state unchanged. _(why: food already on the grill cannot be un-cooked — D3)_
/// - R3.3 — If the order number matches none of today's orders, then the BC shall report the order as not found.
///
/// ### R4: List open orders
/// - R4.1 — When open orders are listed, the BC shall return every one of today's orders whose state is neither `cancelled` nor `handed-over`, oldest first.
/// - R4.2 — While today has no open order, the BC shall return an empty list.
///
/// ## Entities
/// - Order, OrderLine, OrderState
///
/// ## Out of scope
/// - Payment; the order carries a total but no payment record.
/// - Special requests or notes on an order or line.
/// - Customer identity; the order number is the only identifier.
/// - Orders of previous days; a number addresses today's order only.
/// - Preparation and hand-over transitions — future BCs; `ordering` only reads the resulting state.
/// - The item catalog — owned by `menu`.
package airhacks.qmp.ordering;
