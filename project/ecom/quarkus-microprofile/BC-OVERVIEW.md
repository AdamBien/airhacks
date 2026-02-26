# Business Component Overview

This diagram shows the Business Components (BCs) in the ecommerce system and their relationships.

```mermaid
graph TB
    subgraph "Core BCs"
        Customers[Customers<br/>---<br/>Rule: age ≤ 90]
        Products[Products<br/>---<br/>Rule: price > 0]
        Discounts[Discounts<br/>---<br/>Rule: 0 ≤ percentage ≤ 100]
    end
    
    subgraph "Shopping BCs"
        ShoppingCarts[Shopping Carts<br/>---<br/>Per-customer carts with items]
        Orders[Orders<br/>---<br/>Order creation & retrieval]
    end
    
    subgraph "Fulfillment BCs"
        Payments[Payments<br/>---<br/>Payment processing]
        Shipments[Shipments<br/>---<br/>Rule: requires payment]
    end
    
    subgraph "Infrastructure BCs"
        Health[Health<br/>---<br/>Liveness & Readiness]
        Greetings[Greetings<br/>---<br/>Example/Demo BC]
    end
    
    %% Relationships
    ShoppingCarts -->|belongs to| Customers
    ShoppingCarts -->|contains| Products
    Orders -->|placed by| Customers
    Orders -->|references| Products
    Payments -->|for| Customers
    Shipments -->|for| Customers
    Shipments -->|fulfills| Orders
    Shipments -->|requires| Payments
    Discounts -.->|applies to| Products
    Discounts -.->|applies to| Orders
    
    classDef coreBC fill:#e1f5ff,stroke:#0288d1,stroke-width:2px
    classDef shoppingBC fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
    classDef fulfillmentBC fill:#e8f5e9,stroke:#388e3c,stroke-width:2px
    classDef infraBC fill:#fff3e0,stroke:#f57c00,stroke-width:2px
    
    class Customers,Products,Discounts coreBC
    class ShoppingCarts,Orders shoppingBC
    class Payments,Shipments fulfillmentBC
    class Health,Greetings infraBC
```

## Business Component Details

### Core BCs
- **Customers**: Customer management with age validation (max 90 years)
- **Products**: Product catalog with price validation (must be > 0)
- **Discounts**: Discount management with percentage validation (0-100%)

### Shopping BCs
- **Shopping Carts**: Per-customer shopping carts with line items
- **Orders**: Order creation and retrieval for customers

### Fulfillment BCs
- **Payments**: Payment processing associated with customers
- **Shipments**: Shipment management requiring payment validation

### Infrastructure BCs
- **Health**: Application health checks (liveness & readiness)
- **Greetings**: Example/demo business component

## Business Rules

Each BC enforces its own business rules through the `@Violation` annotation:

1. **Customers**: `customer age must not exceed 90`
2. **Products**: `product price must be greater than zero`
3. **Discounts**: `discount percentage must be between 0 and 100`
4. **Shipments**: `shipment requires existing payment for the customer`
