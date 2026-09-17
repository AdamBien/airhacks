/// # Currywurst Stand
/// > Run a Berlin Currywurst stand's workflow from order intake at the counter to hand-over to the customer.
///
/// ## Components
/// - `ordering` may call `menu` to validate and price order lines; never the reverse.
/// - `health` and `greetings` are template infrastructure with no business wiring.
///
/// ## Ubiquitous language
/// - Menu item — a sellable item with a name and a price in whole cents. Owned by `menu`.
/// - Order — a customer's counter order: numbered lines, a total, and a state. Owned by `ordering`.
/// - Order line — one menu item and its quantity within an order.
/// - Order number — a per-day sequence starting at 1, called out at hand-over; addresses today's orders only.
/// - Order state — `placed` and `cancelled` are set by `ordering`; `in-preparation`, `ready`, and `handed-over` are reserved for future BCs.
///
/// ## Decisions
/// - D1 — `menu` is its own BC with a read-only, built-in catalog. _(why: catalog and prices change independently of intake; rejected: fixed catalog inside `ordering`, runtime-editable menu)_
/// - D2 — Orders are identified by a per-day sequential number. _(why: short call-out number at hand-over; rejected: global sequence, opaque id)_
/// - D3 — An order can be cancelled only while in state `placed`. _(why: food already on the grill cannot be un-cooked; rejected: cancel until hand-over, cancel within a time window)_
///
/// ## Stack
/// - microprofile-server · base package `airhacks.qmp` · modules `service` (application) and `service-st` (system tests)
package airhacks.qmp;
