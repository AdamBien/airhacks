import airhacks.meetingschedule.hubtime.boundary.HubTimes;

import static airhacks.meetingschedule.hubtime.Requirement.Rn.R2_1;

void main() {
    var errBuffer = new ByteArrayOutputStream();
    var originalOut = System.out;
    var originalErr = System.err;
    System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    System.setErr(new PrintStream(errBuffer));
    int exit;
    try {
        exit = HubTimes.showHubTimes("tomorrowish");
    } finally {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    var err = errBuffer.toString();

    assert exit == 1 : "%s — %s — expected exit 1 but got %d".formatted(R2_1, R2_1.statement(), exit);
    assert err.contains("yyyy-MM-ddTHH:mm")
            : "%s — %s — error must name the expected format, but was: %s".formatted(R2_1, R2_1.statement(), err);
    assert err.contains("tomorrowish")
            : "%s — %s — error should echo the rejected input, but was: %s".formatted(R2_1, R2_1.statement(), err);
}
