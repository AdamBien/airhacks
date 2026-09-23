package airhacks.qmp.fleet.entity;

import java.util.Arrays;
import java.util.Optional;

/// The unit customers reserve and the unit of pricing.
public enum BikeType {
    BIKE("bike"),
    E_BIKE("e-bike");

    private final String label;

    BikeType(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public static Optional<BikeType> of(String label) {
        return Arrays.stream(values())
                .filter(type -> type.label.equals(label))
                .findFirst();
    }
}
