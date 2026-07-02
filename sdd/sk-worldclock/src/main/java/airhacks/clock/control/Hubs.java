package airhacks.clock.control;

import airhacks.clock.entity.Hub;

import java.util.List;

public interface Hubs {

    List<Hub> BUSINESS_HUBS = List.of(
            Hub.of("New York", "America/New_York"),
            Hub.of("London", "Europe/London"),
            Hub.of("Frankfurt", "Europe/Berlin"),
            Hub.of("Dubai", "Asia/Dubai"),
            Hub.of("Singapore", "Asia/Singapore"),
            Hub.of("Tokyo", "Asia/Tokyo"),
            Hub.of("Sydney", "Australia/Sydney"));

    static List<Hub> all() {
        return BUSINESS_HUBS;
    }

}
