# worldclock

## About

A zero-dependency Java 25 CLI application, built and packaged with [zb](https://github.com/AdamBien/zb). Cloned from the [bce.design](https://bce.design) / [airails.dev](https://airails.dev) `java-cli-app` template.

**Why it exists:** Distributed teams and travelers need to know the current time across major business hubs at a glance, without opening a browser or doing mental timezone math.

**Aspiration** _(seeds the distilled `## Vision`)_: Answer "what time is it there?" instantly for every hub that matters.

**Intended capabilities** _(seed the BC carving — each becomes a business component)_:
- display-hubs — renders the current time for a curated set of major business hubs (New York, London, Frankfurt, Dubai, Singapore, Tokyo, Sydney, …)
- resolve-zone — determines the reference timezone, defaulting to the system default and allowing an explicit override

## Conventions

- Uses the system default timezone (`ZoneId.systemDefault()`) as the reference unless overridden.
- Override the reference zone with a single argument, e.g. `java -jar zbo/app.jar America/New_York`.
- Writes the formatted clock table to stdout; errors (e.g. unknown zone id) to stderr with a non-zero exit.

## Prerequisites

Java 25+, [zb](https://github.com/AdamBien/zb)

## Build & run

```
zb
java -jar zbo/app.jar                 # uses your default timezone as reference
java -jar zbo/app.jar Asia/Tokyo      # overrides the reference timezone
```
