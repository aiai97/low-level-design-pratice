package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarRentalProblemTest {

    @Test
    void shouldAllowOnlyOneConcurrentReservationForSameCarAndRange() throws Exception {
        InMemoryCarRepository carRepository = new InMemoryCarRepository();
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();
        InventoryManager inventoryManager = new InventoryManager();

        Car car = new Car(
                "car-1",
                "Toyota",
                "Camry",
                2023,
                "TEST-001",
                CarType.SEDAN,
                8000,
                CarStatus.AVAILABLE
        );
        carRepository.save(car);
        inventoryManager.registerCar(car.id());

        customerRepository.save(new Customer("cust-1", "Alice", "a@example.com", "DL-100"));
        customerRepository.save(new Customer("cust-2", "Bob", "b@example.com", "DL-200"));

        ReservationService reservationService = new ReservationService(
                carRepository,
                customerRepository,
                reservationRepository,
                paymentRepository,
                inventoryManager,
                new DefaultPricingStrategy(),
                new PaymentStrategyFactory()
        );

        DateRange dateRange = new DateRange(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        Future<Boolean> first = executor.submit(() -> {
            startLatch.await();
            return reservationService.createReservation("cust-1", "car-1", dateRange, PaymentMethod.CARD).isPresent();
        });
        Future<Boolean> second = executor.submit(() -> {
            startLatch.await();
            return reservationService.createReservation("cust-2", "car-1", dateRange, PaymentMethod.CASH).isPresent();
        });

        startLatch.countDown();

        int successCount = 0;
        if (Boolean.TRUE.equals(first.get())) {
            successCount++;
        }
        if (Boolean.TRUE.equals(second.get())) {
            successCount++;
        }

        executor.shutdownNow();

        assertEquals(1, successCount);
        assertEquals(1, reservationRepository.findAll().size());
        assertEquals(1, paymentRepository.findAll().size());
        assertEquals(CarStatus.RESERVED, car.status());
    }
}

