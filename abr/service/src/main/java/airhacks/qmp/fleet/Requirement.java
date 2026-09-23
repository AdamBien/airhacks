package airhacks.qmp.fleet;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.qmp.fleet] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When a bike of type bike or e-bike is registered, the BC shall add it to the fleet as available.
        R1_1("R1.1", "When a bike of type bike or e-bike is registered, the BC shall add it to the fleet as available."),
        /// If the type is neither bike nor e-bike, then the BC shall reject the registration.
        R1_2("R1.2", "If the type is neither bike nor e-bike, then the BC shall reject the registration."),
        /// If a bike with the same identifier is already registered, then the BC shall reject the registration.
        R1_3("R1.3", "If a bike with the same identifier is already registered, then the BC shall reject the registration."),
        /// The BC shall list only bikes that are available, neither allocated nor withdrawn.
        R2_1("R2.1", "The BC shall list only bikes that are available, neither allocated nor withdrawn."),
        /// Where a type is given, the BC shall list only available bikes of that type.
        R2_2("R2.2", "Where a type is given, the BC shall list only available bikes of that type."),
        /// The BC shall count the in-service bikes, available or allocated, excluding withdrawn ones.
        R3_1("R3.1", "The BC shall count the in-service bikes, available or allocated, excluding withdrawn ones."),
        /// Where a type is given, the BC shall count only in-service bikes of that type.
        R3_2("R3.2", "Where a type is given, the BC shall count only in-service bikes of that type."),
        /// While a bike is available, when it is withdrawn, the BC shall mark it withdrawn.
        R4_1("R4.1", "While a bike is available, when it is withdrawn, the BC shall mark it withdrawn."),
        /// If a bike allocated to a rental is withdrawn, then the BC shall reject the withdrawal.
        R4_2("R4.2", "If a bike allocated to a rental is withdrawn, then the BC shall reject the withdrawal."),
        /// While a bike is withdrawn, when it is restored, the BC shall mark it available.
        R4_3("R4.3", "While a bike is withdrawn, when it is restored, the BC shall mark it available."),
        /// If an unknown bike is withdrawn or restored, then the BC shall reject the request.
        R4_4("R4.4", "If an unknown bike is withdrawn or restored, then the BC shall reject the request."),
        /// While at least one bike of the requested type is available, when a bike is allocated, the BC shall mark one such bike allocated and hand it over.
        R5_1("R5.1", "While at least one bike of the requested type is available, when a bike is allocated, the BC shall mark one such bike allocated and hand it over."),
        /// If no bike of the requested type is available, then the BC shall reject the allocation.
        R5_2("R5.2", "If no bike of the requested type is available, then the BC shall reject the allocation."),
        /// While a bike is allocated, when it is released, the BC shall mark it available.
        R5_3("R5.3", "While a bike is allocated, when it is released, the BC shall mark it available."),
        /// If a bike that is not allocated is released, then the BC shall reject the release.
        R5_4("R5.4", "If a bike that is not allocated is released, then the BC shall reject the release.");

        private final String id;
        private final String statement;

        Rn(String id, String statement) {
            this.id = id;
            this.statement = statement;
        }

        public String statement() {
            return this.statement;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    Rn[] value();
}
