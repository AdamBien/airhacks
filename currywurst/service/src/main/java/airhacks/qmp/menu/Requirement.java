package airhacks.qmp.menu;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.qmp.menu] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// The BC shall provide a catalog containing at least the items Currywurst mit Darm, Currywurst ohne Darm, Pommes, Currywurst mit Pommes, Cola, and Wasser, each with a positive price in cents.
        R1_1("R1.1", "The BC shall provide a catalog containing at least the items Currywurst mit Darm, Currywurst ohne Darm, Pommes, Currywurst mit Pommes, Cola, and Wasser, each with a positive price in cents."),
        /// When the menu is listed, the BC shall return every catalog item.
        R1_2("R1.2", "When the menu is listed, the BC shall return every catalog item."),
        /// The BC shall return an identical catalog on every listing.
        R1_3("R1.3", "The BC shall return an identical catalog on every listing."),
        /// When an item is requested by a name in the catalog, the BC shall return that item with its price.
        R2_1("R2.1", "When an item is requested by a name in the catalog, the BC shall return that item with its price."),
        /// If the requested name is not in the catalog, then the BC shall report the item as unknown.
        R2_2("R2.2", "If the requested name is not in the catalog, then the BC shall report the item as unknown.");

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
