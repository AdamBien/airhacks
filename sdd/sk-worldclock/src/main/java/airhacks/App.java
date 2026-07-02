package airhacks;

import airhacks.clock.boundary.WorldClock;
import airhacks.zones.control.ReferenceZone;

import java.time.Instant;
import java.time.ZoneId;

public interface App {

    static void main(String... args) {
        try {
            IO.println(WorldClock.render(Instant.now(), zoneFor(args)));
        } catch (IllegalArgumentException invalidInput) {
            System.err.println(invalidInput.getMessage());
            System.exit(1);
        }
    }

    static ZoneId zoneFor(String... args) {
        return switch (args.length) {
            case 0 -> ReferenceZone.systemDefault();
            case 1 -> ReferenceZone.of(args[0]);
            default -> throw new IllegalArgumentException("Usage: worldclock [ZONE_ID]");
        };
    }

}
