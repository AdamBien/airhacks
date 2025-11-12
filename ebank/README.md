# ebank

A banking API implemented with MicroProfile, powered with Quarkus, demonstrating Java 21+ features. The application provides account management and transaction processing capabilities with a focus on simplicity, observability, and testability.


## Architecture Philosophy

The application follows the [Boundary-Control-Entity (BCE/ECB) pattern](https://bce.design) pattern to maintain clear separation of concerns. 

## Development Setup

### PostgreSQL Database

Use the provided script for quick database setup:
```bash
./runDB.sh
```

Or manually:
```bash
docker pull postgres
docker run --rm --name ebank-postgres -e POSTGRES_USER=ebank -e POSTGRES_DB=ebankdb -e POSTGRES_PASSWORD=ebanksecret -p 5432:5432 -d postgres
```

### Application Start
```bash
cd ebank
mvn quarkus:dev
```

### System Testing
```bash
cd ebank-st
mvn failsafe:integration-test
```

## Technology Stack

- **Quarkus**: Supersonic subatomic Java framework optimized for cloud deployments
- **PostgreSQL**: Popular relational database for transaction consistency
- **Jakarta Persistence (JPA)**: Standard ORM for domain object mapping
- **Jakarta REST**: RESTful web services following industry standards
- **MicroProfile Health**: Production-ready health check endpoints
- **MicroProfile Metrics**: Application performance monitoring

## Conventions

This project demonstrates several Java and architectural conventions:

### Architecture & Design
- **BCE/ECB Pattern**: [Boundary-Control-Entity pattern](https://bce.design) for clear separation of concerns
- **Package by Feature**: Components organized by business domain (accounting, bonus, reporting, logging)
- **Domain-Driven Package Naming**: Packages named after their responsibilities, not technical layers
- **Custom Stereotype Annotations**: `@Boundary` annotation combining `@ApplicationScoped` and `@Transactional`

### Code Organization
- **Package-info Files**: Documentation at package level describing domain responsibilities
- **Package-Private Visibility**: Preferred over private fields
- **Meaningful Names**: Classes named after responsibilities, avoiding generic suffixes (*Impl, *Service, *Manager)

### Java 21+ Features
- **Records**: Immutable data carriers (`AccountCreationResult`, `BonusResult`)
- **Sealed Interfaces**: `Transaction` interface with controlled implementations
- **Pattern Matching**: Enhanced switch expressions for type-safe handling
- **var Keyword**: Local variable type inference for cleaner code


### REST API Design
- **JAX-RS Resources**: REST endpoints with HTTP verbs
- **Response Builders**: Centralized response creation with status codes
- **OpenAPI Annotations**: Schema definitions for API documentation
- **Metrics Integration**: `@Timed` annotations for performance monitoring

### Persistence & Data Access
- **JPA Entities**: Simple entities with public fields
- **EntityManager Usage**: Direct use via `@PersistenceContext`
- **JDBC for Reporting**: Direct JDBC usage in reporting component for optimized read operations
- **Optional Pattern**: Consistent use of `Optional` for nullable returns
- **Factory Methods**: Static factory methods in entities

### Dependency Injection
- **CDI Integration**: Jakarta CDI with `@Inject` for dependencies
- **Custom Logger**: Centralized `EBLog` component for logging

### Functional Programming
- **Stream API**: Preference for streams over traditional loops
- **Method References**: Used instead of verbose lambda expressions
- **Immutable Results**: Result types as records

### Testing
- **AssertJ Library**: Fluent assertions instead of JUnit assertions
- **Essential Tests Only**: Avoiding repetitive tests, focusing on core functionality
- **Integration Tests**: Suffix with "IT" for Failsafe plugin execution

### Code Quality Principles
- **KISS**: Keep It Simple - simplest possible solutions
- **YAGNI**: You Aren't Gonna Need It - no over-engineering
- **High Cohesion**: Classes with single, well-defined responsibilities
- **Low Coupling**: Minimal dependencies between components
