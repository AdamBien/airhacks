# Candidate Concepts — Java Pet Store 1.1.2

Step 3 of `migration/PLAN.md` (concept extraction), 2026-09-09. These are
**candidates, not verdicts** — every concept carries its evidence so the
domain expert (step 4, clarifier session) can confirm, rename, merge, or kill it.

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
- **For clarifier:** is sign-on truly separate from Customer Account, or one concept the
  J2EE security model forced apart?

### 2. Customer Account
- **Vocabulary:** account, customer, contact information, address, credit card,
  create account, update account.
- **Evidence:** `customer` component (`Account`, `Customer`, `ContactInformation`,
  `Address`, `CreditCard`); `AccountEvent`; `AccountHandler`/`AccountFlowHandler`;
  `createnewaccount.jsp`, `editaccount.jsp`, `duplicateaccount.jsp`, `changeaddressform.jsp`.
- **Caveat:** the `customer` component also contains all of **Order** (below) — Sun's
  component boundary is packaging, not domain. Strong carve signal.
- **For clarifier:** does "duplicate account" (its own JSP) imply account-id uniqueness
  rules worth a requirement?

### 3. Catalog
- **Vocabulary:** category, product, item, search, browse, product details,
  list chunk (pagination).
- **Evidence:** `Catalog`, `Category`, `Product`, `Item`, `ListChunk`, `CatalogDAO` —
  all **misfiled inside the `shoppingcart` component**; `CatalogHandler`, `ListHandler`;
  `search.jsp`, `product.jsp`, `productcategory.jsp`, `productdetails.jsp`.
- **Status:** already carved — maps to the existing catalog BC in `../../aldi-eshop`
  ("initial catalog BC creation" commit). Verify the Category→Product→Item hierarchy
  and pagination made it across.

### 4. Shopping Cart
- **Vocabulary:** cart, cart item, quantity; intents ADD_ITEM / DELETE_ITEM / UPDATE_ITEM
  (verbatim constants in `CartEvent`).
- **Evidence:** `ShoppingCart`, `CartItem`, `ShoppingCartModel`; `CartHandler`;
  `cart.jsp`, `carttable.jsp`.
- **For clarifier:** cart lifetime (session-only vs. persisted per account?) — the
  stateful session bean suggests session-only; needs behavioral confirmation (step 2).

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
- **For clarifier:** full status lifecycle (only `pending` is visible in code —
  what are the terminal states?); what "express order" changes vs. normal checkout;
  is UPDATE/DELETE_ORDER reachable by customers or dead code?

### 6. Order Fulfillment (back office)
- **Vocabulary:** pending orders, manage orders, approve/deny (implied by
  `findOrdersByStatus` + pending queue), order report export.
- **Evidence:** `petstoreadmin`: `ManageOrdersBean.getPendingOrders`,
  `PendingOrders` (orderId, userId, itemId, itemQty, orderDate, orderAmount)
  exporting via StarOffice/UNO (`ExcelXML.xls` in repo root); `pendingorders.jsp`,
  `manageorders.jsp`.
- **For clarifier:** what an approval actually did (charge card? release shipment?);
  whether the spreadsheet export is a behavior to keep or a 2001 artifact to drop. ⚑
- **Carve note:** likely a separate BC (different actor: admin, not shopper).

### 7. Inventory
- **Vocabulary:** item id, quantity on hand.
- **Evidence:** `inventory` component — `InventoryModel` is exactly
  `{itemId, quantity}`; 14 files for two fields (pattern-noise poster child).
- **For clarifier:** when is stock decremented — at order creation or at
  fulfillment approval? (Determines whether Inventory belongs inside
  Fulfillment or stands alone.)

### 8. Personalization / Profile
- **Vocabulary:** profile, language preference, favorite category,
  "my list" opt-in, banner opt-in.
- **Evidence:** `personalization` component — `ExplicitInformation`
  (`langPref`, `favCategory`, `myListOpt`, `bannerOpt`); `ProfileMgr*` (17 files
  of DAO/EJB noise around those four fields); `mylist.jsp`, `preferencesform.jsp`,
  `banner.jsp`, taglibs `banner`/`list`.
- **For clarifier:** keep as own BC or fold the four preferences into
  Customer Account? ("ProfileMgr" is a name to kill either way.)

### 9. Order Notification
- **Vocabulary:** e-mail message, order confirmation mail.
- **Evidence:** `mail` component (`EMailMessage`, `Mailer`); `MailAction` — "builds
  content for an order and sends an email to the customer"; `mailerapp.ear` miniapp.
- **Carve note:** classic downstream reaction to an order event — candidate for an
  async integration, not a standalone BC.

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

## Cross-component observations for the carver (step 5)

- **Sun components ≠ BCs, proven twice:** Catalog lives inside `shoppingcart`;
  Order lives inside `customer`. Do not carve along the folder lines.
- **Actor split:** shopper (signon, account, catalog, cart, order), back office
  (fulfillment, inventory?), system (notification). A reasonable first-cut BC axis.
- **Event names are the best vocabulary in the codebase** — six events name intents
  more honestly than any of the 48 pattern-named classes.
