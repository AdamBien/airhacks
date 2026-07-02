/// # airhacks.live Swag
/// > Collect swag claims from airhacks.live attendees and fulfil them within available stock.
///
/// ## Components
/// - `swag` accepts a claim, then calls `inventory` `reserve-swag` to secure one t-shirt and
///   one pair of socks; a rejected reservation rejects the claim. `swag` never mutates stock
///   directly — `inventory` owns every stock change.
/// - `inventory` is called by `swag` and by organizers; it depends on no other component.
///
/// ## Ubiquitous language
/// - Claim — an attendee's request for one t-shirt and one pair of socks, keyed by email.
/// - Item — a kind of swag: t-shirt or socks.
/// - Size — the chosen size of an item (t-shirt XS–XXL, socks S/M/L).
/// - Stock — the remaining quantity of an item in a given size.
/// - Reservation — a decrement of stock that lets an accepted claim be fulfilled.
///
/// ## Stack
/// - microprofile-server (Quarkus, MicroProfile); package base `airhacks.qmp`.
///
/// ---
/// Note: "qmp" is a placeholder application name and should be renamed throughout the codebase
/// when starting a new project from this template.
package airhacks.qmp;
