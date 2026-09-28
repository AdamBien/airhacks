## Use case
- name: order beverages
- goal: a customer sees which beverages the shop sells and adds them to an order alongside, or instead of, hamburgers

## Capability
- name: `beverages`
- kind: new
- responsibility: expose the fixed set of beverages the shop sells, each with calories and a price in cents, so an order can resolve and price them

## Actors
- primary: a customer
- supporting: `ordering` — capability `ordering`; placing the order that contains the beverage lines and resolving each beverage line through this capability

## Trigger
the customer asks which beverages are on offer, or asks for one beverage by name

## Main scenario
1. `list-beverages` — the customer asks for the beverages on offer.
2. The system returns every beverage with its name, calories and price in cents; in the shop's fixed order; the list is never empty; no limit.
3. `find-beverage` — the customer, or the ordering capability on the customer's behalf, asks for one beverage by name.
4. The system returns that beverage with its name, calories and price in cents.

## Inputs
- beverage name: one name; required; invalid when blank

## Extensions
- 3a. the name is blank -> reject the request
- 3b. no beverage carries the name -> report that no such beverage exists
- *a. the same request is repeated -> the same answer; nothing changes

## Lifecycle
- none

## Entities
- Beverage

## Out of scope
- adding, removing or repricing beverages — the list is fixed
- sizes per beverage — one fixed size each
- sold-out or availability state
- resolving and pricing beverage lines inside an order, and the beverages-only order rule — the `ordering` extension, captured in the next story brief
- a beverage-specific quantity bound — `ordering`'s 1 to 10 per line applies unchanged

## Decisions
- record: beverages are their own capability, called by `ordering` _(rejected: extending `menu` with a kind per item)_
- record: item names are unique across `menu` and `beverages`, so an order line names an item only _(rejected: a kind on every order line; resolving the menu first and beverages only on a miss)_
- record: one fixed size per beverage _(rejected: sizes with calories and price per size)_
- record: an order may consist of beverages only _(rejected: at least one hamburger per order)_ — carried into the `ordering` story

## Open issues
- none
