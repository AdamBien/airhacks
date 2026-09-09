# 🚀Quarkus with MicroProfile on BCE

Quarkus MicroProfile template with BCE architecture pattern. Features boundary-control-entity separation, System Tests in a standalone module, REST endpoints with JAX-RS, CDI for dependency injection, and MicroProfile-only dependencies. A starting point for Quarkus projects with minimal dependencies.

BCE-structured 👉 [bce.design](https://bce.design) | AI-assisted with 👉 [airails.dev](https://airails.dev)

## Getting Started

See [AGENTS.md](AGENTS.md#build--test) for build, dev mode, and system test instructions.

## Modules

- [service](service/README.md) - Quarkus application module with BCE structure
- [service-st](service-st/README.md) - System tests for the service module

## [/sbce](https://sbce.space) Quickstart

Spec-driven BCE 👉 [sbce.space](https://sbce.space): one capability spec ≡ one business component. The spec lives in the BC's `package-info.java` and is the boundary contract; a green test run is the only definition of done. The `/sbce` skill and its companions are installed from 👉 [airails.dev](https://airails.dev).

```
/sbce new "let a customer check out a cart"         # intent-level (PM/BA or dev): proposes the BC carving, confirm first
/sbce new checkout                                  # structure-level (dev): the BC is already decided — authors the spec, scaffolds boundary/control/entity
/sbce apply checkout                                # converge: close the spec-vs-code gap until the test loop is green
```

- `new` writes the spec (boundary ops + [EARS](https://alistairmavin.com/ears/) requirements) — no business code yet.
- `apply` is idempotent: each boundary op becomes a boundary method, each requirement id (`R1.1`, …) a traceable test; code drift without a spec counterpart is surfaced, never absorbed.
