# 🛒 ALDI eshop

Backend for the ALDI eshop, built with Quarkus and MicroProfile on the BCE architecture pattern. Each shop capability (catalog, cart, checkout, ...) is a business component with boundary-control-entity separation, exposed via JAX-RS and verified by system tests in a standalone module. Dependencies are limited to Java SE APIs and MicroProfile standards.

BCE-structured 👉 [bce.design](https://bce.design) | AI-assisted with 👉 [airails.dev](https://airails.dev)

<!-- sbce:generated:start — projection of the specs; do not edit; `apply` regenerates from the per-BC package docs -->
## Capabilities

- **catalog** — let a customer browse the products on offer · [`spec`](service/src/main/java/airhacks/eshop/catalog/package-info.java)
<!-- sbce:generated:end -->

## Modules

- [service](service/README.md) - Quarkus application with the eshop business components
- [service-st](service-st/README.md) - System tests running against the deployed service

## Conventions

- Money is always integer cents.
- Product data is seeded in-memory; no persistence yet.

## Getting Started

See [AGENTS.md](AGENTS.md#build--test) for build, dev mode, and system test instructions.

## [/sbce](https://sbce.space) Quickstart

Shop capabilities are added spec-first with 👉 [sbce.space](https://sbce.space): one capability spec ≡ one business component. The spec lives in the BC's `package-info.java` and is the boundary contract; a green test run is the only definition of done. The `/sbce` skill and its companions are installed from 👉 [airails.dev](https://airails.dev).

```
/sbce new "let a customer check out a cart"         # intent-level (PM/BA or dev): proposes the BC carving, confirm first
/sbce new checkout                                  # structure-level (dev): the BC is already decided — authors the spec, scaffolds boundary/control/entity
/sbce apply checkout                                # converge: close the spec-vs-code gap until the test loop is green
```

- `new` writes the spec (boundary ops + [EARS](https://alistairmavin.com/ears/) requirements) — no business code yet.
- `apply` is idempotent: each boundary op becomes a boundary method, each requirement id (`R1.1`, …) a traceable test; code drift without a spec counterpart is surfaced, never absorbed.
