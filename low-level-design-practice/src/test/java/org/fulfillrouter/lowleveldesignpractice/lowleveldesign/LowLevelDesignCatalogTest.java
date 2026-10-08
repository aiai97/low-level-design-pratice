package org.fulfillrouter.lowleveldesignpractice.lowleveldesign;

import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental.CarRentalProblem;
import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.moviebooking.MovieBookingProblem;
import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.parkinglot.ParkingLotProblem;
import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.ratelimiter.RateLimiterProblem;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LowLevelDesignCatalogTest {

    @Test
    void runAllDemosShouldIncludeAllProblems() {
        Map<String, String> demos = LowLevelDesignCatalog.runAllDemos();

        assertEquals(4, demos.size());
        assertTrue(demos.containsKey("Parking Lot"));
        assertTrue(demos.containsKey("Movie Ticket Booking"));
        assertTrue(demos.containsKey("Sliding Window Rate Limiter"));
        assertTrue(demos.containsKey("Car Rental System"));
        demos.values().forEach(value -> {
            assertNotNull(value);
            assertFalse(value.isBlank());
        });
    }

    @Test
    void parkingLotShouldRespectCapacityAndUnpark() {
        ParkingLotProblem.ParkingLotService service = new ParkingLotProblem.ParkingLotService();
        service.addSpots(ParkingLotProblem.VehicleType.CAR, 1);

        ParkingLotProblem.Ticket ticket = service.park("car-1", ParkingLotProblem.VehicleType.CAR).orElseThrow();
        assertEquals(0, service.availableSpots(ParkingLotProblem.VehicleType.CAR));
        assertTrue(service.park("car-2", ParkingLotProblem.VehicleType.CAR).isEmpty());

        assertTrue(service.unpark(ticket.ticketId()));
        assertEquals(1, service.availableSpots(ParkingLotProblem.VehicleType.CAR));
    }

    @Test
    void movieBookingShouldPreventDoubleBooking() {
        MovieBookingProblem.Show show = new MovieBookingProblem.Show("show-1", 2);

        assertTrue(show.reserveSeat("u-1", "S1"));
        assertFalse(show.reserveSeat("u-2", "S1"));
        assertTrue(show.cancelSeat("u-1", "S1"));
        assertTrue(show.reserveSeat("u-2", "S1"));
    }

    @Test
    void rateLimiterShouldApplySlidingWindowRules() {
        RateLimiterProblem.SlidingWindowRateLimiter limiter =
                new RateLimiterProblem.SlidingWindowRateLimiter(2, 1000);

        assertTrue(limiter.allow("k", 0));
        assertTrue(limiter.allow("k", 100));
        assertFalse(limiter.allow("k", 200));
        assertTrue(limiter.allow("k", 1201));
    }

    @Test
    void carRentalDemoShouldProduceOutput() {
        String output = CarRentalProblem.runDemo();
        assertNotNull(output);
        assertFalse(output.isBlank());
    }
}
