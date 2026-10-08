package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

interface CarSpecification {
    boolean isSatisfiedBy(Car car);

    default CarSpecification and(CarSpecification other) {
        return car -> this.isSatisfiedBy(car) && other.isSatisfiedBy(car);
    }
}

final class CarTypeSpecification implements CarSpecification {
    private final CarType expected;

    CarTypeSpecification(CarType expected) {
        this.expected = expected;
    }

    @Override
    public boolean isSatisfiedBy(Car car) {
        return car.carType() == expected;
    }
}

final class PriceRangeSpecification implements CarSpecification {
    private final Long min;
    private final Long max;

    PriceRangeSpecification(Long min, Long max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isSatisfiedBy(Car car) {
        long price = car.rentalPricePerDayInCents();
        if (min != null && price < min) {
            return false;
        }
        if (max != null && price > max) {
            return false;
        }
        return true;
    }
}

final class AvailabilitySpecification implements CarSpecification {
    private final InventoryManager inventoryManager;
    private final DateRange dateRange;

    AvailabilitySpecification(InventoryManager inventoryManager, DateRange dateRange) {
        this.inventoryManager = inventoryManager;
        this.dateRange = dateRange;
    }

    @Override
    public boolean isSatisfiedBy(Car car) {
        return car.status() != CarStatus.MAINTENANCE && inventoryManager.isAvailable(car.id(), dateRange);
    }
}

