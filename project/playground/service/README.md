# service

Quarkus application module structured with the BCE (Boundary-Control-Entity) pattern. Business components organize code by domain responsibilities, with boundary layers for external interaction (JAX-RS resources), control for procedural logic, and entity for domain objects.

**Note:** "qmp" is a placeholder for the application name and should be replaced throughout the codebase with your actual application name.

## Business Component Interaction

```mermaid
graph LR
    Customer(["Customer / Visitor"])
    Seller(["Seller"])

    subgraph Service
        Auth([Auth]) -->|issues JWT| JWT([JWT])
        Products([Products]) -->|verifies via| JWT
        Auth -.->|seller identity| Products
    end

    Customer -->|register / login| Auth
    Customer -->|browse products| Products
    Seller -->|register / login| Auth
    Seller -->|CRUD products| Products

    classDef bc fill:#dae8fc,stroke:#6c8ebf,color:#000
    classDef ext fill:#fff2cc,stroke:#d6b656,color:#000,stroke-dasharray:5 5
    class Auth,Products bc
    class JWT ext
    class Customer,Seller ext
```

## Build

```bash
mvn clean package
```

## Run

Development mode:

```bash
mvn quarkus:dev
```

Production build:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```
