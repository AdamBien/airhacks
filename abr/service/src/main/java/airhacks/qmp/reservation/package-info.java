/// # Reservation
/// > Hold a bike type for a customer's pickup day and hand it over exactly once.
///
/// ## Boundary
/// - `reserve-bike` — hold a bike type for a customer on a pickup day
/// - `cancel-reservation` — withdraw an open reservation before pickup
/// - `redeem-reservation` — consume an open reservation at pickup (called by `rental`)
/// - `find-reservation` — look up a reservation by its identifier
///
/// ## Requirements
/// ### R1: Reserve a bike type
/// - R1.1 — When a customer with contact details reserves a bike type for a pickup day that is today or later, the BC shall create an open reservation.
/// - R1.2 — If the pickup day lies in the past, then the BC shall reject the reservation.
/// - R1.3 — If the type is neither bike nor e-bike, then the BC shall reject the reservation.
/// - R1.4 — If the customer's contact details are missing, then the BC shall reject the reservation.
/// - R1.5 — If the open reservations of that type on that pickup day already equal the fleet's in-service count of that type, then the BC shall reject the reservation. _(why: overbooking is the fastest way to lose a reputation for quality)_
///
/// ### R2: Cancel a reservation
/// - R2.1 — While a reservation is open, when it is cancelled, the BC shall mark it cancelled.
/// - R2.2 — If a reservation that is not open is cancelled, then the BC shall reject the cancellation.
///
/// ### R3: Redeem a reservation
/// - R3.1 — While a reservation is open, when it is redeemed on its pickup day, the BC shall mark it redeemed.
/// - R3.2 — If a reservation is redeemed on a day other than its pickup day, then the BC shall reject the redemption.
/// - R3.3 — If a reservation that is not open is redeemed, then the BC shall reject the redemption.
///
/// ### R4: Expire a reservation
/// - R4.1 — While the pickup day has passed, the BC shall report a still-open reservation as expired.
///
/// ### R5: Find a reservation
/// - R5.1 — When a reservation is looked up by its identifier, the BC shall return it with its current state.
/// - R5.2 — If the identifier is unknown, then the BC shall reject the lookup.
///
/// ## Entities
/// - Reservation
///
/// ## Out of scope
/// - Choosing the physical bike — done by `fleet` when `rental` starts.
/// - Pricing, deposits, and no-show fees.
package airhacks.qmp.reservation;
