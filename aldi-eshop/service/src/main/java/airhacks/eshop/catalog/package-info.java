/// # Catalog
/// > Let a customer browse the products on offer.
///
/// ## Boundary
/// - `list-products` — all products, optionally filtered by category
/// - `find-product` — a single product by its id
///
/// ## Requirements
///
/// ### R1: List products
/// - R1.1 — When products are listed without a category, the BC shall return every product.
/// - R1.2 — When products are listed with a category, the BC shall return only the products of that category.
/// - R1.3 — If products are listed with a category no product belongs to, then the BC shall return an empty listing.
/// - R1.4 — The BC shall include unavailable products in listings, flagged as unavailable. _(why: the shop UI decides how to present sold-out items)_
///
/// ### R2: Find a product
/// - R2.1 — When a product is requested by the id of an offered product, the BC shall return that product.
/// - R2.2 — If a product is requested by an unknown id, then the BC shall reject the request.
///
/// ## Entities
/// - Product
///
/// ## Decisions
/// - D1 — A product carries id, trackingTag, name, price in cents, color in RGB, category, and an availability flag. _(why: minimal browsing contract; rejected: description, image URL, stock quantity — add when a consumer needs them)_
/// - D2 — A product additionally carries an image reference. _(why: the storefront needs product visuals; lifts image URL from D1's rejected list — description and stock quantity remain out)_
///
/// ## Out of scope
/// - product management (create, update, delete) — product data is seeded/managed elsewhere
/// - stock levels and pricing rules
/// - free-text search
/// - cart and checkout
package airhacks.eshop.catalog;
