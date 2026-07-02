# Quickstart: World Clock for Business Hubs

Validation guide proving the feature works end-to-end. See [contracts/cli.md](./contracts/cli.md) for the full CLI contract and [data-model.md](./data-model.md) for the model.

## Prerequisites

- Java 25+
- [zb](https://github.com/AdamBien/zb) on the PATH

## Build & test

```bash
zb                      # compile, run zunit tests, package zbo/app.jar
```

## Validation scenarios

Map each to its spec requirement / acceptance scenario.

### 1. Default timezone (US1 / FR-002)

```bash
java -jar zbo/app.jar
```

**Expect**: A table with every curated hub (New York, London, Frankfurt, Dubai, Singapore, Tokyo, Sydney). The row matching your machine's default zone is marked `*`. If your default zone is not a curated hub, it appears as an extra marked row (FR-006).

### 2. Shared instant & DST correctness (FR-004, FR-009, SC-002)

Inspect the output of scenario 1: the offsets between rows must equal the real current offsets between those zones (accounting for DST in effect today). All rows represent the same moment.

### 3. Override the reference zone (US2 / FR-003)

```bash
java -jar zbo/app.jar Asia/Tokyo
```

**Expect**: Same hubs, but the Tokyo row is now marked `*` as the reference.

### 4. Override with a non-hub zone (FR-006)

```bash
java -jar zbo/app.jar America/Sao_Paulo
```

**Expect**: The curated hubs plus an extra `America/Sao_Paulo` row, marked `*`.

### 5. Invalid zone id (FR-007, SC-004)

```bash
java -jar zbo/app.jar Mars/Phobos ; echo "exit=$?"
```

**Expect**: Error on **stderr** naming the bad value, **no** clock table on stdout, `exit=1`.

### 6. Too many arguments (edge case)

```bash
java -jar zbo/app.jar Asia/Tokyo Europe/London ; echo "exit=$?"
```

**Expect**: Usage message on stderr, `exit=1`.

## Automated coverage

`zb` runs the zunit suite during build. Deterministic tests inject a fixed `Instant`/`Clock` so shared-instant, DST-offset, default/override, and invalid-zone assertions do not depend on wall-clock time (see plan.md test layout).
