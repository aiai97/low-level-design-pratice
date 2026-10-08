package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

final class InventoryManager {
    private final Map<String, CarInventory> carInventories = new ConcurrentHashMap<>();

    void registerCar(String carId) {
        carInventories.computeIfAbsent(carId, ignored -> new CarInventory());
    }

    boolean isAvailable(String carId, DateRange dateRange) {
        CarInventory inventory = carInventories.get(carId);
        if (inventory == null) {
            return false;
        }
        inventory.lock.lock();
        try {
            return !hasOverlap(inventory.reservedDatesByReservationId, dateRange, null);
        } finally {
            inventory.lock.unlock();
        }
    }

    boolean reserve(String carId, String reservationId, DateRange dateRange) {
        CarInventory inventory = carInventories.get(carId);
        if (inventory == null) {
            return false;
        }
        inventory.lock.lock();
        try {
            if (hasOverlap(inventory.reservedDatesByReservationId, dateRange, null)) {
                return false;
            }
            inventory.reservedDatesByReservationId.put(reservationId, dateRange);
            return true;
        } finally {
            inventory.lock.unlock();
        }
    }

    boolean moveReservation(String carId, String reservationId, DateRange newDateRange) {
        CarInventory inventory = carInventories.get(carId);
        if (inventory == null) {
            return false;
        }
        inventory.lock.lock();
        try {
            if (!inventory.reservedDatesByReservationId.containsKey(reservationId)) {
                return false;
            }
            if (hasOverlap(inventory.reservedDatesByReservationId, newDateRange, reservationId)) {
                return false;
            }
            inventory.reservedDatesByReservationId.put(reservationId, newDateRange);
            return true;
        } finally {
            inventory.lock.unlock();
        }
    }

    void release(String carId, String reservationId) {
        CarInventory inventory = carInventories.get(carId);
        if (inventory == null) {
            return;
        }
        inventory.lock.lock();
        try {
            inventory.reservedDatesByReservationId.remove(reservationId);
        } finally {
            inventory.lock.unlock();
        }
    }

    int activeReservations(String carId) {
        CarInventory inventory = carInventories.get(carId);
        if (inventory == null) {
            return 0;
        }
        inventory.lock.lock();
        try {
            return inventory.reservedDatesByReservationId.size();
        } finally {
            inventory.lock.unlock();
        }
    }

    private static boolean hasOverlap(Map<String, DateRange> ranges, DateRange candidate, String ignoreReservationId) {
        for (Map.Entry<String, DateRange> entry : ranges.entrySet()) {
            if (Objects.equals(ignoreReservationId, entry.getKey())) {
                continue;
            }
            if (entry.getValue().overlaps(candidate)) {
                return true;
            }
        }
        return false;
    }

    private static final class CarInventory {
        private final ReentrantLock lock = new ReentrantLock();
        private final Map<String, DateRange> reservedDatesByReservationId = new HashMap<>();
    }
}

