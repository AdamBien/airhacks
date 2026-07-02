# Phase 0 Research: World Clock for Business Hubs

No `NEEDS CLARIFICATION` markers remained after specification (defaults were captured in the spec's Assumptions). This document records the technical decisions behind the plan.

## Decision: Use `java.time` exclusively (no dependencies)

- **Decision**: Model everything with the JDK `java.time` API — `Instant.now()` for the shared reference instant, `ZoneId` for zones, `ZonedDateTime` for per-hub local times, `DateTimeFormatter` for rendering.
- **Rationale**: The project is a zero-dependency `zb` app. `java.time` is IANA-tz-database backed, DST-aware, and immutable/thread-safe — it covers every requirement (FR-004 shared instant, FR-009 correct DST offset) with no third-party code.
- **Alternatives considered**: Joda-Time (obsolete, superseded by `java.time`); manual offset arithmetic (rejected — cannot handle DST correctly, violates FR-009).

## Decision: Single shared `Instant` for all hubs

- **Decision**: Capture one `Instant.now()` at the start of a run and convert it into each hub's zone; never call "now" per hub.
- **Rationale**: Guarantees every displayed time reflects the same moment (FR-004, SC-002). Per-hub `now()` calls could straddle a second/minute boundary.
- **Alternatives considered**: Per-hub `ZonedDateTime.now(zone)` — rejected for the consistency risk above.

## Decision: Reference zone resolution (default vs. override)

- **Decision**: If exactly one CLI argument is present, treat it as an IANA zone id and resolve with `ZoneId.of(arg)`; otherwise use `ZoneId.systemDefault()`. An unknown id throws `DateTimeException`/`ZoneRulesException`, which the boundary catches and turns into a stderr message + non-zero exit.
- **Rationale**: Satisfies FR-002 (default), FR-003 (override), FR-007 (reject invalid). Standard zone ids are the least-surprising, well-documented override format.
- **Alternatives considered**: Accepting abbreviations like `EST`/`PST` (ambiguous, DST-lossy — rejected); accepting UTC offsets like `+09:00` (loses DST rules and city identity — rejected for the reference, though `ZoneId.of` still accepts them if a user insists).

## Decision: Curated, fixed hub list

- **Decision**: Hard-code a small, opinionated set of major business hubs with (city label, zone id): New York `America/New_York`, London `Europe/London`, Frankfurt `Europe/Berlin`, Dubai `Asia/Dubai`, Singapore `Asia/Singapore`, Tokyo `Asia/Tokyo`, Sydney `Australia/Sydney`.
- **Rationale**: Matches the spec's scope (fixed list, no customization in v1). Keeps the tool zero-config (SC-001).
- **Alternatives considered**: User-editable list / config file — explicitly out of scope per spec Assumptions.

## Decision: Ordering by current UTC offset

- **Decision**: Present hubs sorted by their current UTC offset (west → east), so the output reads like a global timeline. The reference zone is inserted/marked in place.
- **Rationale**: FR-010 requires a stable, predictable, scannable order; offset ordering is intuitive for a world clock.
- **Alternatives considered**: Alphabetical (less intuitive for time comparison); insertion order (arbitrary). Offset ordering chosen.

## Decision: Reference marking and inclusion

- **Decision**: Mark the line matching the reference zone (e.g. a `*` / `<- you` marker, FR-005). If the reference zone is not among the curated hubs, add it as an extra labeled row (FR-006).
- **Rationale**: A first-time user must locate their own time without docs (SC-005) and never lose the reference (FR-006).
- **Alternatives considered**: Printing the reference separately above the table — acceptable, but inline marking keeps a single sorted timeline.

## Decision: Output format — minute precision, aligned columns

- **Decision**: One row per hub: `HH:mm  Day  City (Zone)`, padded to aligned columns, minute precision, 24-hour clock. Errors and usage to stderr.
- **Rationale**: Minute precision per spec; a day-of-week hint disambiguates cross-midnight differences; aligned columns aid scanning.
- **Alternatives considered**: Seconds precision (needless churn for a snapshot); per-locale formatting (out of scope v1).

## Decision: Testing with zunit

- **Decision**: Use zunit; drive time-dependent logic from an injected `Instant`/`Clock` (not `Instant.now()` directly) so tests are deterministic.
- **Rationale**: Enables asserting shared-instant behavior, DST offsets, default/override resolution, and invalid-zone rejection without wall-clock flakiness.
- **Alternatives considered**: JUnit (not the java-cli-app convention here); testing against real `now()` (flaky — rejected).
