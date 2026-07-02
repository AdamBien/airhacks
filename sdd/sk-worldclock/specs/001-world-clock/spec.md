# Feature Specification: World Clock for Business Hubs

**Feature Branch**: `001-world-clock`

**Created**: 2026-07-02

**Status**: Draft

**Input**: User description: "Display current time in major business hubs. Use my default TZ, but allow to override it."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - See current time across business hubs (Priority: P1)

A user runs the tool with no arguments and immediately sees the current time in a curated set of major business hubs, presented alongside their own local time so they can compare at a glance.

**Why this priority**: This is the core value of the tool — answering "what time is it there?" for the places that matter, without any configuration. Without it, the tool has no purpose.

**Independent Test**: Run the tool with no arguments and confirm it prints the current time for every hub in the curated list plus the user's local time, using the machine's default timezone as the reference.

**Acceptance Scenarios**:

1. **Given** a machine with a valid default timezone, **When** the user runs the tool with no arguments, **Then** the current time is displayed for each major business hub.
2. **Given** the tool is run, **When** the output is produced, **Then** the user's own local time (the default timezone) is clearly identified among the results.
3. **Given** the tool is run at any moment, **When** the times are displayed, **Then** every hub's time reflects the same instant in time (consistent "now" across all hubs).

---

### User Story 2 - Override the reference timezone (Priority: P2)

A user who is planning around a location other than their own (e.g. a traveler or someone scheduling a meeting for a remote colleague) supplies a specific timezone so the reference point becomes that zone instead of the machine default.

**Why this priority**: Extends the core capability to the common "not where I am right now" case. Valuable, but the tool is already useful without it.

**Independent Test**: Run the tool with an explicit timezone argument and confirm the reference/local marker and relative context reflect the supplied zone rather than the machine default.

**Acceptance Scenarios**:

1. **Given** a valid timezone identifier is supplied, **When** the user runs the tool with that argument, **Then** the supplied zone is used as the reference instead of the machine default.
2. **Given** no timezone argument is supplied, **When** the user runs the tool, **Then** the machine's default timezone is used as the reference.

---

### Edge Cases

- **Invalid timezone override**: When the user supplies an unrecognized timezone identifier, the tool reports a clear error identifying the bad value and exits with a non-zero status, rather than silently falling back to the default.
- **Override equals a listed hub**: When the override matches a hub already in the curated list, that hub is marked as the reference rather than duplicated.
- **Default zone not in hub list**: When the user's default (or overridden) zone is not among the curated hubs, it is still shown, clearly marked as the reference.
- **DST transitions**: Displayed times correctly reflect daylight saving time in effect at the current instant for each hub.
- **Too many arguments**: When more arguments than expected are supplied, the tool reports correct usage and exits with a non-zero status.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST display the current time for a curated set of major business hubs.
- **FR-002**: System MUST use the machine's default timezone as the reference timezone when no override is provided.
- **FR-003**: Users MUST be able to override the reference timezone by supplying a timezone identifier as an argument.
- **FR-004**: System MUST compute all displayed times from a single reference instant, so every hub reflects the same "now".
- **FR-005**: System MUST clearly mark which entry corresponds to the reference (default or overridden) timezone.
- **FR-006**: System MUST include the reference timezone in the output even when it is not part of the curated hub list.
- **FR-007**: System MUST reject an invalid or unrecognized timezone override with a clear error message and a non-zero exit status.
- **FR-008**: System MUST display each hub with a human-readable label (city/hub name) and its current local time.
- **FR-009**: System MUST reflect the correct daylight-saving offset in effect for each hub at the current instant.
- **FR-010**: System MUST present hubs in a stable, predictable order (e.g. ordered by geographic/UTC offset) so output is easy to scan.

### Key Entities *(include if feature involves data)*

- **Business Hub**: A named location of business significance, identified by a display label (city name) and an associated timezone.
- **Reference Timezone**: The timezone used as the user's point of comparison — either the machine default or an explicit override.
- **Clock Reading**: The current time for a given hub at the shared reference instant, consisting of the hub label and its local time (and offset).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A user can see the current time in all major business hubs with a single command and no configuration.
- **SC-002**: 100% of displayed hub times reflect the same instant and the correct current offset (including DST) for that hub.
- **SC-003**: A user can switch the reference timezone in a single invocation by supplying one argument.
- **SC-004**: An invalid timezone override results in an understandable error 100% of the time, with no misleading or partial output.
- **SC-005**: A first-time user understands which line represents their own/reference time without external documentation.

## Assumptions

- The curated list of "major business hubs" is a fixed, opinionated set defined by the tool (e.g. New York, London, Frankfurt, Dubai, Singapore, Tokyo, Sydney). Users cannot customize the list in this version.
- The reference timezone override is supplied as a single command-line argument using a standard timezone identifier (e.g. `America/New_York`).
- Output is human-readable text written to standard output; errors are written to standard error with a non-zero exit code.
- The tool reports a point-in-time snapshot when invoked; it does not continuously refresh or run as a live display.
- Time is displayed to minute precision using the local convention of the reference; no per-hub localization of formatting is required in this version.
- Custom hub lists, favorites, persisted preferences, and meeting-planner ("find an overlapping window") features are out of scope for this version.
