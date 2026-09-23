package airhacks.qmp.reservation.control;

import static airhacks.qmp.reservation.Requirement.Rn.R1_1;
import static airhacks.qmp.reservation.Requirement.Rn.R1_2;
import static airhacks.qmp.reservation.Requirement.Rn.R1_3;
import static airhacks.qmp.reservation.Requirement.Rn.R1_4;
import static airhacks.qmp.reservation.Requirement.Rn.R1_5;
import static airhacks.qmp.reservation.Requirement.Rn.R2_1;
import static airhacks.qmp.reservation.Requirement.Rn.R2_2;
import static airhacks.qmp.reservation.Requirement.Rn.R3_1;
import static airhacks.qmp.reservation.Requirement.Rn.R3_2;
import static airhacks.qmp.reservation.Requirement.Rn.R3_3;
import static airhacks.qmp.reservation.Requirement.Rn.R4_1;
import static airhacks.qmp.reservation.Requirement.Rn.R5_1;
import static airhacks.qmp.reservation.Requirement.Rn.R5_2;
import static airhacks.qmp.reservation.entity.ReservationStatus.CANCELLED;
import static airhacks.qmp.reservation.entity.ReservationStatus.EXPIRED;
import static airhacks.qmp.reservation.entity.ReservationStatus.OPEN;
import static airhacks.qmp.reservation.entity.ReservationStatus.REDEEMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.fleet.control.Bikes;
import airhacks.qmp.reservation.Requirement;
import airhacks.qmp.reservation.Requirement.Rn;
import airhacks.qmp.reservation.entity.Customer;
import airhacks.qmp.reservation.entity.Reservation;
import airhacks.qmp.reservation.entity.ReservationStatus;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

class ReservationsTest {

    static final LocalDate TODAY = LocalDate.of(2026, 9, 23);
    static final Customer DUKE = new Customer("Duke", "duke@java.net");
    static final Customer ANONYMOUS = new Customer("Duke", null);

    Reservations reservations;

    /// a fleet with exactly one e-bike, and the clock frozen on TODAY
    @BeforeEach
    void oneEbikeFleet() {
        this.reservations = new Reservations();
        this.reservations.bikes = new Bikes();
        this.reservations.bikes.registerBike("only-ebike", "e-bike");
        this.reservations.clock = Clock.fixed(TODAY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("reserveBikeCases")
    void reserveBike(Rn requirement, Customer customer, String type, LocalDate pickupDay, boolean dayAlreadyFull, Class<? extends Exception> expectedFailure) {
        if (dayAlreadyFull) {
            this.reservations.reserveBike(DUKE, "e-bike", pickupDay);
        }
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> this.reservations.reserveBike(customer, type, pickupDay), requirement.statement());
            return;
        }
        var reservation = this.reservations.reserveBike(customer, type, pickupDay);
        assertEquals(OPEN, reservation.status(), requirement.statement());
        assertEquals(reservation, this.reservations.findReservation(reservation.id()), requirement.statement());
    }

    static Stream<Arguments> reserveBikeCases() {
        return Stream.of(
                arguments(R1_1, DUKE, "e-bike", TODAY.plusDays(1), false, null),
                arguments(R1_2, DUKE, "e-bike", TODAY.minusDays(1), false, BadRequestException.class),
                arguments(R1_3, DUKE, "unicycle", TODAY, false, BadRequestException.class),
                arguments(R1_4, ANONYMOUS, "e-bike", TODAY, false, BadRequestException.class),
                arguments(R1_5, DUKE, "e-bike", TODAY, true, ReservationConflict.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cancelReservationCases")
    void cancelReservation(Rn requirement, ReservationStatus initialStatus, ReservationStatus expectedStatus, Class<? extends Exception> expectedFailure) {
        var id = reservationIn(initialStatus, TODAY);
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> this.reservations.cancelReservation(id), requirement.statement());
            return;
        }
        assertEquals(expectedStatus, this.reservations.cancelReservation(id).status(), requirement.statement());
    }

    static Stream<Arguments> cancelReservationCases() {
        return Stream.of(
                arguments(R2_1, OPEN, CANCELLED, null),
                arguments(R2_2, REDEEMED, null, ReservationConflict.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("redeemReservationCases")
    void redeemReservation(Rn requirement, ReservationStatus initialStatus, LocalDate pickupDay, ReservationStatus expectedStatus, Class<? extends Exception> expectedFailure) {
        var id = reservationIn(initialStatus, pickupDay);
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> this.reservations.redeemReservation(id), requirement.statement());
            return;
        }
        assertEquals(expectedStatus, this.reservations.redeemReservation(id).status(), requirement.statement());
    }

    static Stream<Arguments> redeemReservationCases() {
        return Stream.of(
                arguments(R3_1, OPEN, TODAY, REDEEMED, null),
                arguments(R3_2, OPEN, TODAY.plusDays(1), null, ReservationConflict.class),
                arguments(R3_3, CANCELLED, TODAY, null, ReservationConflict.class));
    }

    @Test
    @Requirement(R4_1)
    void openReservationPastPickupDayIsExpired() {
        var id = reservationIn(OPEN, TODAY);
        this.reservations.clock = Clock.offset(this.reservations.clock, Duration.ofDays(1));
        var reservation = this.reservations.findReservation(id);
        assertEquals(OPEN, reservation.status(), "stored status stays open");
        assertEquals(EXPIRED, reservation.reportedStatus(this.reservations.today()), R4_1.statement());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("findReservationCases")
    void findReservation(Rn requirement, boolean exists, Class<? extends Exception> expectedFailure) {
        var id = exists ? reservationIn(CANCELLED, TODAY) : "ghost";
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> this.reservations.findReservation(id), requirement.statement());
            return;
        }
        var reservation = this.reservations.findReservation(id);
        assertEquals(id, reservation.id(), requirement.statement());
        assertEquals(CANCELLED, reservation.status(), requirement.statement());
    }

    static Stream<Arguments> findReservationCases() {
        return Stream.of(
                arguments(R5_1, true, null),
                arguments(R5_2, false, NotFoundException.class));
    }

    /// one e-bike reservation for DUKE on pickupDay, driven into the given status
    String reservationIn(ReservationStatus status, LocalDate pickupDay) {
        var id = this.reservations.reserveBike(DUKE, "e-bike", pickupDay).id();
        switch (status) {
            case CANCELLED -> this.reservations.cancelReservation(id);
            case REDEEMED -> this.reservations.redeemReservation(id);
            case OPEN, EXPIRED -> { }
        }
        return id;
    }
}
