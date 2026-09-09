package airhacks.meetingschedule.hubtime;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.meetingschedule.hubtime] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When invoked without a date-time, the BC shall render the table for the current moment.
        R1_1("R1.1", "When invoked without a date-time, the BC shall render the table for the current moment."),
        /// When invoked with a valid date-time, the BC shall interpret it in the system default time zone and render the table for that instant.
        R1_2("R1.2", "When invoked with a valid date-time, the BC shall interpret it in the system default time zone and render the table for that instant."),
        /// The BC shall list, for each major business hub (San Francisco, New York, London, Frankfurt, Vienna, Dubai, Mumbai, Singapore, Tokyo, Sydney), the hub's local date and time for the requested instant.
        R1_3("R1.3", "The BC shall list, for each major business hub (San Francisco, New York, London, Frankfurt, Vienna, Dubai, Mumbai, Singapore, Tokyo, Sydney), the hub's local date and time for the requested instant."),
        /// The BC shall mark each hub whose local time falls outside business hours (09:00–18:00 local).
        R1_4("R1.4", "The BC shall mark each hub whose local time falls outside business hours (09:00–18:00 local)."),
        /// If the date-time argument is unparseable, then the BC shall report an error naming the expected format and signal failure.
        R2_1("R2.1", "If the date-time argument is unparseable, then the BC shall report an error naming the expected format and signal failure.");

        private final String id;
        private final String statement;

        Rn(String id, String statement) {
            this.id = id;
            this.statement = statement;
        }

        public String statement() {
            return statement;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    Rn[] value();
}
