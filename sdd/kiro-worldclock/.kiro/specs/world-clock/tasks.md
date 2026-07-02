# Implementation Plan: World Clock

## Overview

Convert the World Clock design into incremental coding steps for a zero-dependency Java 25 CLI built with `zb`. The plan follows the BCE structure: build the pure entities and catalog first, then the pure core computation and formatting, then the CLI boundary that owns argument parsing, stream writes, and exit codes, and finally wire the entry point. Property-based tests (jqwik, test scope only) validate the seven correctness properties close to the code they cover; unit tests cover concrete flows and edge cases. Test sub-tasks are marked with `*` and are optional.

## Tasks

- [ ] 1. Set up test infrastructure and hub catalog
  - [ ] 1.1 Configure jqwik as a test-scope dependency and create the test source layout
    - Add jqwik as a test-only dependency for the `zb` build so it stays out of the shipped `zbo/app.jar`
    - Create the test source directory structure mirroring `airhacks.*` packages
    - _Requirements: 1.1_

  - [ ] 1.2 Create the BusinessHub entity
    - Implement `airhacks.hubs.entity.BusinessHub` as `record BusinessHub(String name, ZoneId zone)`
    - _Requirements: 1.2_

  - [ ] 1.3 Implement the fixed HubCatalog
    - Implement `airhacks.hubs.control.HubCatalog.hubs()` returning the fixed catalog of well-known business hubs (New York and Tokyo) with valid `ZoneId`s
    - _Requirements: 1.1_

  - [ ]* 1.4 Write unit tests for HubCatalog
    - Assert the catalog is non-empty, contains the expected hubs, and every entry has a valid `ZoneId`
    - _Requirements: 1.1_

- [ ] 2. Implement clock core computation
  - [ ] 2.1 Create ClockReading and ClockReport entities
    - Implement `airhacks.clock.entity.ClockReading` as `record ClockReading(String label, ZoneId zone, ZonedDateTime localTime)`
    - Implement `airhacks.clock.entity.ClockReport` as `record ClockReport(ClockReading reference, List<ClockReading> hubReadings)` documenting the single-instant invariant
    - _Requirements: 1.2, 1.4_

  - [ ] 2.2 Implement WorldClock.report
    - Implement `airhacks.clock.control.WorldClock.report(Instant, ZoneId reference, List<BusinessHub> hubs)` as a pure function projecting one shared `Instant` into the reference zone and each hub zone
    - Label the reference reading with `zone.getId()` and each hub reading with the hub name
    - _Requirements: 1.1, 1.2, 1.4, 3.2_

  - [ ]* 2.3 Write property test for single-instant consistency
    - **Property 1: Single-instant consistency**
    - **Validates: Requirements 1.4, 3.2**
    - Tag: `Feature: world-clock, Property 1`; minimum 100 generated cases over random instants, reference zones, and catalogs

  - [ ]* 2.4 Write property test for report structure
    - **Property 2: Report structure covers every hub correctly**
    - **Validates: Requirements 1.1, 1.2**
    - Tag: `Feature: world-clock, Property 2`; assert one reading per catalog entry with matching zone and projected local time

- [ ] 3. Implement report formatting
  - [ ] 3.1 Implement ReportFormatter
    - Implement `airhacks.format.control.ReportFormatter.format(ClockReport)` producing printable lines using a 24-hour hours-and-minutes format
    - Include the reference IANA identifier in the reference line and the hub name plus formatted time in each hub line
    - _Requirements: 1.3, 2.2, 2.3_

  - [ ]* 3.2 Write property test for 24-hour time formatting
    - **Property 3: 24-hour time formatting**
    - **Validates: Requirements 1.3**
    - Tag: `Feature: world-clock, Property 3`; assert each formatted time matches `HH:mm` (00-23 / 00-59) and equals the projected local time

  - [ ]* 3.3 Write property test for reference reading presence and labeling
    - **Property 5: Reference reading is present and IANA-labeled**
    - **Validates: Requirements 2.2, 2.3**
    - Tag: `Feature: world-clock, Property 5`; assert the report has a reference reading labeled with `zone.getId()` that appears in the rendered reference line

  - [ ]* 3.4 Write unit tests for formatting edge cases
    - Cover boundary times around midnight (`00:00`) and `23:59` under the 24-hour formatter
    - _Requirements: 1.3_

- [ ] 4. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 5. Implement CLI argument parsing
  - [ ] 5.1 Create the Arguments record
    - Implement `airhacks.cli.entity.Arguments` as `record Arguments(boolean help, Optional<String> timezoneOverride)`
    - _Requirements: 3.1, 4.1_

  - [ ] 5.2 Implement ArgumentsParser and typed exceptions
    - Implement `airhacks.cli.control.ArgumentsParser.parse(String...)` recognizing `--help`/`-h` and `--timezone <id>`/`--timezone=<id>`
    - Add `UnknownOptionException` for unrecognized options and `MissingValueException` when `--timezone` has no value
    - _Requirements: 3.1, 4.1, 4.2_

  - [ ]* 5.3 Write unit tests for ArgumentsParser
    - Cover help flags, valid override forms, unknown option, and missing `--timezone` value
    - _Requirements: 4.1, 4.2_

- [ ] 6. Implement CLI boundary and reference resolution
  - [ ] 6.1 Implement WorldClockCLI.run
    - Implement `airhacks.cli.boundary.WorldClockCLI.run(String[] args, Clock clock, ZoneId systemDefault, Appendable out, Appendable err)`
    - Resolve the reference zone (override via `ZoneId.of`, else system default), invoke `WorldClock` and `ReportFormatter`, write results to `out`, errors and usage-on-error to `err`
    - Centralize exit-code selection: `0` for success and help, non-zero for invalid override, unknown option, and missing value; write no partial report on error paths
    - _Requirements: 2.1, 3.1, 3.3, 4.1, 4.2, 4.3_

  - [ ]* 6.2 Write property test for reference timezone resolution
    - **Property 4: Reference timezone resolution**
    - **Validates: Requirements 2.1, 3.1**
    - Tag: `Feature: world-clock, Property 4`; assert no override uses the injected system default and a valid override uses `ZoneId.of(override)`

  - [ ]* 6.3 Write property test for invalid input handling
    - **Property 6: Invalid input yields an error and a non-zero exit code**
    - **Validates: Requirements 3.3, 4.2**
    - Tag: `Feature: world-clock, Property 6`; feed invalid IANA strings and unrecognized tokens, assert non-empty stderr, non-zero exit, and empty stdout report

  - [ ]* 6.4 Write property test for successful runs
    - **Property 7: Successful run yields a zero exit code**
    - **Validates: Requirements 4.3**
    - Tag: `Feature: world-clock, Property 7`; assert valid invocations write the report to stdout and return exit code 0

  - [ ]* 6.5 Write unit tests for help output
    - Assert `--help` and `-h` write usage mentioning `--timezone` to stdout and return exit code 0
    - _Requirements: 4.1_

- [ ] 7. Wire the entry point
  - [ ] 7.1 Update App.main to delegate to WorldClockCLI
    - Replace the placeholder in `airhacks.App.main` to build a real `Clock`/`ZoneId.systemDefault()`, call `WorldClockCLI.run`, and `System.exit` with the returned code
    - _Requirements: 4.3_

- [ ] 8. Final checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional test tasks and can be skipped for a faster MVP.
- Each task references specific requirements for traceability.
- Property tests use jqwik (test scope only) with a minimum of 100 generated cases each and are tagged `Feature: world-clock, Property {n}`; the shipped `zbo/app.jar` stays zero-dependency.
- Checkpoints ensure incremental validation between major components.
- The pure core (`WorldClock`, `ReportFormatter`, `ArgumentsParser`, reference resolution) is tested with an injected `Clock` and system-default `ZoneId`, capturing output through `Appendable` buffers.

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "1.2", "5.1"] },
    { "id": 1, "tasks": ["1.3", "2.1", "5.2"] },
    { "id": 2, "tasks": ["1.4", "2.2", "5.3"] },
    { "id": 3, "tasks": ["2.3", "2.4", "3.1"] },
    { "id": 4, "tasks": ["3.2", "3.3", "3.4", "6.1"] },
    { "id": 5, "tasks": ["6.2", "6.3", "6.4", "6.5", "7.1"] }
  ]
}
```
