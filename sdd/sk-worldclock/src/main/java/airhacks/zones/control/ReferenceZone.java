package airhacks.zones.control;

import java.time.DateTimeException;
import java.time.ZoneId;

public interface ReferenceZone {

    static ZoneId systemDefault() {
        return ZoneId.systemDefault();
    }

    static ZoneId of(String zoneId) {
        try {
            return ZoneId.of(zoneId);
        } catch (DateTimeException notAZone) {
            throw new IllegalArgumentException("'%s' is not a valid timezone id.".formatted(zoneId), notAZone);
        }
    }

}
