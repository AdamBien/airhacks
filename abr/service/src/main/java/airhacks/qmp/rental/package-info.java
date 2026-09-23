/// # Rental
/// > Hand an available bike to a walk-in or reserving customer and settle the rental on return.
///
/// ## Boundary
/// - `start-rental` — hand an available bike of the requested type to a customer now, optionally redeeming a reservation
/// - `return-bike` — take the bike back, close the rental, and record the price
/// - `find-rental` — look up a rental by its identifier
///
/// ## Requirements
/// ### R1: Start a rental
/// - R1.1 — When a customer with contact details requests a bike type and a bike of that type is available, the BC shall allocate one and open a rental starting now.
/// - R1.2 — If no bike of the requested type is available, then the BC shall reject the start.
/// - R1.3 — If the customer's contact details are missing, then the BC shall reject the start.
/// - R1.4 — When a reservation identifier is given, the BC shall redeem the reservation and open the rental for the reserved bike type.
/// - R1.5 — If the reservation cannot be redeemed, then the BC shall reject the start and leave the fleet unchanged. _(why: a failed pickup must never silently consume a bike)_
///
/// ### R2: Return a bike
/// - R2.1 — While a rental is open, when its bike is returned, the BC shall close the rental, release the bike, and record the price.
/// - R2.2 — If a closed rental is returned again, then the BC shall reject the return.
/// - R2.3 — If an unknown rental is returned, then the BC shall reject the return.
///
/// ### R3: Price a rental
/// - R3.1 — The BC shall charge the daily rate of the rented bike's type for every started day between start and return.
/// - R3.2 — The BC shall charge a higher daily rate for an e-bike than for a bike.
/// - R3.3 — When a bike is returned within the first day, the BC shall charge exactly one day.
///
/// ### R4: Find a rental
/// - R4.1 — When a rental is looked up by its identifier, the BC shall return it with its current state.
/// - R4.2 — If the identifier is unknown, then the BC shall reject the lookup.
///
/// ## Entities
/// - Rental
///
/// ## Out of scope
/// - Collecting payment, deposits, and refunds.
/// - Assessing damage on return.
/// - Which physical bikes exist and their condition — owned by `fleet`.
/// - Creating and cancelling reservations — owned by `reservation`.
package airhacks.qmp.rental;
