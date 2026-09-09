import airhacks.meetingschedule.hubtime.Requirement;
import airhacks.meetingschedule.hubtime.boundary.HubTimes;

import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_1;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_2;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_3;
import static airhacks.meetingschedule.hubtime.Requirement.Rn.R1_4;

static final DateTimeFormatter TABLE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

// the spec's fixed hub list, restated here so a hub dropped from the code fails the test
static final List<HubZone> HUBS = List.of(
        new HubZone("San Francisco", ZoneId.of("America/Los_Angeles")),
        new HubZone("New York", ZoneId.of("America/New_York")),
        new HubZone("London", ZoneId.of("Europe/London")),
        new HubZone("Frankfurt", ZoneId.of("Europe/Berlin")),
        new HubZone("Vienna", ZoneId.of("Europe/Vienna")),
        new HubZone("Dubai", ZoneId.of("Asia/Dubai")),
        new HubZone("Mumbai", ZoneId.of("Asia/Kolkata")),
        new HubZone("Singapore", ZoneId.of("Asia/Singapore")),
        new HubZone("Tokyo", ZoneId.of("Asia/Tokyo")),
        new HubZone("Sydney", ZoneId.of("Australia/Sydney")));

record HubZone(String city, ZoneId zone) {}

record Run(int exit, String out, String err) {}

interface Check {
    void verify(Requirement.Rn req, Run run);
}

record Case(Requirement.Rn req, String[] args, Check check) {}

void main() {
    var fixedArg = "2026-09-15T14:00";
    var fixedMeeting = LocalDateTime.parse(fixedArg).atZone(ZoneId.systemDefault());
    var vienna = ZoneId.of("Europe/Vienna");
    var beforeStamp = ZonedDateTime.now(vienna).format(TABLE_FORMAT);

    var cases = List.of(
            new Case(R1_1, new String[]{}, (req, run) -> {
                var afterStamp = ZonedDateTime.now(vienna).format(TABLE_FORMAT);
                assert run.exit() == 0 : "%s — %s — expected exit 0 but got %d".formatted(req, req.statement(), run.exit());
                var line = hubLine(req, run.out(), "Vienna");
                assert line.contains(beforeStamp) || line.contains(afterStamp)
                        : "%s — %s — Vienna row should show the current moment (%s or %s) but was: %s"
                                .formatted(req, req.statement(), beforeStamp, afterStamp, line);
            }),
            new Case(R1_2, new String[]{fixedArg}, (req, run) -> {
                assert run.exit() == 0 : "%s — %s — expected exit 0 but got %d".formatted(req, req.statement(), run.exit());
                var expected = fixedMeeting.withZoneSameInstant(ZoneId.of("Europe/London")).format(TABLE_FORMAT);
                var line = hubLine(req, run.out(), "London");
                assert line.contains(expected)
                        : "%s — %s — London row should contain %s but was: %s".formatted(req, req.statement(), expected, line);
            }),
            new Case(R1_3, new String[]{fixedArg}, (req, run) -> {
                for (var hub : HUBS) {
                    var expected = fixedMeeting.withZoneSameInstant(hub.zone()).format(TABLE_FORMAT);
                    var line = hubLine(req, run.out(), hub.city());
                    assert line.contains(expected)
                            : "%s — %s — %s row should contain %s but was: %s"
                                    .formatted(req, req.statement(), hub.city(), expected, line);
                }
            }),
            new Case(R1_4, new String[]{fixedArg}, (req, run) -> {
                for (var hub : HUBS) {
                    var local = fixedMeeting.withZoneSameInstant(hub.zone()).toLocalTime();
                    var offHours = local.isBefore(LocalTime.of(9, 0)) || local.isAfter(LocalTime.of(18, 0));
                    var line = hubLine(req, run.out(), hub.city());
                    assert line.contains("off-hours") == offHours
                            : "%s — %s — %s at %s should%s be marked off-hours but row was: %s"
                                    .formatted(req, req.statement(), hub.city(), local, offHours ? "" : " not", line);
                }
            }));

    for (var c : cases) {
        var run = act(c.args());
        c.check().verify(c.req(), run);
    }
}

Run act(String... args) {
    var outBuffer = new ByteArrayOutputStream();
    var errBuffer = new ByteArrayOutputStream();
    var originalOut = System.out;
    var originalErr = System.err;
    System.setOut(new PrintStream(outBuffer));
    System.setErr(new PrintStream(errBuffer));
    try {
        var exit = HubTimes.showHubTimes(args);
        return new Run(exit, outBuffer.toString(), errBuffer.toString());
    } finally {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}

String hubLine(Requirement.Rn req, String out, String city) {
    return out.lines()
            .filter(line -> line.contains(city))
            .findFirst()
            .orElseThrow(() -> new AssertionError(
                    "%s — %s — no table row for %s in output: %s".formatted(req, req.statement(), city, out)));
}
