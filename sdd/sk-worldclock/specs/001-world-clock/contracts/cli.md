# CLI Contract: World Clock

The tool's only external interface is its command line, stdout, stderr, and exit code.

## Invocation

```text
java -jar zbo/app.jar [ZONE_ID]
```

| Argument   | Cardinality | Meaning                                                                 |
|------------|-------------|-------------------------------------------------------------------------|
| `ZONE_ID`  | 0 or 1      | IANA timezone id used as the reference zone (e.g. `Asia/Tokyo`). Omit to use the machine default. |

## Behavior

- **No argument** → reference zone = `ZoneId.systemDefault()` (FR-002).
- **One valid `ZONE_ID`** → reference zone = that zone (FR-003).
- **One invalid `ZONE_ID`** → error, no clock output (FR-007).
- **Two or more arguments** → usage error (edge case: too many arguments).

## Standard output (success)

A human-readable table, one row per hub, sorted west → east by current UTC offset, minute precision, 24-hour clock. The reference row is marked. The reference zone appears even if not a curated hub (FR-005, FR-006, FR-010).

Illustrative shape (values depend on the current instant and DST):

```text
06:30  Mon  New York    (America/New_York)
11:30  Mon  London      (Europe/London)
12:30  Mon  Frankfurt   (Europe/Berlin)   *
14:30  Mon  Dubai       (Asia/Dubai)
18:30  Mon  Singapore   (Asia/Singapore)
19:30  Mon  Tokyo       (Asia/Tokyo)
20:30  Mon  Sydney      (Australia/Sydney)

* = your reference timezone
```

- All rows reflect the **same instant** (FR-004).
- The `*` marks the row whose zone equals the reference (FR-005).

## Standard error (failure)

- Invalid zone id:

  ```text
  Error: 'Mars/Phobos' is not a valid timezone id.
  ```

- Too many arguments:

  ```text
  Usage: worldclock [ZONE_ID]
  ```

Error text goes to **stderr**, never stdout, and no partial clock table is printed.

## Exit codes

| Code | Condition                          |
|------|------------------------------------|
| `0`  | Clock table printed successfully   |
| non-zero (`1`) | Invalid zone id or wrong argument count |
