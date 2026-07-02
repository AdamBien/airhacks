# Implementation Plan: World Clock for Business Hubs

**Branch**: `001-world-clock` | **Date**: 2026-07-02 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/001-world-clock/spec.md`

## Summary

A zero-dependency Java 25 CLI that prints the current time across a curated, fixed set of major business hubs at a single shared instant. The reference timezone defaults to the machine's `ZoneId.systemDefault()` and can be overridden by a single command-line argument (a standard zone id such as `Asia/Tokyo`). Output is a human-readable table on stdout; an invalid zone id produces a clear stderr message and a non-zero exit. Implemented with `java.time` only (no external dependencies), organized as BCE business components, built and packaged with `zb`, and tested with `zunit`.

## Technical Context

**Language/Version**: Java 25

**Primary Dependencies**: None (JDK only — `java.time`: `ZoneId`, `ZonedDateTime`, `Instant`, `DateTimeFormatter`). Built with [zb](https://github.com/AdamBien/zb).

**Storage**: N/A (stateless; hub list is a compile-time constant)

**Testing**: zunit (zero-dependency test runner for java-cli-app)

**Target Platform**: Any JVM (Java 25+) on macOS/Linux/Windows; executable JAR `zbo/app.jar`

**Project Type**: Single-project CLI application (BCE-structured)

**Performance Goals**: Instant snapshot — completes and exits in well under 1s; single pass over the hub list

**Constraints**: Zero runtime dependencies; text in (args) → text out (stdout), errors → stderr with non-zero exit; single shared reference `Instant` for all hubs

**Scale/Scope**: ~7 curated hubs; one optional CLI argument; single-shot invocation (no live refresh)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The project constitution (`.specify/memory/constitution.md`) is an unpopulated template with placeholder principles only — no concrete, enforceable gates are defined. No violations possible. The design nevertheless honors the widely-applicable principles the template hints at:

- **CLI interface / text I/O**: args → stdout, errors → stderr, non-zero exit on failure. ✅
- **Simplicity / YAGNI**: no dependencies, no persistence, no configuration; fixed hub list. ✅
- **Test-first**: zunit tests derived from spec acceptance scenarios before implementation. ✅

**Result**: PASS (no unjustified complexity; Complexity Tracking section omitted).

## Project Structure

### Documentation (this feature)

```text
specs/001-world-clock/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/
│   └── cli.md           # CLI contract (args, output, exit codes)
└── tasks.md             # Phase 2 output (/speckit-tasks — NOT created here)
```

### Source Code (repository root)

BCE (Boundary-Control-Entity) layout. Two business components under the `airhacks` package, plus the CLI boundary.

```text
src/main/java/airhacks/
├── App.java                       # CLI boundary: parse args, orchestrate, print, exit codes
├── clock/
│   ├── boundary/
│   │   └── WorldClock.java         # renders hub readings for a reference instant + zone
│   ├── control/
│   │   └── Hubs.java               # curated hub list + ordering (by UTC offset)
│   └── entity/
│       ├── Hub.java                # record: label + ZoneId
│       └── Reading.java            # record: hub label, local time, offset, isReference
└── zones/
    └── control/
        └── ReferenceZone.java      # resolve ZoneId from optional arg, else systemDefault()

src/test/java/airhacks/
├── clock/
│   ├── HubsTest.java               # curated list present, stable ordering
│   └── WorldClockTest.java         # shared instant, DST offsets, reference marking
└── zones/
    └── ReferenceZoneTest.java      # default vs override, invalid-zone rejection
```

**Structure Decision**: Single-project BCE CLI. The `clock` BC owns "display current time across hubs" (entities `Hub`/`Reading`, control `Hubs`, boundary `WorldClock`). The `zones` BC owns "resolve the reference timezone" (`ReferenceZone`). `App` is the thin CLI boundary that wires argument → `ReferenceZone` → `WorldClock` → stdout, translating failures into stderr + non-zero exit. This maps 1:1 to the README capabilities `display-hubs` and `resolve-zone`.

## Complexity Tracking

No constitution violations; section intentionally empty.
