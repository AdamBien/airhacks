# 🚀 airhacks.live Attendees Management

Attendees management application for [airhacks.live](https://airhacks.live) workshops, built with Quarkus and MicroProfile on the BCE (Boundary-Control-Entity) architecture. Features boundary-control-entity separation, System Tests in a standalone module, REST endpoints with JAX-RS, CDI for dependency injection, and MicroProfile-only dependencies — a minimal-dependency foundation for managing workshop attendees.

BCE-structured 👉 [bce.design](https://bce.design) | AI-assisted with 👉 [airails.dev](https://airails.dev)

## Getting Started

See [AGENTS.md](AGENTS.md#build--test) for build, dev mode, and system test instructions.

## Modules

- [service](service/README.md) - Quarkus application module with BCE structure
- [service-st](service-st/README.md) - System tests for the service module
