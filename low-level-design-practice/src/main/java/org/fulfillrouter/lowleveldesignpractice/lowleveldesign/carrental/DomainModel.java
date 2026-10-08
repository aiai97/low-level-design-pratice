package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

enum CarType {
    HATCHBACK,
    SEDAN,
    SUV,
    LUXURY
}

enum CarStatus {
    AVAILABLE,
    RESERVED,
    MAINTENANCE
}

enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}

enum PaymentMethod {
    CARD,
    CASH
}

enum PaymentStatus {
    SUCCESS,
    FAILED
}

abstract class BaseEntity {
    private final String id;

    protected BaseEntity(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        this.id = id;
    }

    String id() {
        return id;
    }
}

final class Car extends BaseEntity {
    private final String make;
    private final String model;
    private final int year;
    private final String licensePlate;
    private final CarType carType;
    private final long rentalPricePerDayInCents;
    private volatile CarStatus status;

    Car(
            String id,
            String make,
            String model,
            int year,
            String licensePlate,
            CarType carType,
            long rentalPricePerDayInCents,
            CarStatus status
    ) {
        super(id);
        if (make == null || make.isBlank()) {
            throw new IllegalArgumentException("make is required");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("model is required");
        }
        if (year < 1980) {
            throw new IllegalArgumentException("year is invalid");
        }
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("licensePlate is required");
        }
        if (rentalPricePerDayInCents <= 0) {
            throw new IllegalArgumentException("rental price must be > 0");
        }
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.carType = Objects.requireNonNull(carType);
        this.rentalPricePerDayInCents = rentalPricePerDayInCents;
        this.status = Objects.requireNonNull(status);
    }

    String make() {
        return make;
    }

    String model() {
        return model;
    }

    int year() {
        return year;
    }

    String licensePlate() {
        return licensePlate;
    }

    CarType carType() {
        return carType;
    }

    long rentalPricePerDayInCents() {
        return rentalPricePerDayInCents;
    }

    CarStatus status() {
        return status;
    }

    void setStatus(CarStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}

final class Customer extends BaseEntity {
    private final String name;
    private final String contact;
    private final String driverLicenseNumber;

    Customer(String id, String name, String contact, String driverLicenseNumber) {
        super(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("contact is required");
        }
        if (driverLicenseNumber == null || driverLicenseNumber.isBlank()) {
            throw new IllegalArgumentException("driver license is required");
        }
        this.name = name;
        this.contact = contact;
        this.driverLicenseNumber = driverLicenseNumber;
    }

    String name() {
        return name;
    }

    String contact() {
        return contact;
    }

    String driverLicenseNumber() {
        return driverLicenseNumber;
    }
}

final class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    DateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be on/after startDate");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    LocalDate startDate() {
        return startDate;
    }

    LocalDate endDate() {
        return endDate;
    }

    boolean overlaps(DateRange other) {
        return !(endDate.isBefore(other.startDate) || startDate.isAfter(other.endDate));
    }

    long days() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        return startDate + ".." + endDate;
    }
}

final class Reservation extends BaseEntity {
    private final String customerId;
    private final String carId;
    private DateRange dateRange;
    private long totalPriceInCents;
    private ReservationStatus status;

    Reservation(
            String id,
            String customerId,
            String carId,
            DateRange dateRange,
            long totalPriceInCents,
            ReservationStatus status
    ) {
        super(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.carId = Objects.requireNonNull(carId);
        this.dateRange = Objects.requireNonNull(dateRange);
        if (totalPriceInCents <= 0) {
            throw new IllegalArgumentException("totalPriceInCents must be > 0");
        }
        this.totalPriceInCents = totalPriceInCents;
        this.status = Objects.requireNonNull(status);
    }

    String customerId() {
        return customerId;
    }

    String carId() {
        return carId;
    }

    DateRange dateRange() {
        return dateRange;
    }

    long totalPriceInCents() {
        return totalPriceInCents;
    }

    ReservationStatus status() {
        return status;
    }

    void setDateRange(DateRange dateRange) {
        this.dateRange = Objects.requireNonNull(dateRange);
    }

    void setTotalPriceInCents(long totalPriceInCents) {
        if (totalPriceInCents <= 0) {
            throw new IllegalArgumentException("totalPriceInCents must be > 0");
        }
        this.totalPriceInCents = totalPriceInCents;
    }

    void setStatus(ReservationStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}

final class Payment extends BaseEntity {
    private final String reservationId;
    private final long amountInCents;
    private final PaymentMethod paymentMethod;
    private final PaymentStatus status;

    Payment(String id, String reservationId, long amountInCents, PaymentMethod paymentMethod, PaymentStatus status) {
        super(id);
        this.reservationId = Objects.requireNonNull(reservationId);
        this.amountInCents = amountInCents;
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.status = Objects.requireNonNull(status);
    }

    String reservationId() {
        return reservationId;
    }

    long amountInCents() {
        return amountInCents;
    }

    PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    PaymentStatus status() {
        return status;
    }
}

final class SearchCriteria {
    private final CarType carType;
    private final Long minPriceInCents;
    private final Long maxPriceInCents;
    private final DateRange dateRange;

    private SearchCriteria(Builder builder) {
        this.carType = builder.carType;
        this.minPriceInCents = builder.minPriceInCents;
        this.maxPriceInCents = builder.maxPriceInCents;
        this.dateRange = builder.dateRange;
    }

    CarType carType() {
        return carType;
    }

    Long minPriceInCents() {
        return minPriceInCents;
    }

    Long maxPriceInCents() {
        return maxPriceInCents;
    }

    DateRange dateRange() {
        return dateRange;
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private CarType carType;
        private Long minPriceInCents;
        private Long maxPriceInCents;
        private DateRange dateRange;

        Builder carType(CarType carType) {
            this.carType = carType;
            return this;
        }

        Builder minPriceInCents(Long minPriceInCents) {
            this.minPriceInCents = minPriceInCents;
            return this;
        }

        Builder maxPriceInCents(Long maxPriceInCents) {
            this.maxPriceInCents = maxPriceInCents;
            return this;
        }

        Builder dateRange(DateRange dateRange) {
            this.dateRange = dateRange;
            return this;
        }

        SearchCriteria build() {
            if (minPriceInCents != null && minPriceInCents < 0) {
                throw new IllegalArgumentException("min price must be >= 0");
            }
            if (maxPriceInCents != null && maxPriceInCents < 0) {
                throw new IllegalArgumentException("max price must be >= 0");
            }
            if (minPriceInCents != null && maxPriceInCents != null && minPriceInCents > maxPriceInCents) {
                throw new IllegalArgumentException("min price cannot be greater than max price");
            }
            return new SearchCriteria(this);
        }
    }
}

