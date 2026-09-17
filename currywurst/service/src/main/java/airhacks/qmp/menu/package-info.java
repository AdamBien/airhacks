/// # Menu
/// > Publish the stand's fixed catalog of sellable items and their prices.
///
/// ## Boundary
/// - `list-menu` — return every sellable item with its price
/// - `get-item` — return one sellable item by its name
///
/// ## Requirements
/// ### R1: List the menu
/// - R1.1 — The BC shall provide a catalog containing at least the items Currywurst mit Darm, Currywurst ohne Darm, Pommes, Currywurst mit Pommes, Cola, and Wasser, each with a positive price in cents.
/// - R1.2 — When the menu is listed, the BC shall return every catalog item.
/// - R1.3 — The BC shall return an identical catalog on every listing. _(why: the stand changes its menu offline, never at the counter — D1)_
///
/// ### R2: Look up an item
/// - R2.1 — When an item is requested by a name in the catalog, the BC shall return that item with its price.
/// - R2.2 — If the requested name is not in the catalog, then the BC shall report the item as unknown.
///
/// ## Entities
/// - MenuItem
///
/// ## Out of scope
/// - Editing the catalog at runtime (adding, removing, or repricing items).
/// - Availability, stock, or sold-out tracking.
/// - Composing or pricing orders — owned by `ordering`.
package airhacks.qmp.menu;
