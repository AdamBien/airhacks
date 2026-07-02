# Specification — World Business Clock CLI

*Requirements written with **RUPP's sentence template** (SOPHIST).*

## 1. Context & Purpose

A single-file Java 25 command-line tool that shows, in one glance, the current time across
the world's major business hubs, plus how the home working day (09:00 / 12:00 / 17:00)
maps onto each hub. It removes the mental arithmetic of "what time is it for them right
now, and when does my morning/noon/end-of-day land in their day" when scheduling calls
across regions.

**Delivery:** a zero-dependency, single-file Java 25 PATH script (`java-cli-script` style,
shebang-launched via single-file source execution), no `.java` extension, installed on
PATH.

## 2. RUPP Sentence Template

> `[<condition>] <subject> shall [provide <actor> with the ability to | be able to] <process> <object> [<detail>].`

Obligation levels: **shall** = mandatory, **should** = desirable, **will** = intention.
Activity types: *autonomous* (`shall <process>`), *user interaction*
(`shall provide <actor> with the ability to <process>`), *interface* (`shall be able to
<process>`).

## 3. Actors & Glossary

- **User** — the person running the tool from a terminal.
- **worldclock** — the system (the CLI tool itself); the subject of every requirement.
- **Home hub** — the business hub matching the machine's system timezone; the anchor for
  the target-time columns. Auto-detected, never configured in v1.
- **Business hub** — one entry in the curated built-in list (§4).
- **Target times** — the fixed working-day anchors 09:00, 12:00, 17:00 in the **home hub**.

## 4. Curated Built-in Business Hubs (fixed data, v1)

| Display name  | IANA zone           |
|---------------|---------------------|
| San Francisco | America/Los_Angeles |
| New York      | America/New_York    |
| São Paulo     | America/Sao_Paulo   |
| London        | Europe/London       |
| Frankfurt     | Europe/Berlin       |
| Dubai         | Asia/Dubai          |
| Mumbai        | Asia/Kolkata        |
| Singapore     | Asia/Singapore      |
| Hong Kong     | Asia/Hong_Kong      |
| Tokyo         | Asia/Tokyo          |
| Sydney        | Australia/Sydney    |

## 5. Functional Requirements

**R1 — Show current time (autonomous).**
When invoked with no arguments, the **worldclock** shall display the current local time
for every business hub in the curated built-in list.

**R2 — Aligned table (autonomous).**
The **worldclock** shall render its output as a single column-aligned table with one row
per business hub and the columns: *City*, *Zone*, *Now*, *Date*, *Offset*, *09:00*,
*12:00*, *17:00*.

**R3 — 24-hour formatting (autonomous).**
The **worldclock** shall format every clock value in 24-hour `HH:mm` notation and every
date value as ISO `yyyy-MM-dd`.

**R4 — UTC offset column (autonomous).**
The **worldclock** shall display, for each business hub, its current UTC offset in
`UTC±hh:mm` form, reflecting the offset in effect at the moment of invocation.

**R5 — Auto-detected home hub (autonomous).**
On start-up, the **worldclock** shall determine the home hub from the machine's system
timezone (`ZoneId.systemDefault()`) and use it as the anchor for all target-time columns.

**R6 — Home hub anchor fallback (autonomous).**
If the system timezone does not match any hub in the curated list, the **worldclock**
shall use the system timezone itself as the anchor for the target-time columns without
adding it as a table row.

**R7 — Target-time mapping (autonomous).**
For each business hub and each target time (09:00, 12:00, 17:00 in the home hub on the
current date), the **worldclock** shall display, in the corresponding column, that hub's
local clock time at the same instant.

**R8 — Home hub marking (autonomous).**
When a business hub matches the home hub, the **worldclock** shall visually mark that row
(e.g. a `*` after the city name) so the user can identify the anchor.

**R9 — Stable ordering (autonomous).**
The **worldclock** shall order the table rows by each hub's current UTC offset in
ascending order (westmost first), producing a west-to-east reading order.

**R10 — DST correctness (autonomous).**
The **worldclock** shall compute every displayed time, offset, and target-time mapping
using timezone rules that account for daylight saving time in effect on the current date.

**R11 — Header line (autonomous).**
The **worldclock** shall print a header line naming the home hub, its current offset, and
the current home date above the table.

**R12 — Help (user interaction).**
When invoked with `-h` or `--help`, the **worldclock** shall provide the user with the
ability to read a short usage description listing its behavior and the built-in hub list,
and shall exit without printing the table.

**R13 — Reject unknown arguments (autonomous).**
If invoked with an unrecognized argument, the **worldclock** shall print a short error
message referencing `--help` and shall exit with a non-zero status code.

## 6. Non-Functional Requirements

**N1 — Zero dependencies (constraint).**
The **worldclock** shall be implemented using only the Java 25 standard library, with no
third-party dependencies and no build system beyond single-file source execution.

**N2 — Single file (constraint).**
The **worldclock** shall be contained in one executable source file with no `.java`
extension, launchable directly via its shebang from any directory on PATH.

**N3 — Offline (constraint).**
The **worldclock** shall compute all output locally and shall not perform any network
access.

**N4 — Responsiveness (quality).**
The **worldclock** should complete a no-argument run and return to the shell prompt in
under 500 ms on a typical developer machine.

**N5 — Time source (constraint).**
The **worldclock** shall derive all times from a single captured instant so that every
cell in one run is internally consistent.

**N6 — Modern Java style (constraint).**
The **worldclock** shall follow the `java-cli-script` / `java-conventions` rules: records
for immutable hub definitions, `java.time` (`ZoneId`, `ZonedDateTime`, `DateTimeFormatter`)
for all date/time work, streams where they read clearly, minimal visibility, and
`System.out` / `System.err` for output.

## 7. Illustrative Output (home = Frankfurt)

```
World Business Clock — home: Frankfurt (UTC+02:00)  2026-07-02

City            Zone                  Now    Date        Offset     09:00  12:00  17:00
San Francisco   America/Los_Angeles   05:37  2026-07-02  UTC-07:00  00:00  03:00  08:00
New York        America/New_York      08:37  2026-07-02  UTC-04:00  03:00  06:00  11:00
São Paulo       America/Sao_Paulo     09:37  2026-07-02  UTC-03:00  04:00  07:00  12:00
London          Europe/London         13:37  2026-07-02  UTC+01:00  08:00  11:00  16:00
Frankfurt *     Europe/Berlin         14:37  2026-07-02  UTC+02:00  09:00  12:00  17:00
Dubai           Asia/Dubai            16:37  2026-07-02  UTC+04:00  11:00  14:00  19:00
Mumbai          Asia/Kolkata          18:07  2026-07-02  UTC+05:30  12:30  15:30  20:30
Singapore       Asia/Singapore        20:37  2026-07-02  UTC+08:00  15:00  18:00  23:00
Hong Kong       Asia/Hong_Kong        20:37  2026-07-02  UTC+08:00  15:00  18:00  23:00
Tokyo           Asia/Tokyo            21:37  2026-07-02  UTC+09:00  16:00  19:00  00:00
Sydney          Australia/Sydney      22:37  2026-07-02  UTC+10:00  17:00  20:00  01:00
```

The 09:00 / 12:00 / 17:00 columns read: "when it is that time in the home hub, the local
clock in this hub shows …". Target times crossing midnight simply wrap.
