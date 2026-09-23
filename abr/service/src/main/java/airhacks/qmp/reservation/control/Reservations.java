package airhacks.qmp.reservation.control;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

import airhacks.qmp.fleet.control.Bikes;
import airhacks.qmp.fleet.entity.BikeType;
import airhacks.qmp.reservation.entity.Customer;
import airhacks.qmp.reservation.entity.Reservation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

/// In-memory reservations. `redeemReservation` is the cross-BC contract used by `rental`.
@ApplicationScoped
public class Reservations {

    static final ZoneId AMSTERDAM = ZoneId.of("Europe/Amsterdam");

    @Inject
    Bikes bikes;

    Clock clock = Clock.system(AMSTERDAM);

    final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public synchronized Reservation reserveBike(Customer customer, String typeLabel, LocalDate pickupDay) {
        if (customer == null || !customer.isComplete()) {
            throw new BadRequestException("customer name and email are required");
        }
        var type = Bikes.typeOf(typeLabel);
        if (pickupDay == null) {
            throw new BadRequestException("pickup day is required");
        }
        if (pickupDay.isBefore(today())) {
            throw new BadRequestException("pickup day %s lies in the past".formatted(pickupDay));
        }
        if (openReservations(type, pickupDay) >= this.bikes.countBikes(Optional.of(type))) {
            throw new ReservationConflict("no %s left to reserve on %s".formatted(type.label(), pickupDay));
        }
        var reservation = Reservation.open(customer, type, pickupDay);
        this.reservations.put(reservation.id(), reservation);
        return reservation;
    }

    public Reservation cancelReservation(String id) {
        return transition(id, Reservation::cancelled);
    }

    public Reservation redeemReservation(String id) {
        var today = today();
        return transition(id, reservation -> reservation.redeemed(today));
    }

    public Reservation findReservation(String id) {
        return Optional.ofNullable(this.reservations.get(id))
                .orElseThrow(() -> new NotFoundException("unknown reservation: " + id));
    }

    public LocalDate today() {
        return LocalDate.now(this.clock);
    }

    public static LocalDate pickupDayOf(String isoDate) {
        if (isoDate == null || isoDate.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(isoDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("pickup day must be an ISO date: " + isoDate);
        }
    }

    long openReservations(BikeType type, LocalDate pickupDay) {
        return this.reservations.values().stream()
                .filter(reservation -> reservation.isOpenFor(type, pickupDay))
                .count();
    }

    Reservation transition(String id, UnaryOperator<Reservation> transition) {
        try {
            var next = this.reservations.computeIfPresent(id, (_, current) -> transition.apply(current));
            if (next == null) {
                throw new NotFoundException("unknown reservation: " + id);
            }
            return next;
        } catch (IllegalStateException e) {
            throw new ReservationConflict(e.getMessage());
        }
    }
}
