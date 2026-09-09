/// # Pet Store
///
/// ## Charter
///
/// Sell pets online: customers browse a catalog, keep a cart, and place orders
/// that the back office ships.
///
/// ## Stack
///
/// `microprofile-server` (Quarkus, MicroProfile-only dependencies) — package base
/// `airhacks.petstore`, one business component per child package with
/// `boundary`/`control`/`entity` layers, system tests in `service-st`.
package airhacks.petstore;
