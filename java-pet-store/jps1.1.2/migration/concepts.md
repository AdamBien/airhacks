# Concepts — Java Pet Store 1.1.2

Steps 1–2 of `migration/PLAN.md`: extraction (2026-09-09) and clarifier session
with the domain expert (2026-09-09). Each concept carries its evidence plus the
**Clarified** decision from the expert session. This is now the vocabulary input
for `/bc-carver` (step 3).

## Sources mined

- **Events** (`petstore/control/event/`) — the closest thing to named business intents:
  `AccountEvent`, `CartEvent`, `OrderEvent`, `SigninEvent`, `SignoutEvent`, `LanguageChangeEvent`.
- **Handlers** (`control/web/handlers/`) — user-facing use cases behind the `*Handler` noise.
- **Component classes** (`src/components/*/`) — entities and value objects under the
  `EJB/Home/Model/DAO/Factory` boilerplate.
- **JSP names** — the user-visible vocabulary (`checkout.jsp`, `mylist.jsp`, `shiporder.jsp`, …).
- **Admin app** (`petstoreadmin`) — back-office vocabulary (`PendingOrders`, `ManageOrders`).

Not mined (out of scope here): runtime behavior (step 2), DB schema, `populate` seed data.

## Candidate concepts

### 1. Sign-On (identity)
- **Vocabulary:** user id, password, sign in, sign out.
- **Evidence:** `signon` component (`SignOn`, `SignOnEJB`); `SigninEvent`/`SignoutEvent`;
  `SigninFlowHandler`, `SignoutHandler`; `signin.jsp`, `signoff.jsp`, `signinsuccess.jsp`.
- **Noise stripped:** 18 files reduce to one concept with two intents.
- **Clarified: leaves the domain.** Credential storage is replaced by an external
  IdP (OIDC, Keycloak-style). No sign-on BC; the new system consumes identity,
  it does not own it. The `signon` component ports to nothing.

### 2. Customer Account
- **Vocabulary:** account, customer, contact information, address, credit card,
  create account, update account.
- **Evidence:** `customer` component (`Account`, `Customer`, `ContactInformation`,
  `Address`, `CreditCard`); `AccountEvent`; `AccountHandler`/`AccountFlowHandler`;
  `createnewaccount.jsp`, `editaccount.jsp`, `duplicateaccount.jsp`, `changeaddressform.jsp`.
- **Caveat:** the `customer` component also contains all of **Order** (below) — Sun's
  component boundary is packaging, not domain. Strong carve signal.
- **Clarified: stays, minus credentials.** Account identity comes from the external
  IdP subject; the BC owns contact information, addresses, and payment data.
  Duplicate-account handling becomes the IdP's problem, not a domain requirement.

### 3. Catalog
- **Vocabulary:** category, product, item, search, browse, product details,
  list chunk (pagination).
- **Evidence:** `Catalog`, `Category`, `Product`, `Item`, `ListChunk`, `CatalogDAO` —
  all **misfiled inside the `shoppingcart` component**; `CatalogHandler`, `ListHandler`;
  `search.jsp`, `product.jsp`, `productcategory.jsp`, `productdetails.jsp`.
- **Status:** to carve, like every other BC. The Category→Product→Item hierarchy
  and `ListChunk` pagination are the vocabulary to preserve.

### 4. Shopping Cart
- **Vocabulary:** cart, cart item, quantity; intents ADD_ITEM / DELETE_ITEM / UPDATE_ITEM
  (verbatim constants in `CartEvent`).
- **Evidence:** `ShoppingCart`, `CartItem`, `ShoppingCartModel`; `CartHandler`;
  `cart.jsp`, `carttable.jsp`.
- **Clarified: persisted per customer.** The cart survives sessions and devices,
  keyed by customer id — a deliberate upgrade over the legacy stateful session bean.
  Legacy behavior (session-only) is *not* the spec here; characterization of cart
  expiry is therefore not required.

### 5. Order & Checkout
- **Vocabulary:** order, line item, order id, order date, total price, status
  (at least `pending`), carrier, ship-to / bill-to (name + address each),
  charge card, express order, checkout, confirm shipping data.
- **Evidence:** `Order`, `OrderModel`, `LineItem`, `UUIDGenerator` in the `customer`
  component; `OrderEvent` (CREATE/DELETE/UPDATE_ORDER with shipping/billing/carrier/
  credit-card/locale payload); `OrderHandler`, `ExpressOrderHandler`,
  `ExtractBillingInformationHandler`, `ExtractShippingInformationHandler`,
  `ShippingFlowHandler`; `checkout.jsp`, `entershippingaddress.jsp`,
  `confirmshippingdata.jsp`, `shiporder.jsp`; multi-DB `OrderDAO` variants
  (CS/Oracle/Sybase — pure infrastructure noise).
- **Clarified:** status lifecycle is **`pending → shipped`** — no approve/deny step;
  the admin "approval" UI was decorative. **Express order stays** as an order variant:
  checkout using stored address/card, a requirement in the order spec. Order placement
  **publishes an event** (consumed by notification, see concept 9). UPDATE/DELETE_ORDER
  reachability still unknown — verify against the revived instance (plan step 5) before
  speccing customer-facing order mutation.

### 6. Order Fulfillment (back office)
- **Vocabulary:** pending orders, manage orders, approve/deny (implied by
  `findOrdersByStatus` + pending queue), order report export.
- **Evidence:** `petstoreadmin`: `ManageOrdersBean.getPendingOrders`,
  `PendingOrders` (orderId, userId, itemId, itemQty, orderDate, orderAmount)
  exporting via StarOffice/UNO (`ExcelXML.xls` in repo root); `pendingorders.jsp`,
  `manageorders.jsp`.
- **Clarified:** no approval semantics — fulfillment ships pending orders
  (`pending → shipped`). **Both the pending-orders view and the export stay**:
  the spreadsheet download is real business behavior, reimplemented cleanly
  (CSV/Excel) without the StarOffice/UNO plumbing.
- **Carve note:** separate BC confirmed (different actor: admin, not shopper).

### 7. Inventory
- **Vocabulary:** item id, quantity on hand.
- **Evidence:** `inventory` component — `InventoryModel` is exactly
  `{itemId, quantity}`; 14 files for two fields (pattern-noise poster child).
- **Clarified: standalone BC with reservation semantics.** Stock is reserved/
  decremented **at order creation**, not at fulfillment. This adds vocabulary the
  legacy never had (reservation, release on cancellation?) — the inventory spec
  must define what happens to reservations of orders that never ship.

### 8. Personalization / Profile
- **Vocabulary:** profile, language preference, favorite category,
  "my list" opt-in, banner opt-in.
- **Evidence:** `personalization` component — `ExplicitInformation`
  (`langPref`, `favCategory`, `myListOpt`, `bannerOpt`); `ProfileMgr*` (17 files
  of DAO/EJB noise around those four fields); `mylist.jsp`, `preferencesform.jsp`,
  `banner.jsp`, taglibs `banner`/`list`.
- **Clarified: own BC.** Personalization stays separate (room to grow into
  recommendations). "ProfileMgr" the name is dead; the concept lives.

### 9. Order Notification
- **Vocabulary:** e-mail message, order confirmation mail.
- **Evidence:** `mail` component (`EMailMessage`, `Mailer`); `MailAction` — "builds
  content for an order and sends an email to the customer"; `mailerapp.ear` miniapp.
- **Clarified: async adapter, not a BC.** The Order BC publishes an order-placed
  event; a thin notification adapter consumes it and sends the confirmation mail.
  No standalone mail BC; `mailerapp.ear` ports to nothing.

### 10. Localization (cross-cutting)
- **Vocabulary:** locale, language change.
- **Evidence:** `LanguageChangeEvent`, `LanguageHandler`, `I18nUtil`, `locale` fields
  threaded through `OrderEvent`/`OrderModel`/`getPendingOrders`, `cart_unicode.jsp`.
- **Carve note:** a concern, not a component — should not become a BC.

## Non-concepts (noise catalog — names to leave behind)

`*EJB`, `*Home`, `*DAO*` (+ per-vendor variants), `*Factory`, `*Model`/`Mutable*Model`,
`*Impl`, `EJBUtil`, `JNDINames`, `DatabaseNames`, `EStoreEventSupport`,
`RequestHandlerSupport`, `ModelManager`, `FlowHandler`, `ProfileMgr` (the "Mgr"),
the `ann_*.jsp` annotated-tutorial pages, `populate` (DB seeding tool),
StarOffice/UNO plumbing. Roughly 130 of 233 classes carry no domain vocabulary.

## Clarified BC candidates — input for `/bc-carver` (step 3)

| BC candidate | Status | Key clarified decision |
|---|---|---|
| catalog | to carve | Category→Product→Item hierarchy + pagination (`ListChunk`) |
| customer-account | to carve | identity from external IdP; owns contact/address/payment |
| shopping-cart | to carve | persisted per customer (deliberate upgrade over legacy) |
| order | to carve | `pending → shipped`; express variant; publishes order-placed event |
| fulfillment | to carve | admin actor; ships pending orders; keeps view + modern export |
| inventory | to carve | standalone; reservation at order creation (new vocabulary — spec must define release) |
| personalization | to carve | own BC; four preferences, room for recommendations |

**Not BCs:** sign-on (external IdP), notification (async adapter on the order-placed
event), localization (cross-cutting concern).

## Cross-component observations for the carver

- **Sun components ≠ BCs, proven twice:** Catalog lives inside `shoppingcart`;
  Order lives inside `customer`. Do not carve along the folder lines.
- **Actor split confirmed by clarification:** shopper (account, catalog, cart, order,
  personalization), back office (fulfillment, inventory), system (notification adapter).
- **Event names are the best vocabulary in the codebase** — six events name intents
  more honestly than any of the 48 pattern-named classes.

## Remaining unknown (deferred to characterization, plan step 5)

- Whether customer-facing UPDATE/DELETE_ORDER is reachable in the legacy UI or dead
  code — decides if the order spec includes customer order mutation.
