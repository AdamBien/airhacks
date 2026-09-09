# java-cli-app

## About

A zero-dependency Java 25 CLI application, built and packaged with [zb](https://github.com/AdamBien/zb). Cloned from the [bce.design](https://bce.design) / [airails.dev](https://airails.dev) `java-cli-app` template.

**Why it exists:** _<one or two sentences — the problem this CLI solves and for whom>_

**Aspiration** _(seeds the distilled `## Vision`)_: _<one aspirational sentence — the outcome this tool chases>_

**Intended capabilities** _(seed the BC carving — each becomes a business component)_:
- _<verb-noun>_ — _<the single job it owns>_
- _<verb-noun>_ — _<the single job it owns>_

## Conventions

- _<e.g. reads stdin, writes results to stdout, errors to stderr with a non-zero exit>_

## Prerequisites

Java 25+, [zb](https://github.com/AdamBien/zb)

## Build & run

```
zb
java -jar zbo/app.jar
```

## [/sbce](https://sbce.space) Quickstart

Spec-driven BCE 👉 [sbce.space](https://sbce.space): one capability spec ≡ one business component. The spec lives in the BC's `package-info.java` and is the boundary contract; a green test run is the only definition of done. The `/sbce` skill and its companions are installed from 👉 [airails.dev](https://airails.dev).

Example — a "time in business hubs" TZ utility:

```
/sbce new "show the current time in business hubs"  # intent-level (PM/BA or dev): proposes the BC carving, confirm first
/sbce new hubtime                                   # structure-level (dev): the BC is already decided — authors the spec, scaffolds boundary/control/entity
/sbce apply hubtime                                 # converge: close the spec-vs-code gap until the test loop is green
```

Bare `/sbce new` (no argument) bootstraps from the `## About` prose above — it is the inception seed a PM/BA can fill in.
