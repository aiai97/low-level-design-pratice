package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.moviebooking;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MovieBookingProblem {

    /*
     * Problem: Design a Movie Ticket Booking service for a show.
     *
     * Requirements:
     * 1) Initialize a show with a fixed number of seats.
     * 2) Reserve a seat by user id and seat id.
     * 3) Prevent double booking on the same seat.
     * 4) Allow cancellation only by the same user who booked the seat.
     * 5) Expose the number of currently available seats.
     */

    private MovieBookingProblem() {
    }

    public static final class Show {
        private final String showId;
        private final Map<String, String> seatToUser = new LinkedHashMap<>();

        public Show(String showId, int seatCount) {
            if (seatCount <= 0) {
                throw new IllegalArgumentException("seatCount must be > 0");
            }
            this.showId = showId;
            for (int i = 1; i <= seatCount; i++) {
                seatToUser.put("S" + i, null);
            }
        }

        public synchronized boolean reserveSeat(String userId, String seatId) {
            if (!seatToUser.containsKey(seatId)) {
                return false;
            }
            if (seatToUser.get(seatId) != null) {
                return false;
            }
            seatToUser.put(seatId, userId);
            return true;
        }

        public synchronized boolean cancelSeat(String userId, String seatId) {
            if (!seatToUser.containsKey(seatId)) {
                return false;
            }
            if (!userId.equals(seatToUser.get(seatId))) {
                return false;
            }
            seatToUser.put(seatId, null);
            return true;
        }

        public synchronized int availableSeatCount() {
            int count = 0;
            for (String userId : seatToUser.values()) {
                if (userId == null) {
                    count++;
                }
            }
            return count;
        }

        public String showId() {
            return showId;
        }
    }

    public static String runDemo() {
        Show show = new Show("show-1", 3);
        boolean first = show.reserveSeat("u-1", "S1");
        boolean second = show.reserveSeat("u-2", "S1");
        boolean cancel = show.cancelSeat("u-1", "S1");
        boolean third = show.reserveSeat("u-2", "S1");

        return "MovieBooking: first=" + first
                + ", second=" + second
                + ", cancel=" + cancel
                + ", third=" + third
                + ", available=" + show.availableSeatCount();
    }
}


