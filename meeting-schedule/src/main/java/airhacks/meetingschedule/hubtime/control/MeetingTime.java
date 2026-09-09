package airhacks.meetingschedule.hubtime.control;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface MeetingTime {

    static ZonedDateTime parse(String... args) {
        var zone = ZoneId.systemDefault();
        if (args.length == 0) {
            return ZonedDateTime.now(zone);
        }
        return LocalDateTime.parse(args[0]).atZone(zone);
    }
}
