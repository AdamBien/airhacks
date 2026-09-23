package airhacks.qmp.fleet.control;

import static airhacks.qmp.fleet.Requirement.Rn.R1_1;
import static airhacks.qmp.fleet.Requirement.Rn.R1_2;
import static airhacks.qmp.fleet.Requirement.Rn.R1_3;
import static airhacks.qmp.fleet.Requirement.Rn.R2_1;
import static airhacks.qmp.fleet.Requirement.Rn.R2_2;
import static airhacks.qmp.fleet.Requirement.Rn.R3_1;
import static airhacks.qmp.fleet.Requirement.Rn.R3_2;
import static airhacks.qmp.fleet.Requirement.Rn.R4_1;
import static airhacks.qmp.fleet.Requirement.Rn.R4_2;
import static airhacks.qmp.fleet.Requirement.Rn.R4_3;
import static airhacks.qmp.fleet.Requirement.Rn.R4_4;
import static airhacks.qmp.fleet.Requirement.Rn.R5_1;
import static airhacks.qmp.fleet.Requirement.Rn.R5_2;
import static airhacks.qmp.fleet.Requirement.Rn.R5_3;
import static airhacks.qmp.fleet.Requirement.Rn.R5_4;
import static airhacks.qmp.fleet.entity.BikeStatus.ALLOCATED;
import static airhacks.qmp.fleet.entity.BikeStatus.AVAILABLE;
import static airhacks.qmp.fleet.entity.BikeStatus.WITHDRAWN;
import static airhacks.qmp.fleet.entity.BikeType.BIKE;
import static airhacks.qmp.fleet.entity.BikeType.E_BIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.qmp.fleet.Requirement;
import airhacks.qmp.fleet.Requirement.Rn;
import airhacks.qmp.fleet.entity.Bike;
import airhacks.qmp.fleet.entity.BikeStatus;
import airhacks.qmp.fleet.entity.BikeType;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

class BikesTest {

    Bikes bikes;

    @BeforeEach
    void freshFleet() {
        this.bikes = new Bikes();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("registerBikeCases")
    void registerBike(Rn requirement, String id, String type, boolean alreadyRegistered, Class<? extends Exception> expectedFailure) {
        if (alreadyRegistered) {
            this.bikes.registerBike(id, "bike");
        }
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> this.bikes.registerBike(id, type), requirement.statement());
            return;
        }
        var bike = this.bikes.registerBike(id, type);
        assertEquals(AVAILABLE, bike.status(), requirement.statement());
        assertEquals(List.of(bike), this.bikes.listAvailableBikes(Optional.empty()), requirement.statement());
    }

    static Stream<Arguments> registerBikeCases() {
        return Stream.of(
                arguments(R1_1, "duke-1", "e-bike", false, null),
                arguments(R1_2, "duke-2", "unicycle", false, BadRequestException.class),
                arguments(R1_3, "duke-3", "bike", true, BikeConflict.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listAvailableBikesCases")
    void listAvailableBikes(Rn requirement, Optional<BikeType> type, List<String> expectedIds) {
        mixedFleet();
        var ids = this.bikes.listAvailableBikes(type).stream().map(Bike::id).toList();
        assertEquals(expectedIds, ids, requirement.statement());
    }

    static Stream<Arguments> listAvailableBikesCases() {
        return Stream.of(
                arguments(R2_1, Optional.empty(), List.of("available-bike", "available-ebike")),
                arguments(R2_2, Optional.of(E_BIKE), List.of("available-ebike")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("countBikesCases")
    void countBikes(Rn requirement, Optional<BikeType> type, long expectedCount) {
        mixedFleet();
        assertEquals(expectedCount, this.bikes.countBikes(type), requirement.statement());
    }

    static Stream<Arguments> countBikesCases() {
        return Stream.of(
                arguments(R3_1, Optional.empty(), 3L),
                arguments(R3_2, Optional.of(BIKE), 2L));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("withdrawAndRestoreCases")
    void withdrawAndRestore(Rn requirement, BikeStatus initialStatus, Function<Bikes, Bike> act, BikeStatus expectedStatus, Class<? extends Exception> expectedFailure) {
        if (initialStatus != null) {
            bikeIn(initialStatus);
        }
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> act.apply(this.bikes), requirement.statement());
            return;
        }
        assertEquals(expectedStatus, act.apply(this.bikes).status(), requirement.statement());
    }

    static Stream<Arguments> withdrawAndRestoreCases() {
        Function<Bikes, Bike> withdraw = bikes -> bikes.withdrawBike("subject");
        Function<Bikes, Bike> restore = bikes -> bikes.restoreBike("subject");
        return Stream.of(
                arguments(R4_1, AVAILABLE, withdraw, WITHDRAWN, null),
                arguments(R4_2, ALLOCATED, withdraw, null, BikeConflict.class),
                arguments(R4_3, WITHDRAWN, restore, AVAILABLE, null),
                arguments(R4_4, null, withdraw, null, NotFoundException.class));
    }

    @Test
    @Requirement(R4_4)
    void restoreUnknownBike() {
        assertThrows(NotFoundException.class, () -> this.bikes.restoreBike("ghost"), R4_4.statement());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allocateAndReleaseCases")
    void allocateAndRelease(Rn requirement, BikeStatus initialStatus, Function<Bikes, Bike> act, BikeStatus expectedStatus, Class<? extends Exception> expectedFailure) {
        bikeIn(initialStatus);
        if (expectedFailure != null) {
            assertThrows(expectedFailure, () -> act.apply(this.bikes), requirement.statement());
            return;
        }
        var bike = act.apply(this.bikes);
        assertEquals(expectedStatus, bike.status(), requirement.statement());
        assertEquals(bike, this.bikes.bikes.get("subject"), requirement.statement());
    }

    static Stream<Arguments> allocateAndReleaseCases() {
        Function<Bikes, Bike> allocate = bikes -> bikes.allocateBike(E_BIKE);
        Function<Bikes, Bike> release = bikes -> bikes.releaseBike("subject");
        return Stream.of(
                arguments(R5_1, AVAILABLE, allocate, ALLOCATED, null),
                arguments(R5_2, ALLOCATED, allocate, null, BikeConflict.class),
                arguments(R5_3, ALLOCATED, release, AVAILABLE, null),
                arguments(R5_4, AVAILABLE, release, null, BikeConflict.class));
    }

    /// one e-bike with id `subject` in the given status
    void bikeIn(BikeStatus status) {
        this.bikes.registerBike("subject", "e-bike");
        switch (status) {
            case ALLOCATED -> this.bikes.allocateBike(E_BIKE);
            case WITHDRAWN -> this.bikes.withdrawBike("subject");
            case AVAILABLE -> { }
        }
    }

    /// two available (bike, e-bike), one allocated bike, one withdrawn e-bike
    void mixedFleet() {
        this.bikes.registerBike("available-bike", "bike");
        this.bikes.registerBike("available-ebike", "e-bike");
        this.bikes.registerBike("allocated-bike", "bike");
        this.bikes.registerBike("withdrawn-ebike", "e-bike");
        this.bikes.withdrawBike("withdrawn-ebike");
        this.bikes.transition("allocated-bike", Bike::allocated);
    }
}
