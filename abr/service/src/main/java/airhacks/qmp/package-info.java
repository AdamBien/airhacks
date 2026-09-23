/// # Amsterdam Bike Rental
/// > Rent the nicest bikes and e-bikes in Amsterdam to walk-in and reserving customers, from pickup to return.
///
/// ## Vision
/// - Renting a great bike in Amsterdam takes one minute and needs no explanation.
///
/// ## Components
/// - `rental` may call `fleet` (`allocate-bike`, `release-bike`); never the reverse.
/// - `rental` may call `reservation` (`redeem-reservation`); never the reverse.
/// - `reservation` may call `fleet` (`count-bikes`); never the reverse.
/// - `health` calls no business component.
///
/// ## System invariants
/// - S1 — The system shall never hold one bike in two open rentals at the same time.
/// - S2 — The system shall never hold more open reservations of a type for one pickup day than in-service bikes of that type.
///
/// ## Ubiquitous language
/// - Bike — one physical bicycle in the fleet, of type bike or e-bike. Owned by `fleet`.
/// - Bike type — bike or e-bike; the unit customers reserve and the unit of pricing.
/// - Customer — the renting person, identified by contact details; a value carried by rentals and reservations, not an owned entity.
/// - Rental — one bike in a customer's hands from start to return. Owned by `rental`.
/// - Reservation — a bike type held for a customer on a pickup day. Owned by `reservation`.
/// - Pickup day — the calendar day on which a reservation is redeemed.
///
/// ## Decisions
/// - D1 — Three BCs: `fleet`, `rental`, `reservation`. _(why: a reservation holds a type ahead of time while a rental hands over a bike now — different lifecycles; rejected: `fleet` + `rental` only, a single `rental` BC)_
///
/// ## Stack
/// - microprofile-server (Quarkus) · base package `airhacks.qmp`
package airhacks.qmp;
