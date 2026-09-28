package airhacks.hamburger.menu;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.hamburger.menu] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// The BC shall return every menu item with its name, calories, and price in cents.
        R1_1("R1.1", "The BC shall return every menu item with its name, calories, and price in cents."),
        /// The BC shall return at least one menu item.
        R1_2("R1.2", "The BC shall return at least one menu item."),
        /// When an existing item name is requested, the BC shall return that item with its calories and price in cents.
        R2_1("R2.1", "When an existing item name is requested, the BC shall return that item with its calories and price in cents."),
        /// If an unknown item name is requested, then the BC shall report that no such item exists.
        R2_2("R2.2", "If an unknown item name is requested, then the BC shall report that no such item exists.");

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
