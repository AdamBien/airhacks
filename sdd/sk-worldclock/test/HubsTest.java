import airhacks.clock.control.Hubs;
import airhacks.clock.entity.Hub;

import java.time.ZoneId;

void main() {
    var hubs = Hubs.all();

    if (hubs.size() != 7)
        throw new AssertionError("expected 7 curated hubs but got " + hubs.size());

    for (var hub : hubs) {
        if (hub.label() == null || hub.label().isBlank())
            throw new AssertionError("hub with blank label: " + hub);
        if (!ZoneId.getAvailableZoneIds().contains(hub.zone().getId()))
            throw new AssertionError("unknown zone id: " + hub.zone());
    }

    var labels = hubs.stream().map(Hub::label).toList();
    for (var expected : new String[]{"New York", "London", "Frankfurt", "Tokyo", "Sydney"}) {
        if (!labels.contains(expected))
            throw new AssertionError("missing expected hub: " + expected);
    }
}
