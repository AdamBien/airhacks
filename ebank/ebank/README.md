# ebank

Quarkus-based banking application providing account management and transaction processing through RESTful APIs.

## Core Features

- Account creation with initial balance validation
- Debit and deposit operations
- Balance inquiries
- JDBC-based reporting for account data

## Technology Stack

- Quarkus 3.X
- Java 25
- PostgreSQL (JDBC)
- Hibernate ORM
- SmallRye Metrics & Health
- OpenAPI

## Development

Start the application in dev mode:

```bash
mvn quarkus:dev
```

The application exposes:
- REST API: http://localhost:8080/accounts
- Health checks: http://localhost:8080/q/health
- Metrics: http://localhost:8080/q/metrics
- OpenAPI: http://localhost:8080/q/swagger-ui

## Production Build

Create an optimized production build:

```bash
mvn clean package
```

Build a native executable:

```bash
mvn clean package -Pnative
```
