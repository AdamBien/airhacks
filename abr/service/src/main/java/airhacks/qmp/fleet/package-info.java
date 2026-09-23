/// # Fleet
/// > Own the bikes and e-bikes of the Amsterdam fleet and know which of them can be rented right now.
///
/// ## Boundary
/// - `register-bike` — add a bike or e-bike to the fleet
/// - `list-available-bikes` — list the bikes that can be rented right now, optionally by type
/// - `count-bikes` — count the in-service bikes, optionally by type (called by `reservation`)
/// - `withdraw-bike` — take a bike out of service for maintenance or damage
/// - `restore-bike` — bring a withdrawn bike back into service
/// - `allocate-bike` — hand an available bike of a type to a rental (called by `rental`)
/// - `release-bike` — take an allocated bike back from a rental (called by `rental`)
///
/// ## Requirements
/// ### R1: Register a bike
/// - R1.1 — When a bike of type bike or e-bike is registered, the BC shall add it to the fleet as available.
/// - R1.2 — If the type is neither bike nor e-bike, then the BC shall reject the registration.
/// - R1.3 — If a bike with the same identifier is already registered, then the BC shall reject the registration.
///
/// ### R2: List available bikes
/// - R2.1 — The BC shall list only bikes that are available, neither allocated nor withdrawn.
/// - R2.2 — Where a type is given, the BC shall list only available bikes of that type.
///
/// ### R3: Count bikes
/// - R3.1 — The BC shall count the in-service bikes, available or allocated, excluding withdrawn ones.
/// - R3.2 — Where a type is given, the BC shall count only in-service bikes of that type.
///
/// ### R4: Withdraw and restore a bike
/// - R4.1 — While a bike is available, when it is withdrawn, the BC shall mark it withdrawn.
/// - R4.2 — If a bike allocated to a rental is withdrawn, then the BC shall reject the withdrawal. _(why: a bike on the road cannot be pulled from under a customer)_
/// - R4.3 — While a bike is withdrawn, when it is restored, the BC shall mark it available.
/// - R4.4 — If an unknown bike is withdrawn or restored, then the BC shall reject the request.
///
/// ### R5: Allocate and release a bike
/// - R5.1 — While at least one bike of the requested type is available, when a bike is allocated, the BC shall mark one such bike allocated and hand it over.
/// - R5.2 — If no bike of the requested type is available, then the BC shall reject the allocation.
/// - R5.3 — While a bike is allocated, when it is released, the BC shall mark it available.
/// - R5.4 — If a bike that is not allocated is released, then the BC shall reject the release.
///
/// ## Entities
/// - Bike
///
/// ## Out of scope
/// - Who rents a bike, for how long, and at what price — owned by `rental`.
/// - Holding a bike type ahead of time — owned by `reservation`.
/// - Bike location, tracking, or telemetry.
package airhacks.qmp.fleet;
