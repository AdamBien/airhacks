/// # Inventory
/// > Track per-size swag stock and reserve it atomically as claims are accepted.
///
/// ## Boundary
/// - `set-stock` — an organizer sets or adjusts the available quantity for an item and size
/// - `reserve-swag` — reserve one t-shirt size and one socks size for an accepted claim
/// - `list-availability` — report the remaining quantity for each item and size
///
/// ## Requirements
/// ### R1: Set stock
/// - R1.1 — When an organizer sets a quantity for a recognised item and size, the BC shall record it as the available stock for that item and size.
/// - R1.2 — If the quantity is negative, then the BC shall reject the update. _(why: stock can never be negative)_
/// - R1.3 — If the item or the size is not a recognised swag option, then the BC shall reject the update.
///
/// ### R2: Reserve stock for a claim
/// - R2.1 — When a reservation requests a t-shirt size and a socks size that both have remaining stock, the BC shall decrement each by one and confirm the reservation.
/// - R2.2 — If either requested size has no remaining stock, then the BC shall reject the reservation without decrementing any stock. _(why: a claim reserves a full pack or nothing — never a partial pack)_
///
/// ### R3: Report availability
/// - R3.1 — When availability is requested, the BC shall report the remaining quantity for each item and size.
///
/// ## Entities
/// - Stock, SwagItem
///
/// ## Out of scope
/// - releasing a reservation (no claim cancellation exists yet)
/// - back-orders and waitlists
/// - low-stock notifications
/// - persistence beyond process memory
package airhacks.qmp.inventory;
