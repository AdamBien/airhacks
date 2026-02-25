# Amazon Store — Multi-Seller Marketplace

A multi-seller marketplace where independent sellers run branded stores, customers shop across all stores in a single experience, and the platform earns a configurable commission on every sale.

---

## The Problem

Buyers want variety and competitive pricing in one place. Sellers want reach without the overhead of running their own storefront. Traditional single-vendor e-commerce solves neither. A marketplace solves both — but only if it handles the complexity of multiple sellers, split fulfilment, and transparent financials behind a seamless customer experience.

---

## Who It's For

| Role | Who They Are | What They Need |
|---|---|---|
| **Customer** | Registered buyer | Browse, search, cart across stores, single checkout, track per-seller shipment, leave verified reviews |
| **Seller** | Self-registered shop owner | Manage store branding, list products with variants, fulfil sub-orders, receive weekly payouts |
| **Platform Admin** | Marketplace operator | Set commission rates, suspend bad actors, monitor GMV and platform health |

---

## Core Concepts

### Products & Variants
Every product is a catalogue entry. The buyable unit is always a **variant** — a specific combination of seller-defined options such as Colour, Size, Material, or Storage. Each variant has its own SKU, price, stock level, and image gallery (up to 8 images). Products with no variant-specific images fall back to product-level images.

### Stock Management
Stock lives exclusively on variants, never on the product itself. When an order is confirmed, stock decrements atomically. If any variant in the cart has insufficient stock, the entire order is rejected — no partial orders. When a variant's stock falls to or below a configurable threshold (default: 5 units), the seller receives an automatic low-stock alert.

### Multi-Seller Checkout
A customer pays once for their entire cart, regardless of how many sellers it spans. Internally, the platform splits the order into one **sub-order** per seller. Each seller sees and fulfils only their own sub-orders. The customer sees a unified order with per-seller shipment status and tracking.

### Commission & Payouts
The platform earns a configurable commission rate per store (default: 10%). On each sub-order:

```
commission = subtotal × commission_rate / 100
seller_net  = subtotal − commission
```

Every Monday at 08:00 UTC, an automated job creates a payout record for each store with delivered, unpaid sub-orders and notifies the seller by email.

### Reviews
Only customers who have purchased a product can leave a review. Reviews roll up to a store rating and a product average rating.

---

## User-Facing Features

### Customer
- Register and log in; reset forgotten password securely
- Browse all stores and search products by keyword, price range, category, and sort order
- View product detail with full variant matrix (options, stock status, image gallery)
- Add specific variants to a multi-store cart; price locked at add-to-cart time
- Single checkout across all sellers; stock validated atomically at purchase
- View orders with per-seller shipment tracking
- Cancel orders before any seller ships (full refund)
- Leave verified-purchase reviews with star ratings
- AI-powered product recommendations and natural-language search chatbot *(stretch goal)*
- AI-generated review summaries *(stretch goal)*

### Seller
- Self-register store; goes live immediately with no approval queue
- Manage store profile: name, description, logo, and banner
- Create products with flexible variant dimensions and option values
- Upload and reorder multiple images per variant
- Manage inventory: update stock, price, and variant status
- View own sub-orders and mark them as shipped with a tracking number
- View commission breakdown per sub-order and weekly payout history
- Receive low-stock alerts and payout notification emails
- AI-powered product description generator and pricing suggestions *(stretch goal)*
- AI stock-out forecasting *(stretch goal)*

### Platform Admin
- Set commission rate per store
- Suspend or reinstate seller stores (with full audit trail)
- View marketplace-wide dashboard: GMV, commission earned, top stores, new customers
- AI-generated weekly marketplace health report *(stretch goal)*

---

## Business Rules

- A seller cannot see another seller's products, orders, or financials
- Stock quantity is never exposed to customers — only a stock status (`IN_STOCK`, `LOW_STOCK`, `OUT_OF_STOCK`) and a human-readable message
- Email and user enumeration are never possible through any API response
- Commission rate changes apply to future orders only; past sub-orders are never recalculated
- A store suspension deactivates all its products immediately; reinstatement restores only products that were active before suspension
- Payouts are weekly; no on-demand payout requests

---

## Product Backlog Summary

| # | Iteration | What Gets Built | Stories |
|---|---|---|---|
| 1 | Foundation | Project scaffold, schema migrations, health checks, API documentation | US01–US04 |
| 2 | Auth | Customer & seller registration, login, token refresh, forgot/reset password | US05–US10 |
| 3 | Stores | Store profile management, public storefront, admin commission & suspend | US11–US14 |
| 4 | Catalog + Images | Products, variants, option dimensions, image gallery, stock status, browse & search | US15–US22 |
| 5 | Cart | Add variant, view grouped by store, update/remove items | US23–US25 |
| 6 | Orders | Multi-seller checkout, atomic stock decrement, sub-order split, ship, track, cancel | US26–US30 |
| 7 | Payouts | Commission breakdown, weekly automated payout, payout history, low-stock alerts | US31–US34 |
| 8 | Dashboards | Seller performance dashboard, admin marketplace dashboard | US35–US36 |
| ★ | AI & Intelligence | Chatbot search, recommendations, description generator, pricing suggest, stock forecast, health report, review summary | US37–US43 |

**Workshop target:** Iterations 1–8 deliver a complete, production-shaped multi-seller marketplace. The AI iteration is a post-workshop stretch goal.

---

## Out of Scope

- Frontend / UI (backend API only)
- Real payment processing (payment gateway is a stub)
- Mobile push notifications
- Inventory forecasting beyond the AI stretch goal
- Multi-currency (GBP only for the workshop)
- Seller KYC / identity verification

---

*Prepared by Mohamed — Product Manager / Product Owner*
*Airhacks Workshop · February 26th, 2026*
