# AGENTS.md

## Project Overview

Quarkus MicroProfile template using Boundary-Control-Entity (BCE) architectural pattern. The project demonstrates clean separation of concerns with JAX-RS resources, MicroProfile Config, CDI, and Health checks.

## Architecture

Follows the [BCE pattern](https://bce.design).

### Package Structure

```
airhacks.[app-name].[component-name].[boundary|control|entity]
```

Example: `airhacks.contacts.contacts.boundary.ContactsResource`

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

## Dependencies

**IMPORTANT**: Always ask before adding new dependencies to `pom.xml`. This project minimizes external dependencies and relies on Java SE APIs and MicroProfile standards.