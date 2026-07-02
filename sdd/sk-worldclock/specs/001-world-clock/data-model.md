# Phase 1 Data Model: World Clock for Business Hubs

All types are immutable Java `record`s. No persistence — the model is in-memory only.

## Entity: Hub

A named location of business significance.

| Field   | Type      | Notes                                              |
|---------|-----------|----------------------------------------------------|
| `label` | `String`  | Human-readable city/hub name, e.g. `"New York"`    |
| `zone`  | `ZoneId`  | IANA timezone, e.g. `America/New_York`             |

**Validation**: `label` non-blank; `zone` non-null. Hubs are only ever constructed from the curated constant list, so values are known-good at compile time.

## Entity: Reference Zone

The timezone used as the user's point of comparison.

- Not a distinct record — represented as a `ZoneId` produced by the `zones` BC.
- Source: the single optional CLI argument if present, otherwise `ZoneId.systemDefault()`.

**Validation**: When derived from an argument, must be a resolvable `ZoneId`; an unresolvable value is rejected (no silent fallback) — see FR-007.

## Entity: Reading

The current time for one hub at the shared reference instant.

| Field         | Type            | Notes                                                        |
|---------------|-----------------|--------------------------------------------------------------|
| `label`       | `String`        | Hub label (or reference zone id if not a curated hub)        |
| `localTime`   | `ZonedDateTime` | The shared `Instant` converted into this entry's zone        |
| `isReference` | `boolean`       | `true` for the entry matching the reference zone (FR-005)    |

**Derivation**: `localTime = instant.atZone(zone)`. The `offset` and day-of-week shown in output are read from `localTime`. `isReference` is `true` when the entry's zone equals the resolved reference zone.

## Relationships

```text
Reference Zone (ZoneId)
        │  determines which Reading.isReference == true
        ▼
Hubs (curated List<Hub>) ──┐
Reference Zone (if absent) ─┤──► [shared Instant] ──► List<Reading> (sorted by current UTC offset)
   (added as extra entry)  ─┘
```

- The set of `Reading`s = one per curated `Hub`, **plus** one extra for the reference zone when it is not already a curated hub (FR-006).
- Exactly one `Reading` has `isReference == true`.
- All `Reading`s derive from the **same** `Instant` (FR-004).

## Ordering rule

`Reading`s are sorted ascending by the current UTC offset of `localTime` (west → east). Ties broken by `label` for determinism (FR-010).

## Derived / computed values (not stored)

- **UTC offset per reading**: from `localTime.getOffset()` — reflects current DST (FR-009).
- **Day-of-week hint**: from `localTime.getDayOfWeek()` — disambiguates cross-midnight readings.
