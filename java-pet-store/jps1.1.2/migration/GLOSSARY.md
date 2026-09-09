# Glossary: Java Pet Store 1.1.2 Migration

Canonical terms, one line each, in domain language. Derived 2026-09-09 from
`concepts.md` (extraction + expert clarifier session) — no new mining. Ground
truth for `CARVING.md` and all `/sbce` specs; evidence trails live in `concepts.md`.

## Terms

- **customer** — a person shopping in the store; identified by the external IdP subject.
- **account** — a customer's stored contact information, addresses, and credit card.
- **address** — a postal address used as ship-to or bill-to.
- **credit card** — a customer's stored payment card.
- **category** — a top-level group of products (e.g. a pet family).
- **product** — a kind of pet within a category.
- **item** — a purchasable variant of a product, with a price.
- **list chunk** — one page of a paginated result list.
- **cart** — a customer's persisted collection of cart items; survives sessions and devices.
- **cart item** — an item in a cart with a quantity.
- **order** — a placed purchase: line items, ship-to/bill-to, carrier, total price, status.
- **line item** — an ordered item with quantity and price at time of order.
- **checkout** — the flow that turns a cart into an order.
- **express order** — checkout variant using the account's stored address and card.
- **order status** — `pending → shipped`; no other states.
- **order-placed event** — published when an order is created; consumed by the notification adapter.
- **carrier** — the shipping company chosen for an order.
- **pending orders** — the back-office queue of orders awaiting shipment.
- **order report export** — spreadsheet download (CSV/Excel) of pending orders.
- **quantity on hand** — stock level of an item.
- **reservation** — stock claimed for an order at placement time (new term; release semantics open, Q2).
- **profile** — a customer's shopping preferences.
- **language preference** — the customer's chosen language.
- **favorite category** — the customer's preferred category (free string vs. reference open, Q3).
- **my-list** — opt-in list of favorite items shown while shopping.
- **banner** — opt-in informational messages based on favorite pets.
- **order confirmation mail** — email sent to the customer after order placement.

## Decisions

Resolved by the domain expert (clarifier session, 2026-09-09). Stay resolved on re-runs.

1. **Sign-on leaves the domain** — external IdP owns identity and credentials; no sign-on term, no sign-on BC.
2. **Order lifecycle is `pending → shipped`** — the legacy admin approve/deny UI was decorative; no approval terms.
3. **Cart is persisted per customer** — deliberate upgrade; legacy session-only behavior is not the spec.
4. **Stock is reserved at order creation** — inventory is standalone with reservation semantics.
5. **Personalization is its own component** — not folded into account; "ProfileMgr" the name is dead.
6. **Pending-orders view and report export both stay** — the export is business behavior; its StarOffice implementation is not.
7. **Express order stays** — an order variant, a requirement in the orders spec.
8. **Notification is an async adapter** — reacts to the order-placed event; not a component.
9. **Localization is cross-cutting** — a concern, never a component.
10. **aldi-eshop is out of scope** (directive 2026-09-09) — every component, catalog included, is carved fresh from this vocabulary.

## Open

- Q1 — customer-facing order update/delete: reachable or dead code? (characterization)
- Q2 — reservation release for never-shipped orders? (clarifier)
- Q3 — favorite category: free string or catalog reference? (clarifier)
