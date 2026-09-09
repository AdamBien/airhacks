package airhacks;

import airhacks.meetingschedule.hubtime.boundary.HubTimes;

public interface App {

    static void main(String... args) {
        System.exit(HubTimes.showHubTimes(args));
    }

}