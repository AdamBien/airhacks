package airhacks.meetingschedule.hubtime.entity;

import java.time.ZoneId;

public enum Hub {
    SAN_FRANCISCO("San Francisco", "America/Los_Angeles"),
    NEW_YORK("New York", "America/New_York"),
    LONDON("London", "Europe/London"),
    FRANKFURT("Frankfurt", "Europe/Berlin"),
    VIENNA("Vienna", "Europe/Vienna"),
    DUBAI("Dubai", "Asia/Dubai"),
    MUMBAI("Mumbai", "Asia/Kolkata"),
    SINGAPORE("Singapore", "Asia/Singapore"),
    TOKYO("Tokyo", "Asia/Tokyo"),
    SYDNEY("Sydney", "Australia/Sydney");

    private final String city;
    private final ZoneId zone;

    Hub(String city, String zoneId) {
        this.city = city;
        this.zone = ZoneId.of(zoneId);
    }

    public String city() {
        return city;
    }

    public ZoneId zone() {
        return zone;
    }
}
