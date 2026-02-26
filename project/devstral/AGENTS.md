# AGENTS.md

## Project Overview

Quarkus MicroProfile template using Boundary-Control-Entity (BCE) architectural pattern. The project demonstrates clean separation of concerns with JAX-RS resources, MicroProfile Config, CDI, and Health checks.

## Architecture

### BCE Pattern

- **Boundary**: JAX-RS resources and health checks - coarse-grained components exposing functionality
- **Control**: Business logic and procedural code - stateless processing
- **Entity**: Domain objects, data classes, and entities

[BCE pattern]https://bce.design

### Package Structure

```
airhacks.[app-name].[component-name].[boundary|control|entity]
```

Example: `airhacks.qmp.greetings.boundary.GreetingResource`

### Modules

- `service/` - Main Quarkus application with BCE structure
- `service-st/` - System tests using MicroProfile REST Client

## Build & Test

### Development Mode

```bash
cd service
mvn quarkus:dev
```

### Build

```bash
mvn clean package
```

### Run System Tests

System tests require the service to be running:

```bash
# Terminal 1: Start service
cd service
mvn quarkus:dev

# Terminal 2: Run system tests
cd service-st
mvn verify
```

### Package

```bash
mvn package
```

## Code Style

### Java Version

Java 25 with modern syntax:
- Use `var` for local variables
- Pattern matching
- Text blocks for multiline strings
- Records for immutable data

## Security Considerations

- Avoid command injection, XSS, SQL injection
- Do not commit secrets (.env, credentials files)
- Use proper input validation in resources

## Dependencies

**IMPORTANT**: Always ask before adding new dependencies to `pom.xml`. This project minimizes external dependencies and relies on Java SE APIs and MicroProfile standards.