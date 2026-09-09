/// # catalog
///
/// > Browse and search the pet catalog through its category → product → item
/// > hierarchy, in paginated lists.
///
/// ## Boundary
///
/// - `list-categories` — all categories of the store (R1)
/// - `list-products` — the products of one category, paginated (R2)
/// - `list-items` — the purchasable items of one product, paginated (R3)
/// - `get-item` — one item with its details and price (R4)
/// - `search-products` — keyword search over products, paginated (R5)
///
/// ## Requirements
///
/// ### R1 — list categories
///
/// - R1.1 When categories are requested, the catalog shall return all categories.
///
/// ### R2 — list products of a category
///
/// - R2.1 When the products of a category are requested, the catalog shall return
///   one page of that category's products together with the total product count.
///   _(why: legacy ListChunk pagination — lists were always chunked)_
/// - R2.2 If the category is unknown, then the catalog shall report it as not found.
///
/// ### R3 — list items of a product
///
/// - R3.1 When the items of a product are requested, the catalog shall return one
///   page of that product's items together with the total item count.
/// - R3.2 If the product is unknown, then the catalog shall report it as not found.
///
/// ### R4 — item details
///
/// - R4.1 When an item is requested by its id, the catalog shall return the item
///   with its product reference and list price.
/// - R4.2 If the item is unknown, then the catalog shall report it as not found.
///
/// ### R5 — search products
///
/// - R5.1 When a keyword search is requested, the catalog shall return one page of
///   the products whose name or description matches the keyword, together with the
///   total match count.
/// - R5.2 If no product matches the keyword, then the catalog shall return an empty
///   page with a total count of zero.
///
/// ## Entities
///
/// - **category** — id, name, description; top-level group of products.
/// - **product** — id, name, description; belongs to one category.
/// - **item** — id, name, list price; purchasable variant of one product.
/// - **page** — one chunk of a list: the elements plus the total count.
///
/// ## Decisions
///
/// - D1 — Read-only browsing. _(why: catalog content is seeded, never edited
///   in-app; rejected: in-app catalog maintenance)_
/// - D2 — Single-language content, no locale parameter. _(why: localization is
///   cross-cutting and may extend this spec later via a Where requirement;
///   rejected: locale-aware ops as in the legacy CatalogEJB)_
/// - D3 — Search covers product names and descriptions only. _(rejected:
///   item-level search)_
///
/// ## Out of scope
///
/// - Catalog maintenance (create/update of categories, products, items).
/// - Stock levels and reservations (inventory BC).
/// - Prices beyond the list price (discounts, promotions).
/// - Localized content (cross-cutting concern, not yet included).
package airhacks.petstore.catalog;
