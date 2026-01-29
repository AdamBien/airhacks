# Technology Stack

## Runtime
- Java EE 7 (javax namespace)
- WAR packaging for application server deployment

## Build System
- Maven
- Java 8 source/target compatibility
- Final artifact: `tuica.war`

## Key Dependencies
- `javax:javaee-api:7.0` (provided scope)

## Frameworks & APIs
- JAX-RS for REST endpoints (base path: `/resources`)
- JPA 2.1 with JTA transactions
- CDI with bean-discovery-mode="all"
- EJB (Stateless session beans)
- Bean Validation
- JSON-P for JSON handling
- JAXB for XML serialization
- Managed Executor Service for async operations

## Common Commands
```bash
# Build
mvn clean package

# Run tests
mvn test

# Deploy (produces target/tuica.war)
mvn clean package
```

## Persistence
- JPA persistence unit: `prod`
- Schema generation: drop-and-create (development mode)
