package airhacks.qmp.ordering.entity;

/// The lifecycle of a counter order. `ordering` sets `PLACED` and `CANCELLED`;
/// the remaining states are reserved for the preparation and hand-over BCs.
public enum OrderState {
    PLACED("placed"),
    IN_PREPARATION("in-preparation"),
    READY("ready"),
    HANDED_OVER("handed-over"),
    CANCELLED("cancelled");

    private final String label;

    OrderState(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    /// Open means the customer is still waiting for the food.
    public boolean isOpen() {
        return this != CANCELLED && this != HANDED_OVER;
    }
}
