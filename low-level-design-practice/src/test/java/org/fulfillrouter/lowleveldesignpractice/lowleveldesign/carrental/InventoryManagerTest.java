package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryManagerTest {

    @Test
    void reserveAndReleaseShouldUpdateAvailabilityAndCount() {
        InventoryManager inventoryManager = new InventoryManager();
        inventoryManager.registerCar("car-1");

        DateRange initial = range(10, 12);
        DateRange overlap = range(11, 13);

        assertTrue(inventoryManager.isAvailable("car-1", initial));
        assertTrue(inventoryManager.reserve("car-1", "R-1", initial));
        assertFalse(inventoryManager.isAvailable("car-1", overlap));
        assertEquals(1, inventoryManager.activeReservations("car-1"));

        inventoryManager.release("car-1", "R-1");
        assertTrue(inventoryManager.isAvailable("car-1", overlap));
        assertEquals(0, inventoryManager.activeReservations("car-1"));
    }

    @Test
    void unregisteredCarShouldRejectInventoryOperations() {
        InventoryManager inventoryManager = new InventoryManager();
        DateRange dateRange = range(10, 12);

        assertFalse(inventoryManager.isAvailable("unknown-car", dateRange));
        assertFalse(inventoryManager.reserve("unknown-car", "R-1", dateRange));
        assertFalse(inventoryManager.moveReservation("unknown-car", "R-1", dateRange));
        assertEquals(0, inventoryManager.activeReservations("unknown-car"));
    }

    @Test
    void moveReservationShouldBlockOverlapAndAllowNonOverlap() {
        InventoryManager inventoryManager = new InventoryManager();
        inventoryManager.registerCar("car-1");

        assertTrue(inventoryManager.reserve("car-1", "R-1", range(10, 12)));
        assertTrue(inventoryManager.reserve("car-1", "R-2", range(15, 16)));

        assertFalse(inventoryManager.moveReservation("car-1", "R-1", range(16, 17)));
        assertTrue(inventoryManager.moveReservation("car-1", "R-1", range(13, 14)));
        assertEquals(2, inventoryManager.activeReservations("car-1"));
    }

    @Test
    void releasingUnknownReservationShouldBeNoOp() {
        InventoryManager inventoryManager = new InventoryManager();
        inventoryManager.registerCar("car-1");

        assertTrue(inventoryManager.reserve("car-1", "R-1", range(10, 12)));
        inventoryManager.release("car-1", "R-404");

        assertEquals(1, inventoryManager.activeReservations("car-1"));
        assertFalse(inventoryManager.isAvailable("car-1", range(11, 11)));
    }

    private static DateRange range(int startDay, int endDay) {
        return new DateRange(LocalDate.of(2026, 10, startDay), LocalDate.of(2026, 10, endDay));
    }
}
