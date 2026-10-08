package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class CarRentalProblem {

    private CarRentalProblem() {
    }

    public static String runDemo() {
        InMemoryCarRepository carRepository = new InMemoryCarRepository();
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
        InMemoryPaymentRepository paymentRepository = new InMemoryPaymentRepository();
        InventoryManager inventoryManager = new InventoryManager();

        Car car1 = new Car("car-1", "Toyota", "Camry", 2023, "ABC-101", CarType.SEDAN, 7000, CarStatus.AVAILABLE);
        Car car2 = new Car("car-2", "BMW", "X5", 2022, "ABC-202", CarType.SUV, 12000, CarStatus.AVAILABLE);
        carRepository.save(car1);
        carRepository.save(car2);
        inventoryManager.registerCar(car1.id());
        inventoryManager.registerCar(car2.id());

        Customer c1 = new Customer("cust-1", "Alice", "alice@example.com", "DL-001");
        Customer c2 = new Customer("cust-2", "Bob", "bob@example.com", "DL-002");
        customerRepository.save(c1);
        customerRepository.save(c2);

        ReservationService reservationService = new ReservationService(
                carRepository,
                customerRepository,
                reservationRepository,
                paymentRepository,
                inventoryManager,
                new DefaultPricingStrategy(),
                new PaymentStrategyFactory()
        );
        CarSearchService searchService = new CarSearchService(carRepository, inventoryManager);

        DateRange sameWindow = new DateRange(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12));
        SearchCriteria criteria = SearchCriteria.builder()
                .carType(CarType.SEDAN)
                .maxPriceInCents(9000L)
                .dateRange(sameWindow)
                .build();
        int availableBefore = searchService.search(criteria).size();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        Future<Boolean> f1 = executor.submit(() -> {
            startLatch.await();
            return reservationService.createReservation(c1.id(), car1.id(), sameWindow, PaymentMethod.CARD).isPresent();
        });
        Future<Boolean> f2 = executor.submit(() -> {
            startLatch.await();
            return reservationService.createReservation(c2.id(), car1.id(), sameWindow, PaymentMethod.CASH).isPresent();
        });

        startLatch.countDown();

        int successCount = 0;
        try {
            if (Boolean.TRUE.equals(f1.get())) {
                successCount++;
            }
            if (Boolean.TRUE.equals(f2.get())) {
                successCount++;
            }
        } catch (Exception e) {
            throw new RuntimeException("demo execution failed", e);
        } finally {
            executor.shutdownNow();
        }

        Reservation firstReservation = reservationRepository.findAll().stream().findFirst().orElse(null);
        boolean modified = false;
        boolean cancelled = false;
        if (firstReservation != null) {
            modified = reservationService.modifyReservation(
                    firstReservation.id(),
                    new DateRange(LocalDate.of(2026, 10, 13), LocalDate.of(2026, 10, 14))
            );
            cancelled = reservationService.cancelReservation(firstReservation.id());
        }

        int availableAfter = searchService.search(criteria).size();

        return "CarRental: availableBefore=" + availableBefore
                + ", concurrentSuccess=" + successCount
                + ", reservations=" + reservationRepository.findAll().size()
                + ", payments=" + paymentRepository.findAll().size()
                + ", modified=" + modified
                + ", cancelled=" + cancelled
                + ", availableAfter=" + availableAfter;
    }
}
