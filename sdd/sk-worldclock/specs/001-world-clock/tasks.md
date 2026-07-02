---
description: "Task list for World Clock for Business Hubs"
---

# Tasks: World Clock for Business Hubs

**Input**: Design documents from `/specs/001-world-clock/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/cli.md, quickstart.md

**Tests**: INCLUDED. plan.md mandates test-first with zunit, and acceptance scenarios exist in spec.md. Deterministic tests inject a fixed `Instant`/`Clock`.

**Organization**: Tasks grouped by user story (US1 = default-TZ display, US2 = zone override) for independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: US1, US2 (Setup/Foundational/Polish have no story label)
- BCE layout: `src/main/java/airhacks/<bc>/<layer>/`, tests in `src/test/java/airhacks/`

## Path Conventions

- Main: `src/main/java/airhacks/`
- Test: `src/test/java/airhacks/`
- Build: `zb` (zero-dependency), package `zbo/app.jar`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Establish the BCE package structure and confirm the build runs.

- [ ] T001 Create BCE package directories under `src/main/java/airhacks/`: `clock/entity/`, `clock/control/`, `clock/boundary/`, `zones/control/`; and test dirs `src/test/java/airhacks/clock/` and `src/test/java/airhacks/zones/`
- [ ] T002 Verify the toolchain: run `zb` and confirm the existing `src/main/java/airhacks/App.java` compiles and packages to `zbo/app.jar`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared, story-agnostic domain data both user stories depend on. No user value alone.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [ ] T003 [P] Create `Hub` record (`label` String, `zone` ZoneId) in `src/main/java/airhacks/clock/entity/Hub.java`
- [ ] T004 [P] Create `Reading` record (`label` String, `localTime` ZonedDateTime, `isReference` boolean) in `src/main/java/airhacks/clock/entity/Reading.java`
- [ ] T005 Create `Hubs` control with the curated fixed list (New York, London, Frankfurt, Dubai, Singapore, Tokyo, Sydney) as `List<Hub>` in `src/main/java/airhacks/clock/control/Hubs.java` (depends on T003)
- [ ] T006 [P] Add `HubsTest` asserting all 7 curated hubs are present with valid `ZoneId`s in `src/test/java/airhacks/clock/HubsTest.java`

**Checkpoint**: Domain entities and curated list ready — user stories can begin.

---

## Phase 3: User Story 1 - See current time across business hubs (Priority: P1) 🎯 MVP

**Goal**: Running the tool with no arguments prints the current time for every curated hub at one shared instant, using the machine default timezone as the marked reference.

**Independent Test**: Run `java -jar zbo/app.jar` with no args; confirm every curated hub appears, all rows reflect the same instant with correct current offsets, and the default-zone row is marked `*` (added as an extra row if the default is not a curated hub).

### Tests for User Story 1 ⚠️ (write first, ensure they FAIL)

- [ ] T007 [P] [US1] `ReferenceZoneTest` — with no argument, resolver returns `ZoneId.systemDefault()`, in `src/test/java/airhacks/zones/ReferenceZoneTest.java`
- [ ] T008 [P] [US1] `WorldClockTest` — given a fixed `Instant` + reference zone: all `Reading`s share that instant (FR-004), offsets reflect DST at that instant (FR-009), exactly one `Reading.isReference` is true (FR-005), readings sorted west→east by UTC offset (FR-010), and a non-hub reference is added as an extra row (FR-006), in `src/test/java/airhacks/clock/WorldClockTest.java`

### Implementation for User Story 1

- [ ] T009 [US1] Implement `ReferenceZone` control with default resolution (`ZoneId.systemDefault()` when no argument) in `src/main/java/airhacks/zones/control/ReferenceZone.java`
- [ ] T010 [US1] Implement `WorldClock` boundary: accept a reference `Instant` + reference `ZoneId`, produce `List<Reading>` from `Hubs` (converting the shared instant per zone), mark the reference row, add the reference as an extra row when not a curated hub, and sort west→east by offset, in `src/main/java/airhacks/clock/boundary/WorldClock.java` (depends on T003, T004, T005)
- [ ] T011 [US1] Implement rendering in `WorldClock` (or a private formatter): one aligned row per reading `HH:mm  Day  City (Zone)` with the `*` reference marker and legend, minute precision, 24-hour clock, per contracts/cli.md, in `src/main/java/airhacks/clock/boundary/WorldClock.java`
- [ ] T012 [US1] Wire `App.java`: with no argument, capture a single `Instant.now()`, resolve default zone via `ReferenceZone`, render via `WorldClock`, print to stdout, exit 0, in `src/main/java/airhacks/App.java` (depends on T009, T010, T011)

**Checkpoint**: `java -jar zbo/app.jar` shows the full hub table for the default zone — MVP is functional and demoable.

---

## Phase 4: User Story 2 - Override the reference timezone (Priority: P2)

**Goal**: Supplying a single zone id makes that zone the marked reference; an invalid id fails cleanly.

**Independent Test**: Run `java -jar zbo/app.jar Asia/Tokyo` and confirm the Tokyo row is marked; run with `Mars/Phobos` and confirm a stderr error, no clock output, and non-zero exit.

### Tests for User Story 2 ⚠️ (write first, ensure they FAIL)

- [ ] T013 [P] [US2] Extend `ReferenceZoneTest`: a valid arg resolves to that `ZoneId` (FR-003); an invalid arg is rejected (throws / signals error, no silent default) (FR-007), in `src/test/java/airhacks/zones/ReferenceZoneTest.java`
- [ ] T014 [P] [US2] `AppTest` — argument-count handling: one valid arg → success/exit 0; invalid zone → non-zero exit with stderr message and no stdout table; two+ args → usage error, non-zero exit, in `src/test/java/airhacks/AppTest.java`

### Implementation for User Story 2

- [ ] T015 [US2] Extend `ReferenceZone` to resolve a supplied zone id via `ZoneId.of(arg)` and signal invalid ids distinctly (no fallback), in `src/main/java/airhacks/zones/control/ReferenceZone.java`
- [ ] T016 [US2] Extend `App.java` argument handling: 0 args → default (US1 path); 1 arg → override; invalid zone → stderr error naming the value + exit 1; ≥2 args → usage message on stderr + exit 1; never print a partial table on error, per contracts/cli.md, in `src/main/java/airhacks/App.java` (depends on T015)

**Checkpoint**: Both default and override paths work; invalid input fails cleanly. All user stories independently functional.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Verification and finishing touches spanning both stories.

- [ ] T017 Run full `zb` build and the zunit suite; confirm all tests pass and `zbo/app.jar` is produced
- [ ] T018 Execute all quickstart.md scenarios (1–6) manually and confirm actual output matches expected (default, shared-instant/DST, override, non-hub override, invalid zone, too-many-args)
- [ ] T019 [P] Confirm `README.md` build/run examples match the shipped CLI contract (default vs override invocations)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately.
- **Foundational (Phase 2)**: Depends on Setup — BLOCKS both user stories.
- **User Story 1 (Phase 3)**: Depends on Foundational. Delivers the MVP.
- **User Story 2 (Phase 4)**: Depends on Foundational; extends the `ReferenceZone` and `App` files authored/wired in US1, so it is sequenced after US1 in practice.
- **Polish (Phase 5)**: Depends on all targeted stories complete.

### User Story Dependencies

- **US1 (P1)**: Independent — full default-zone display path.
- **US2 (P2)**: Independently testable (its own zone-override and error behavior), but shares `ReferenceZone.java` and `App.java` with US1, so complete US1 first to avoid same-file conflicts.

### Within Each User Story

- Tests written first and failing before implementation.
- Entities → control → boundary → `App` wiring.

### Parallel Opportunities

- Foundational: T003 and T004 (different entity files) run in parallel; T006 in parallel once T003/T005 exist.
- US1 tests T007 and T008 run in parallel (different files).
- US2 tests T013 and T014 run in parallel (different files).
- Implementation tasks touching the same file (`ReferenceZone.java`: T009/T015; `App.java`: T012/T016; `WorldClock.java`: T010/T011) are NOT parallel.

---

## Parallel Example: User Story 1

```bash
# Write US1 tests together (different files):
Task: "ReferenceZoneTest default resolution in src/test/java/airhacks/zones/ReferenceZoneTest.java"
Task: "WorldClockTest shared-instant/DST/order/reference in src/test/java/airhacks/clock/WorldClockTest.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Phase 1 Setup → Phase 2 Foundational → Phase 3 US1.
2. **STOP and VALIDATE**: `java -jar zbo/app.jar` shows the hub table for your default zone.
3. Demo the MVP.

### Incremental Delivery

1. Setup + Foundational → foundation ready.
2. US1 → test → demo (MVP: default-TZ world clock).
3. US2 → test → demo (adds zone override + clean error handling).
4. Polish → full build, quickstart validation, README check.

---

## Notes

- [P] = different files, no dependencies.
- Zero-dependency: `java.time` only; no external libraries.
- Inject a fixed `Instant`/`Clock` into `WorldClock` for deterministic tests (never call `Instant.now()` inside tested logic).
- Commit after each task or logical group.
