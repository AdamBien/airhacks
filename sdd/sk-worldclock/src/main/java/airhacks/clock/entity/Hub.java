package airhacks.clock.entity;

import java.time.ZoneId;

public record Hub(String label, ZoneId zone) {

    public Hub {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("hub label is required");
        }
        if (zone == null) {
            throw new IllegalArgumentException("hub zone is required");
        }
    }

    public static Hub of(String label, String zoneId) {
        return new Hub(label, ZoneId.of(zoneId));
    }

}
