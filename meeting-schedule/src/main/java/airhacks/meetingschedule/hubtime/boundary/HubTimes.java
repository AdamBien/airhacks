package airhacks.meetingschedule.hubtime.boundary;

import airhacks.meetingschedule.hubtime.Requirement;
import airhacks.meetingschedule.hubtime.control.HubTable;
import airhacks.meetingschedule.hubtime.control.MeetingTime;

import java.time.format.DateTimeParseException;

import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_1;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_2;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_3;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_4;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R2_1;

public interface HubTimes {

    @Requirement({R1_1, R1_2, R1_3, R1_4, R2_1})
    static int showHubTimes(String... args) {
        try {
            var meeting = MeetingTime.parse(args);
            IO.println(HubTable.render(meeting));
            return 0;
        } catch (DateTimeParseException invalid) {
            System.err.println("invalid date-time '%s' — expected format yyyy-MM-ddTHH:mm, e.g. 2026-09-15T14:00"
                    .formatted(invalid.getParsedString()));
            return 1;
        }
    }
}
