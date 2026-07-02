package airhacks.clock.entity;

import java.time.ZonedDateTime;

public record Reading(String label, ZonedDateTime localTime, boolean isReference) {

    public Reading {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("reading label is required");
        }
        if (localTime == null) {
            throw new IllegalArgumentException("reading localTime is required");
        }
    }

}
