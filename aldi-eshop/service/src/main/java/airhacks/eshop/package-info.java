/// # ALDI eshop
/// > Let a customer buy ALDI products online.
///
/// ## Vision
/// - Make the weekly ALDI shop a five-minute affair.
///
/// ## Components
/// - `catalog` depends on no other BC.
/// - Future BCs (`cart`, `checkout`, ...) may reference `catalog` products by id; never the reverse.
/// - `greetings` (template demo) and `health` (platform probes) carry no domain wiring.
///
/// ## System invariants
/// - S1 — When a catalog read is served, the system shall include caching metadata in the response. _(why: graduated from the README NFR "catalog responses are cacheable"; the remaining NFR targets stay declared in the README until they become testable)_
/// - S2 — When a catalog read is served, the system shall respond within 200 milliseconds. _(why: the server-side share of the ~2.5 s customer-perceived page budget; the loop verifies per request — the p99 across the fleet is tracked via OTEL in production)_
///
/// ## Ubiquitous language
/// - Product — an item ALDI offers for sale. Owned by `catalog`.
///
/// ## Stack
/// - microprofile-server (Quarkus) · base package `airhacks.eshop` · modules `service` + `service-st`
package airhacks.eshop;
