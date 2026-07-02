/// # Swag
/// > Accept and confirm one swag claim per airhacks.live attendee.
///
/// ## Boundary
/// - `claim-swag` — submit a swag claim (t-shirt, socks, shipping address)
///
/// ## Requirements
/// ### R1: Claim swag
/// - R1.1 — When an attendee submits a claim with an email, a t-shirt size, a socks size, and a complete shipping address, the BC shall record and confirm the claim.
/// - R1.2 — If the email, the t-shirt size, the socks size, or any required address field is missing, then the BC shall reject the claim.
/// - R1.3 — If the email is malformed, then the BC shall reject the claim.
/// - R1.4 — If the t-shirt size or the socks size is not an accepted value, then the BC shall reject the claim.
/// - R1.5 — If the claimed t-shirt size or socks size is out of stock, then the BC shall reject the claim. _(why: a claim that cannot be fulfilled must never be confirmed)_
///
/// ### R2: One claim per attendee
/// - R2.1 — If a claim is submitted for an email that already has a claim, then the BC shall reject the duplicate. _(why: one swag pack per attendee)_
///
/// ## Entities
/// - Claim — an attendee's swag request, identified by email.
/// - ShippingAddress — first name, last name, street, postal code, city, country (all required).
///
/// ## Out of scope
/// - attendee eligibility verification
/// - payment or cost
/// - inventory and stock limits
/// - shipping and fulfilment tracking
/// - editing or cancelling a submitted claim
package airhacks.qmp.swag;
