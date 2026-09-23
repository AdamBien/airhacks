package airhacks.qmp.reservation.entity;

import static airhacks.qmp.reservation.entity.ReservationStatus.CANCELLED;
import static airhacks.qmp.reservation.entity.ReservationStatus.EXPIRED;
import static airhacks.qmp.reservation.entity.ReservationStatus.OPEN;
import static airhacks.qmp.reservation.entity.ReservationStatus.REDEEMED;

import java.time.LocalDate;
import java.util.UUID;

import airhacks.qmp.fleet.entity.BikeType;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/// A bike type held for a customer on a pickup day. Handed over at most once.
public record Reservation(String id, Customer customer, BikeType type, LocalDate pickupDay, ReservationStatus status) {

    public static Reservation open(Customer customer, BikeType type, LocalDate pickupDay) {
        return new Reservation(UUID.randomUUID().toString(), customer, type, pickupDay, OPEN);
    }

    public boolean isOpen() {
        return this.status == OPEN;
    }

    public boolean isOpenFor(BikeType type, LocalDate pickupDay) {
        return isOpen() && this.type == type && this.pickupDay.equals(pickupDay);
    }

    public Reservation cancelled() {
        requireOpen("cancel");
        return new Reservation(this.id, this.customer, this.type, this.pickupDay, CANCELLED);
    }

    public Reservation redeemed(LocalDate today) {
        requireOpen("redeem");
        if (!this.pickupDay.equals(today)) {
            throw new IllegalStateException("reservation %s is for %s, not %s".formatted(this.id, this.pickupDay, today));
        }
        return new Reservation(this.id, this.customer, this.type, this.pickupDay, REDEEMED);
    }

    void requireOpen(String action) {
        if (!isOpen()) {
            throw new IllegalStateException("cannot %s reservation %s: it is %s".formatted(action, this.id, this.status.label()));
        }
    }

    /// The status as seen on `today`: an open reservation past its pickup day is expired.
    public ReservationStatus reportedStatus(LocalDate today) {
        return isOpen() && this.pickupDay.isBefore(today) ? EXPIRED : this.status;
    }

    public JsonObject toJSON(LocalDate today) {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("customer", this.customer.toJSON())
                .add("type", this.type.label())
                .add("pickupDay", this.pickupDay.toString())
                .add("status", reportedStatus(today).label())
                .build();
    }
}
