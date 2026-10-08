package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

interface CarRepository {
    void save(Car car);

    Optional<Car> findById(String carId);

    List<Car> findAll();
}

interface CustomerRepository {
    void save(Customer customer);

    Optional<Customer> findById(String customerId);
}

interface ReservationRepository {
    void save(Reservation reservation);

    Optional<Reservation> findById(String reservationId);

    List<Reservation> findAll();
}

interface PaymentRepository {
    void save(Payment payment);

    List<Payment> findAll();
}

final class InMemoryCarRepository implements CarRepository {
    private final Map<String, Car> cars = new ConcurrentHashMap<>();

    @Override
    public void save(Car car) {
        cars.put(car.id(), car);
    }

    @Override
    public Optional<Car> findById(String carId) {
        return Optional.ofNullable(cars.get(carId));
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }
}

final class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();

    @Override
    public void save(Customer customer) {
        customers.put(customer.id(), customer);
    }

    @Override
    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }
}

final class InMemoryReservationRepository implements ReservationRepository {
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }
}

final class InMemoryPaymentRepository implements PaymentRepository {
    private final List<Payment> payments = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(Payment payment) {
        payments.add(payment);
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }
}

