import airhacks.zones.control.ReferenceZone;

import java.time.ZoneId;

void main() {
    // no argument -> system default (FR-002)
    var byDefault = ReferenceZone.systemDefault();
    if (!byDefault.equals(ZoneId.systemDefault()))
        throw new AssertionError("expected system default %s but got %s".formatted(ZoneId.systemDefault(), byDefault));

    // valid id resolves to that zone (FR-003)
    var tokyo = ReferenceZone.of("Asia/Tokyo");
    if (!tokyo.equals(ZoneId.of("Asia/Tokyo")))
        throw new AssertionError("expected Asia/Tokyo but got " + tokyo);

    // invalid id is rejected, no silent fallback (FR-007)
    try {
        var bogus = ReferenceZone.of("Mars/Phobos");
        throw new AssertionError("expected IllegalArgumentException but got " + bogus);
    } catch (IllegalArgumentException expected) {
        if (!expected.getMessage().contains("Mars/Phobos"))
            throw new AssertionError("error message should name the bad value: " + expected.getMessage());
    }
}
