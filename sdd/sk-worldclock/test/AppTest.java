import airhacks.App;

import java.time.ZoneId;

void main() {
    // no argument -> system default reference (US1)
    var byDefault = App.zoneFor();
    if (!byDefault.equals(ZoneId.systemDefault()))
        throw new AssertionError("expected system default but got " + byDefault);

    // one valid argument -> override reference (US2 / FR-003)
    var tokyo = App.zoneFor("Asia/Tokyo");
    if (!tokyo.equals(ZoneId.of("Asia/Tokyo")))
        throw new AssertionError("expected Asia/Tokyo but got " + tokyo);

    // invalid zone -> rejected (FR-007)
    try {
        App.zoneFor("Mars/Phobos");
        throw new AssertionError("expected IllegalArgumentException for invalid zone");
    } catch (IllegalArgumentException expected) {
        // ok
    }

    // too many arguments -> usage error (edge case)
    try {
        App.zoneFor("Asia/Tokyo", "Europe/London");
        throw new AssertionError("expected IllegalArgumentException for too many arguments");
    } catch (IllegalArgumentException expected) {
        if (!expected.getMessage().contains("Usage"))
            throw new AssertionError("expected a usage message but got: " + expected.getMessage());
    }
}
