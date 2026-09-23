package airhacks.qmp.reservation.entity;

/// `EXPIRED` is reported, never stored: an open reservation whose pickup day has passed.
public enum ReservationStatus {
    OPEN,
    CANCELLED,
    REDEEMED,
    EXPIRED;

    public String label() {
        return name().toLowerCase();
    }
}
