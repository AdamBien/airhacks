package airhacks.eshop.catalog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.eshop.catalog] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When products are listed without a category, the BC shall return every product.
        R1_1("R1.1", "When products are listed without a category, the BC shall return every product."),
        /// When products are listed with a category, the BC shall return only the products of that category.
        R1_2("R1.2", "When products are listed with a category, the BC shall return only the products of that category."),
        /// If products are listed with a category no product belongs to, then the BC shall return an empty listing.
        R1_3("R1.3", "If products are listed with a category no product belongs to, then the BC shall return an empty listing."),
        /// The BC shall include unavailable products in listings, flagged as unavailable.
        R1_4("R1.4", "The BC shall include unavailable products in listings, flagged as unavailable."),
        /// When a product is requested by the id of an offered product, the BC shall return that product.
        R2_1("R2.1", "When a product is requested by the id of an offered product, the BC shall return that product."),
        /// If a product is requested by an unknown id, then the BC shall reject the request.
        R2_2("R2.2", "If a product is requested by an unknown id, then the BC shall reject the request.");

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
