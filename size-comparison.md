# Size Comparison: Markdown Spec vs. Java Code

**Feature**: World Clock for Business Hubs (`001-world-clock`)
**Measured**: 2026-07-02

## Per-file breakdown

### Markdown (spec artifacts)

| File | Lines | Bytes |
|------|------:|------:|
| README.md | 31 | 1,477 |
| specs/001-world-clock/checklists/requirements.md | 35 | 1,425 |
| specs/001-world-clock/contracts/cli.md | 64 | 2,065 |
| specs/001-world-clock/data-model.md | 59 | 2,815 |
| specs/001-world-clock/plan.md | 89 | 4,675 |
| specs/001-world-clock/quickstart.md | 66 | 2,045 |
| specs/001-world-clock/research.md | 51 | 4,631 |
| specs/001-world-clock/spec.md | 90 | 6,666 |
| specs/001-world-clock/tasks.md | 166 | 9,402 |
| **Total** | **651** | **35,201** |

### Java main source

| File | Lines | Bytes |
|------|------:|------:|
| src/main/java/airhacks/App.java | 28 | 766 |
| src/main/java/airhacks/clock/boundary/WorldClock.java | 67 | 2,507 |
| src/main/java/airhacks/clock/control/Hubs.java | 22 | 562 |
| src/main/java/airhacks/clock/entity/Hub.java | 20 | 486 |
| src/main/java/airhacks/clock/entity/Reading.java | 16 | 439 |
| src/main/java/airhacks/zones/control/ReferenceZone.java | 20 | 479 |
| **Total** | **173** | **5,239** |

### Java tests (zunit)

| File | Lines | Bytes |
|------|------:|------:|
| test/AppTest.java | 32 | 1,180 |
| test/HubsTest.java | 24 | 830 |
| test/ReferenceZoneTest.java | 24 | 1,008 |
| test/WorldClockTest.java | 48 | 2,493 |
| **Total** | **128** | **5,511** |

## Totals

| Category | Lines | Bytes |
|----------|------:|------:|
| Markdown (spec + README) | 651 | 35,201 |
| Java total (main + tests) | 301 | 10,750 |
| — Java main only | 173 | 5,239 |
| — Java tests only | 128 | 5,511 |

## Ratios

| Comparison | By bytes | By lines |
|------------|---------:|---------:|
| Markdown : all Java | **3.3×** | 2.2× |
| Markdown : Java main only | **6.7×** | 3.8× |
| Java tests : Java main | 1.05× | 0.74× |

## Takeaway

The spec-driven workflow produced roughly **3.5 KB of documentation per 1 KB of shipped
production code** — and nearly **7× the size of the main source** when tests are excluded.

This is characteristic of Spec-Driven Development on a small feature: the fixed-cost
artifacts (spec, plan, research, contracts, tasks, checklist) do not shrink with problem
size, so on a 173-line CLI the planning prose heavily outweighs the code. On a larger
feature the same artifacts amortize and the ratio inverts.
