package airhacks.qmp.ordering;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.qmp.ordering] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When an order with at least one valid line is placed, the BC shall create the order in state `placed` and return it with its order number.
        R1_1("R1.1", "When an order with at least one valid line is placed, the BC shall create the order in state `placed` and return it with its order number."),
        /// When an order is placed, the BC shall assign the next order number of the current day, starting at 1 for the first order of each day.
        R1_2("R1.2", "When an order is placed, the BC shall assign the next order number of the current day, starting at 1 for the first order of each day."),
        /// When an order is placed, the BC shall price each line as the item's menu price times its quantity and the order total as the sum of its lines, in cents.
        R1_3("R1.3", "When an order is placed, the BC shall price each line as the item's menu price times its quantity and the order total as the sum of its lines, in cents."),
        /// If the order has no lines, then the BC shall reject it.
        R1_4("R1.4", "If the order has no lines, then the BC shall reject it."),
        /// If any line names an item unknown to `menu`, then the BC shall reject the whole order.
        R1_5("R1.5", "If any line names an item unknown to `menu`, then the BC shall reject the whole order."),
        /// If any line has a quantity below 1, then the BC shall reject the whole order.
        R1_6("R1.6", "If any line has a quantity below 1, then the BC shall reject the whole order."),
        /// When an order is requested by the number of one of today's orders, the BC shall return it with its lines, total, and state.
        R2_1("R2.1", "When an order is requested by the number of one of today's orders, the BC shall return it with its lines, total, and state."),
        /// If the order number matches none of today's orders, then the BC shall report the order as not found.
        R2_2("R2.2", "If the order number matches none of today's orders, then the BC shall report the order as not found."),
        /// While an order is in state `placed`, when it is cancelled, the BC shall move it to state `cancelled`.
        R3_1("R3.1", "While an order is in state `placed`, when it is cancelled, the BC shall move it to state `cancelled`."),
        /// If an order that has left the `placed` state is cancelled, then the BC shall reject the cancellation and leave its state unchanged.
        R3_2("R3.2", "If an order that has left the `placed` state is cancelled, then the BC shall reject the cancellation and leave its state unchanged."),
        /// If the order number matches none of today's orders, then the BC shall report the order as not found.
        R3_3("R3.3", "If the order number matches none of today's orders, then the BC shall report the order as not found."),
        /// When open orders are listed, the BC shall return every one of today's orders whose state is neither `cancelled` nor `handed-over`, oldest first.
        R4_1("R4.1", "When open orders are listed, the BC shall return every one of today's orders whose state is neither `cancelled` nor `handed-over`, oldest first."),
        /// While today has no open order, the BC shall return an empty list.
        R4_2("R4.2", "While today has no open order, the BC shall return an empty list.");

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
