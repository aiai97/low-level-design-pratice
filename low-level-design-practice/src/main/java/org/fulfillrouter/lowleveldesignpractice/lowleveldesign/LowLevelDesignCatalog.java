package org.fulfillrouter.lowleveldesignpractice.lowleveldesign;

import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental.CarRentalProblem;
import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.moviebooking.MovieBookingProblem;
import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.parkinglot.ParkingLotProblem;
import org.fulfillrouter.lowleveldesignpractice.lowleveldesign.ratelimiter.RateLimiterProblem;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LowLevelDesignCatalog {

    private LowLevelDesignCatalog() {
    }

    public static Map<String, String> runAllDemos() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("Parking Lot", ParkingLotProblem.runDemo());
        result.put("Movie Ticket Booking", MovieBookingProblem.runDemo());
        result.put("Sliding Window Rate Limiter", RateLimiterProblem.runDemo());
        result.put("Car Rental System", CarRentalProblem.runDemo());
        return result;
    }
}
