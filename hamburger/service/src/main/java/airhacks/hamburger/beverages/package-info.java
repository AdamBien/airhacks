/// # Beverages
/// > Expose the fixed set of beverages the shop sells, each with calories and a price in cents, so an order can resolve and price them.
///
/// ## Boundary
/// - `list-beverages` — return every beverage on offer
/// - `find-beverage` — resolve one beverage by name
///
/// ## Requirements
/// ### R1: List beverages
/// - R1.1 — The BC shall return every beverage with its name, calories, and price in cents, in the shop's fixed order.
/// - R1.2 — The BC shall return at least one beverage. _(why: the list is fixed at startup; an empty list makes beverages unorderable)_
///
/// ### R2: Find a beverage
/// - R2.1 — When an existing beverage name is requested, the BC shall return that beverage with its calories and price in cents.
/// - R2.2 — If an unknown beverage name is requested, then the BC shall report that no such beverage exists.
/// - R2.3 — If a blank name is requested, then the BC shall reject the request.
/// - R2.4 — When the same name is requested again, the BC shall return the same beverage. _(why: read-only; nothing changes between requests)_
///
/// ## Entities
/// - Beverage
///
/// ## Decisions
/// - D1 — One fixed size per beverage. _(why: keeps a line to name plus quantity, like a hamburger; rejected: sizes with calories and price per size)_
///
/// ## Out of scope
/// - Adding, removing, or repricing beverages; the list is fixed.
/// - Sold-out or availability state.
/// - Resolving and pricing beverage lines inside an order; that is `ordering`.
/// - A beverage-specific quantity bound; `ordering`'s bound per line applies unchanged.
package airhacks.hamburger.beverages;
