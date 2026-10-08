package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

final class CarSearchService {
    private final CarRepository carRepository;
    private final InventoryManager inventoryManager;

    CarSearchService(CarRepository carRepository, InventoryManager inventoryManager) {
        this.carRepository = carRepository;
        this.inventoryManager = inventoryManager;
    }

    List<Car> search(SearchCriteria criteria) {
        CarSpecification spec = car -> true;
        if (criteria.carType() != null) {
            spec = spec.and(new CarTypeSpecification(criteria.carType()));
        }
        spec = spec.and(new PriceRangeSpecification(criteria.minPriceInCents(), criteria.maxPriceInCents()));
        if (criteria.dateRange() != null) {
            spec = spec.and(new AvailabilitySpecification(inventoryManager, criteria.dateRange()));
        }

        List<Car> result = new ArrayList<>();
        for (Car car : carRepository.findAll()) {
            if (spec.isSatisfiedBy(car)) {
                result.add(car);
            }
        }
        return result;
    }
}

final class ReservationService {
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryManager inventoryManager;
    private final PricingStrategy pricingStrategy;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final AtomicLong reservationSeq = new AtomicLong(1);
    private final AtomicLong paymentSeq = new AtomicLong(1);

    ReservationService(
            CarRepository carRepository,
            CustomerRepository customerRepository,
            ReservationRepository reservationRepository,
            PaymentRepository paymentRepository,
            InventoryManager inventoryManager,
            PricingStrategy pricingStrategy,
            PaymentStrategyFactory paymentStrategyFactory
    ) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.inventoryManager = inventoryManager;
        this.pricingStrategy = pricingStrategy;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    Optional<Reservation> createReservation(String customerId, String carId, DateRange dateRange, PaymentMethod paymentMethod) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new IllegalArgumentException("customer not found: " + customerId);
        }
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("car not found: " + carId));
        if (car.status() == CarStatus.MAINTENANCE) {
            return Optional.empty();
        }

        String reservationId = "R-" + reservationSeq.getAndIncrement();
        if (!inventoryManager.reserve(carId, reservationId, dateRange)) {
            return Optional.empty();
        }

        long total = pricingStrategy.quoteInCents(car, dateRange);
        Reservation reservation = new Reservation(
                reservationId,
                customerId,
                carId,
                dateRange,
                total,
                ReservationStatus.PENDING
        );
        reservationRepository.save(reservation);

        PaymentStrategy paymentStrategy = paymentStrategyFactory.get(paymentMethod);
        Payment payment = paymentStrategy.process("P-" + paymentSeq.getAndIncrement(), reservation, total);
        paymentRepository.save(payment);

        if (payment.status() != PaymentStatus.SUCCESS) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            inventoryManager.release(carId, reservationId);
            refreshCarStatus(carId);
            return Optional.empty();
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        refreshCarStatus(carId);
        return Optional.of(reservation);
    }

    boolean modifyReservation(String reservationId, DateRange newDateRange) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null || reservation.status() == ReservationStatus.CANCELLED) {
            return false;
        }
        Car car = carRepository.findById(reservation.carId()).orElseThrow();

        boolean moved = inventoryManager.moveReservation(reservation.carId(), reservation.id(), newDateRange);
        if (!moved) {
            return false;
        }

        reservation.setDateRange(newDateRange);
        reservation.setTotalPriceInCents(pricingStrategy.quoteInCents(car, newDateRange));
        return true;
    }

    boolean cancelReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null || reservation.status() == ReservationStatus.CANCELLED) {
            return false;
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        inventoryManager.release(reservation.carId(), reservation.id());
        refreshCarStatus(reservation.carId());
        return true;
    }

    private void refreshCarStatus(String carId) {
        carRepository.findById(carId).ifPresent(car -> {
            if (car.status() == CarStatus.MAINTENANCE) {
                return;
            }
            car.setStatus(inventoryManager.activeReservations(carId) > 0
                    ? CarStatus.RESERVED
                    : CarStatus.AVAILABLE);
        });
    }
}

