package airhacks.hamburger.beverages;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.hamburger.beverages] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// The BC shall return every beverage with its name, calories, and price in cents, in the shop's fixed order.
        R1_1("R1.1", "The BC shall return every beverage with its name, calories, and price in cents, in the shop's fixed order."),
        /// The BC shall return at least one beverage.
        R1_2("R1.2", "The BC shall return at least one beverage."),
        /// When an existing beverage name is requested, the BC shall return that beverage with its calories and price in cents.
        R2_1("R2.1", "When an existing beverage name is requested, the BC shall return that beverage with its calories and price in cents."),
        /// If an unknown beverage name is requested, then the BC shall report that no such beverage exists.
        R2_2("R2.2", "If an unknown beverage name is requested, then the BC shall report that no such beverage exists."),
        /// If a blank name is requested, then the BC shall reject the request.
        R2_3("R2.3", "If a blank name is requested, then the BC shall reject the request."),
        /// When the same name is requested again, the BC shall return the same beverage.
        R2_4("R2.4", "When the same name is requested again, the BC shall return the same beverage.");

        final String id;
        final String statement;

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
