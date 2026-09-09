# Carving: Java Pet Store 1.1.2

Step 3 of `migration/PLAN.md`, 2026-09-09. Inputs: `migration/GLOSSARY.md`
(canonical terms + decisions, ground truth) and `migration/concepts.md`
(evidence trails). No new concepts were mined here. Candidates, not verdicts:
a human confirms this cut before `/sbce` consumes it, one BC at a time.

BC names are single lowercase tokens (the future sbce BC names). Renames from the
concepts table: customer-account → `customers`, shopping-cart → `cart`,
order → `orders`. Target: each BC becomes package `airhacks.qmp.<bc>` in
`../../modern-jps/service` (see PLAN.md "Migration target").

## Components

### catalog
- **Responsibility:** Browse and search the pet catalog through its category → product → item hierarchy, in paginated lists.
- **Concepts:** category, product, item, search, list chunk (pagination)
- **Uses:** —

### customers
- **Responsibility:** Own a customer's contact information, addresses, and payment data (identity comes from the external IdP).
- **Concepts:** customer, account, contact information, address, credit card
- **Uses:** —

### cart
- **Responsibility:** Keep each customer's persisted cart of items and quantities.
- **Concepts:** cart, cart item, quantity; intents add/remove/update item
- **Uses:** catalog

### orders
- **Responsibility:** Own the order lifecycle from checkout to shipped (`pending → shipped`), publishing an order-placed event.
- **Concepts:** order, line item, checkout, express order, ship-to/bill-to, carrier, status, total price, order-placed event
- **Uses:** cart, customers, inventory

### inventory
- **Responsibility:** Reserve item stock when orders are placed.
- **Concepts:** item id, quantity on hand, reservation (new vocabulary — see Q2)
- **Uses:** —

### fulfillment
- **Responsibility:** Ship pending orders — the back-office queue, its view, and its export.
- **Concepts:** pending orders, ship, order report export
- **Uses:** orders

### personalization
- **Responsibility:** Own a customer's shopping preferences (language, favorite category, my-list, banner).
- **Concepts:** profile, language preference, favorite category, my-list opt-in, banner opt-in
- **Uses:** —

Dependency graph is acyclic: `cart → catalog`; `orders → cart, customers, inventory`;
`fulfillment → orders`. The notification adapter (not a BC) subscribes to the
order-placed event.

## Diff

All packages relative to `com.sun.j2ee.blueprints.`

| Current | Target BC | Layer |
|---|---|---|
| shoppingcart.catalog.model | catalog | entity |
| shoppingcart.catalog.dao | catalog | control |
| shoppingcart.catalog.ejb | catalog | — |
| shoppingcart.catalog.exceptions | catalog | — |
| shoppingcart.cart.model | cart | entity |
| shoppingcart.cart.ejb | cart | — |
| customer.account.model | customers | entity |
| customer.account.dao | customers | control |
| customer.account.ejb | customers | — |
| customer.account.exceptions | customers | — |
| customer.customer.ejb | customers | — |
| customer.customer.exceptions | customers | — |
| customer.util (Address, CreditCard, Calendar) | customers | entity |
| customer.order.model | orders | entity |
| customer.order.dao | orders | control |
| customer.order.ejb | orders | — |
| customer.order.exceptions | orders | — |
| inventory.model | inventory | entity |
| inventory.dao | inventory | control |
| inventory.ejb | inventory | — |
| inventory.exceptions | inventory | — |
| personalization.profilemgr.model | personalization | entity |
| personalization.profilemgr.dao | personalization | control |
| personalization.profilemgr.ejb | personalization | — |
| personalization.profilemgr.exceptions | personalization | — |
| petstoreadmin.control.* | fulfillment | — |
| petstore.control.ejb (MailAction) | orders (notification adapter) | — |
| mail.* , mailerapp | orders (notification adapter) | — |

Vocabulary evidence only, no code ports: `petstore.control.event` (the six intent
events inform the boundary contracts of cart, orders, customers).

## Unmapped

| Current | Reason |
|---|---|
| signon.* | Concept left the domain: external IdP replaces credential storage (clarifier decision). Ports to nothing. |
| petstore.control.web, .web.handlers, .control (EJB controller, events, exceptions) | J2EE MVC plumbing — replaced by BC boundaries; behavior, not code, carries over. |
| petstore.taglib.\*, petstore.util, \*/util (EJBUtil, JNDINames, DatabaseNames), util.tracer | Technical noise catalogued in concepts.md; no domain vocabulary. |
| customer.util (I18nUtil, UUIDGenerator) | I18nUtil → localization is cross-cutting, not a BC; UUIDGenerator → infrastructure. |
| tools.populate.* | DB seeding tool for the 2001 schema; new BCs seed their own test data. |
| petstoreadmin StarOffice/UNO classes (PendingOrders export plumbing) | Export behavior stays (fulfillment), the StarOffice implementation does not. |
| ann_\*.jsp, all JSPs, `ExcelXML.xls` | Tutorial pages and view artifacts; UI is out of carving scope. |

## Open Questions

Q-id space starts here (concepts.md predates the shared-id convention; its one
deferred unknown becomes Q1).

### Q1 — Is customer-facing order update/delete reachable, or dead code?
`OrderEvent` declares UPDATE_ORDER/DELETE_ORDER but no JSP/handler path was traced
to them. Decides whether the `orders` spec includes customer order mutation.
Resolve via characterization against the revived instance (plan step 5).

### Q2 — What releases an inventory reservation for an order that never ships?
Clarifier set reservation at order creation — vocabulary the legacy never had, so
no legacy behavior can answer this. Candidates: timeout, explicit cancellation
(depends on Q1), or reservations never release. Resolve via `/concept-clarifier`.

### Q3 — Is `favCategory` a free string or a reference into catalog categories?
`ExplicitInformation.favCategory` is an unconstrained String in the legacy. If it
must be a valid category, personalization gains a `uses: catalog` edge; if not,
personalization stays dependency-free. Resolve via `/concept-clarifier`.
