package org.fulfillrouter.lowleveldesignpractice.lowleveldesign.carrental;

import java.util.EnumMap;
import java.util.Map;

interface PricingStrategy {
    long quoteInCents(Car car, DateRange dateRange);
}

final class DefaultPricingStrategy implements PricingStrategy {
    @Override
    public long quoteInCents(Car car, DateRange dateRange) {
        return car.rentalPricePerDayInCents() * dateRange.days();
    }
}

interface PaymentStrategy {
    Payment process(String paymentId, Reservation reservation, long amountInCents);
}

final class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public Payment process(String paymentId, Reservation reservation, long amountInCents) {
        return new Payment(paymentId, reservation.id(), amountInCents, PaymentMethod.CARD, PaymentStatus.SUCCESS);
    }
}

final class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public Payment process(String paymentId, Reservation reservation, long amountInCents) {
        return new Payment(paymentId, reservation.id(), amountInCents, PaymentMethod.CASH, PaymentStatus.SUCCESS);
    }
}

final class PaymentStrategyFactory {
    private final Map<PaymentMethod, PaymentStrategy> strategies = new EnumMap<>(PaymentMethod.class);

    PaymentStrategyFactory() {
        strategies.put(PaymentMethod.CARD, new CardPaymentStrategy());
        strategies.put(PaymentMethod.CASH, new CashPaymentStrategy());
    }

    PaymentStrategy get(PaymentMethod method) {
        PaymentStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("unsupported payment method: " + method);
        }
        return strategy;
    }
}

