package airhacks.clock.boundary;

import airhacks.clock.control.Hubs;
import airhacks.clock.entity.Reading;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public interface WorldClock {

    DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm", Locale.US);
    DateTimeFormatter DAY = DateTimeFormatter.ofPattern("EEE", Locale.US);

    static List<Reading> readings(Instant instant, ZoneId reference) {
        var readings = new ArrayList<Reading>();
        var referenceIsHub = false;
        for (var hub : Hubs.all()) {
            var isReference = hub.zone().equals(reference);
            referenceIsHub = referenceIsHub || isReference;
            readings.add(new Reading(hub.label(), instant.atZone(hub.zone()), isReference));
        }
        if (!referenceIsHub) {
            readings.add(new Reading(labelFor(reference), instant.atZone(reference), true));
        }
        readings.sort(Comparator
                .comparingInt((Reading reading) -> reading.localTime().getOffset().getTotalSeconds())
                .thenComparing(Reading::label));
        return List.copyOf(readings);
    }

    static String render(Instant instant, ZoneId reference) {
        var readings = readings(instant, reference);
        var labelWidth = readings.stream()
                .mapToInt(reading -> reading.label().length())
                .max()
                .orElse(0);
        var table = readings.stream()
                .map(reading -> line(reading, labelWidth))
                .collect(Collectors.joining(System.lineSeparator()));
        return "%s%n%n* = your reference timezone".formatted(table);
    }

    private static String line(Reading reading, int labelWidth) {
        var marker = reading.isReference() ? "  *" : "";
        var pattern = "%s  %s  %-" + labelWidth + "s  (%s)%s";
        return pattern.formatted(
                reading.localTime().format(TIME),
                reading.localTime().format(DAY),
                reading.label(),
                reading.localTime().getZone().getId(),
                marker);
    }

    private static String labelFor(ZoneId zone) {
        var id = zone.getId();
        var lastSlash = id.lastIndexOf('/');
        var city = lastSlash >= 0 ? id.substring(lastSlash + 1) : id;
        return city.replace('_', ' ');
    }

}
