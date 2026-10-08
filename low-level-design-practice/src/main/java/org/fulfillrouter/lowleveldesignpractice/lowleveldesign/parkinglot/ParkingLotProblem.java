package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.parkinglot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class ParkingLotProblem {

    /*
     * Problem: Design a Parking Lot service.
     *
     * Requirements:
     * 1) Support multiple vehicle types: BIKE, CAR, TRUCK.
     * 2) Add parking spots by vehicle type.
     * 3) Park a vehicle by allocating the first available spot of that type.
     * 4) Return a parking ticket for successful parking.
     * 5) Unpark by ticket id and release the spot back to inventory.
     * 6) Expose counts such as available spots and active tickets.
     */

    private ParkingLotProblem() {
    }

    public enum VehicleType {
        BIKE,
        CAR,
        TRUCK
    }

    public static final class Ticket {
        private final String ticketId;
        private final String vehicleId;
        private final VehicleType vehicleType;
        private final String spotId;

        Ticket(String ticketId, String vehicleId, VehicleType vehicleType, String spotId) {
            this.ticketId = ticketId;
            this.vehicleId = vehicleId;
            this.vehicleType = vehicleType;
            this.spotId = spotId;
        }

        public String ticketId() {
            return ticketId;
        }

        public String vehicleId() {
            return vehicleId;
        }

        public VehicleType vehicleType() {
            return vehicleType;
        }

        public String spotId() {
            return spotId;
        }

        @Override
        public String toString() {
            return "Ticket{id='" + ticketId + "', vehicle='" + vehicleId + "', spot='" + spotId + "'}";
        }
    }

    public static final class ParkingLotService {
        private final Map<VehicleType, Deque<String>> freeSpots = new EnumMap<>(VehicleType.class);
        private final Map<VehicleType, Integer> spotSequence = new EnumMap<>(VehicleType.class);
        private final Map<String, Ticket> activeTickets = new java.util.HashMap<>();
        private final AtomicLong sequence = new AtomicLong(1);

        public ParkingLotService() {
            for (VehicleType type : VehicleType.values()) {
                freeSpots.put(type, new ArrayDeque<>());
                spotSequence.put(type, 0);
            }
        }

        public void addSpots(VehicleType type, int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must be > 0");
            }
            Deque<String> queue = freeSpots.get(type);
            for (int i = 0; i < count; i++) {
                int next = spotSequence.get(type) + 1;
                spotSequence.put(type, next);
                String spotId = type.name().charAt(0) + "-" + next;
                queue.offerLast(spotId);
            }
        }

        public Optional<Ticket> park(String vehicleId, VehicleType type) {
            if (vehicleId == null || vehicleId.isBlank()) {
                throw new IllegalArgumentException("vehicleId is required");
            }
            Deque<String> queue = freeSpots.get(type);
            String spotId = queue.pollFirst();
            if (spotId == null) {
                return Optional.empty();
            }
            String ticketId = "T-" + sequence.getAndIncrement();
            Ticket ticket = new Ticket(ticketId, vehicleId, type, spotId);
            activeTickets.put(ticketId, ticket);
            return Optional.of(ticket);
        }

        public boolean unpark(String ticketId) {
            Ticket ticket = activeTickets.remove(ticketId);
            if (ticket == null) {
                return false;
            }
            freeSpots.get(ticket.vehicleType()).offerLast(ticket.spotId());
            return true;
        }

        public int availableSpots(VehicleType type) {
            return freeSpots.get(type).size();
        }

        public int activeTicketCount() {
            return activeTickets.size();
        }

        public List<String> snapshotActiveTickets() {
            List<String> values = new ArrayList<>();
            for (Ticket ticket : activeTickets.values()) {
                values.add(ticket.toString());
            }
            return values;
        }
    }

    public static String runDemo() {
        ParkingLotService service = new ParkingLotService();
        service.addSpots(VehicleType.CAR, 2);
        service.addSpots(VehicleType.BIKE, 1);

        Optional<Ticket> t1 = service.park("car-001", VehicleType.CAR);
        Optional<Ticket> t2 = service.park("car-002", VehicleType.CAR);
        Optional<Ticket> t3 = service.park("car-003", VehicleType.CAR);

        if (t1.isPresent()) {
            service.unpark(t1.get().ticketId());
        }

        return "ParkingLot: parked=" + (t1.isPresent() ? 1 : 0)
                + ", second=" + t2.isPresent()
                + ", third=" + t3.isPresent()
                + ", availableCar=" + service.availableSpots(VehicleType.CAR);
    }
}



