import airhacks.clock.boundary.WorldClock;
import airhacks.clock.entity.Reading;

import java.time.Instant;
import java.time.ZoneId;

void main() {
    var instant = Instant.parse("2026-07-02T12:00:00Z");
    var berlin = ZoneId.of("Europe/Berlin"); // a curated hub (Frankfurt)
    var readings = WorldClock.readings(instant, berlin);

    // every reading reflects the SAME instant (FR-004)
    for (var reading : readings) {
        if (!reading.localTime().toInstant().equals(instant))
            throw new AssertionError("reading %s not at shared instant: %s".formatted(reading.label(), reading.localTime()));
    }

    // exactly one reference row (FR-005)
    var referenceCount = readings.stream().filter(Reading::isReference).count();
    if (referenceCount != 1)
        throw new AssertionError("expected exactly 1 reference reading but got " + referenceCount);

    var reference = readings.stream().filter(Reading::isReference).findFirst().orElseThrow();
    if (!reference.localTime().getZone().equals(berlin))
        throw new AssertionError("reference zone mismatch: " + reference.localTime().getZone());

    // DST in effect: Berlin is UTC+2 in July (FR-009)
    if (reference.localTime().getOffset().getTotalSeconds() != 2 * 3600)
        throw new AssertionError("expected Berlin +02:00 in July but got " + reference.localTime().getOffset());

    // sorted west -> east by current UTC offset, i.e. ascending total offset seconds (FR-010)
    for (var i = 1; i < readings.size(); i++) {
        var previous = readings.get(i - 1).localTime().getOffset().getTotalSeconds();
        var current = readings.get(i).localTime().getOffset().getTotalSeconds();
        if (previous > current)
            throw new AssertionError("readings not sorted west->east by offset at index " + i);
    }

    // a reference outside the curated list is added as an extra row (FR-006)
    var saoPaulo = ZoneId.of("America/Sao_Paulo");
    var withExtra = WorldClock.readings(instant, saoPaulo);
    if (withExtra.size() != readings.size() + 1)
        throw new AssertionError("expected an extra row for a non-hub reference; hubs=%d withExtra=%d"
                .formatted(readings.size(), withExtra.size()));
    var extraReference = withExtra.stream().filter(Reading::isReference).findFirst().orElseThrow();
    if (!extraReference.localTime().getZone().equals(saoPaulo))
        throw new AssertionError("non-hub reference zone mismatch: " + extraReference.localTime().getZone());
}
