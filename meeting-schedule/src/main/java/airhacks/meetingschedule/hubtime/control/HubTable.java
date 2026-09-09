package airhacks.meetingschedule.hubtime.control;

import airhacks.meetingschedule.hubtime.entity.Hub;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface HubTable {

    DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalTime BUSINESS_START = LocalTime.of(9, 0);
    LocalTime BUSINESS_END = LocalTime.of(18, 0);
    String OFF_HOURS_MARK = "off-hours";

    static String render(ZonedDateTime meeting) {
        return Arrays.stream(Hub.values())
                .map(hub -> row(hub, meeting))
                .collect(Collectors.joining("\n"));
    }

    static String row(Hub hub, ZonedDateTime meeting) {
        var local = meeting.withZoneSameInstant(hub.zone());
        var mark = withinBusinessHours(local.toLocalTime()) ? "" : "  " + OFF_HOURS_MARK;
        return "%-14s %s%s".formatted(hub.city(), local.format(LOCAL_DATE_TIME), mark);
    }

    static boolean withinBusinessHours(LocalTime time) {
        return !time.isBefore(BUSINESS_START) && !time.isAfter(BUSINESS_END);
    }
}
