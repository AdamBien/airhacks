/// # Menu
/// > Expose the fixed set of hamburgers the shop sells, each with calories and a price in cents.
///
/// ## Boundary
/// - `list-menu` — return every menu item
/// - `find-item` — resolve one menu item by name
///
/// ## Requirements
/// ### R1: List the menu
/// - R1.1 — The BC shall return every menu item with its name, calories, and price in cents.
/// - R1.2 — The BC shall return at least one menu item. _(why: an empty menu makes ordering impossible; the menu is fixed at startup)_
///
/// ### R2: Find an item
/// - R2.1 — When an existing item name is requested, the BC shall return that item with its calories and price in cents.
/// - R2.2 — If an unknown item name is requested, then the BC shall report that no such item exists.
///
/// ## Entities
/// - MenuItem
///
/// ## Out of scope
/// - Adding, removing, or repricing items; the menu is fixed.
/// - Availability or sold-out state.
package airhacks.hamburger.menu;
