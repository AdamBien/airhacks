package airhacks.qmp.yieldplanning;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.qmp.yieldplanning] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When a date, a planned tonnage, and a stone size are submitted for a day without a plan, the BC shall store them as that day's plan.
        R1_1("R1.1", "When a date, a planned tonnage, and a stone size are submitted for a day without a plan, the BC shall store them as that day's plan."),
        /// When the planned tonnage is zero, the BC shall accept it.
        R1_2("R1.2", "When the planned tonnage is zero, the BC shall accept it."),
        /// If a plan already exists for the submitted date, then the BC shall reject the submission.
        R1_3("R1.3", "If a plan already exists for the submitted date, then the BC shall reject the submission."),
        /// If the date is missing, the planned tonnage is missing, negative, or not a whole number, or the stone size is missing or not a whole number of millimetres above zero, then the BC shall reject the submission, naming every invalid part.
        R1_4("R1.4", "If the date is missing, the planned tonnage is missing, negative, or not a whole number, or the stone size is missing or not a whole number of millimetres above zero, then the BC shall reject the submission, naming every invalid part."),
        /// When a planned tonnage is submitted for a day that has a plan, the BC shall replace that day's planned tonnage.
        R2_1("R2.1", "When a planned tonnage is submitted for a day that has a plan, the BC shall replace that day's planned tonnage."),
        /// If the day has no plan, then the BC shall reject the revision as not found.
        R2_2("R2.2", "If the day has no plan, then the BC shall reject the revision as not found."),
        /// When a stone size is submitted for a day that has a plan, the BC shall replace that day's stone size.
        R2_4("R2.4", "When a stone size is submitted for a day that has a plan, the BC shall replace that day's stone size."),
        /// When a revision omits the planned tonnage or the stone size, the BC shall keep the omitted value unchanged.
        R2_5("R2.5", "When a revision omits the planned tonnage or the stone size, the BC shall keep the omitted value unchanged."),
        /// If a revision submits neither a planned tonnage nor a stone size, then the BC shall reject the revision, naming both.
        R2_6("R2.6", "If a revision submits neither a planned tonnage nor a stone size, then the BC shall reject the revision, naming both."),
        /// If a submitted planned tonnage is negative or not a whole number, or a submitted stone size is not a whole number of millimetres above zero, then the BC shall reject the revision, naming every invalid part.
        R2_7("R2.7", "If a submitted planned tonnage is negative or not a whole number, or a submitted stone size is not a whole number of millimetres above zero, then the BC shall reject the revision, naming every invalid part."),
        /// When deletion of a day's plan is requested, the BC shall remove that plan.
        R3_1("R3.1", "When deletion of a day's plan is requested, the BC shall remove that plan."),
        /// If the day has no plan, then the BC shall reject the deletion as not found.
        R3_2("R3.2", "If the day has no plan, then the BC shall reject the deletion as not found."),
        /// When the plan of a day is requested, the BC shall return its date, planned tonnage, and stone size, whether or not the day has started.
        R4_1("R4.1", "When the plan of a day is requested, the BC shall return its date, planned tonnage, and stone size, whether or not the day has started."),
        /// If the day has no plan, then the BC shall report that no plan exists.
        R4_2("R4.2", "If the day has no plan, then the BC shall report that no plan exists."),
        /// When plans are requested for a from–to date range, the BC shall return the plans dated within that range, both ends inclusive, ordered by date ascending.
        R5_1("R5.1", "When plans are requested for a from–to date range, the BC shall return the plans dated within that range, both ends inclusive, ordered by date ascending."),
        /// The BC shall omit days without a plan from a listed range.
        R5_2("R5.2", "The BC shall omit days without a plan from a listed range."),
        /// If the from date is after the to date, then the BC shall reject the request.
        R5_3("R5.3", "If the from date is after the to date, then the BC shall reject the request."),
        /// If the from date or the to date is missing, then the BC shall reject the request, naming every missing part.
        R5_4("R5.4", "If the from date or the to date is missing, then the BC shall reject the request, naming every missing part."),
        /// The BC shall treat a day as started from midnight in the mine's time zone.
        R6_1("R6.1", "The BC shall treat a day as started from midnight in the mine's time zone."),
        /// While a day has started, the BC shall reject planning it.
        R6_2("R6.2", "While a day has started, the BC shall reject planning it."),
        /// While a day has started, the BC shall reject revising its plan.
        R6_3("R6.3", "While a day has started, the BC shall reject revising its plan."),
        /// While a day has started, the BC shall reject deleting its plan.
        R6_4("R6.4", "While a day has started, the BC shall reject deleting its plan.");

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
