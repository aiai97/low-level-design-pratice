package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class StrategyPatternExample {

    private StrategyPatternExample() {
    }

    interface DiscountStrategy {
        int apply(int subtotal);
    }

    static final class NoDiscount implements DiscountStrategy {
        @Override
        public int apply(int subtotal) {
            return subtotal;
        }
    }

    static final class VipDiscount implements DiscountStrategy {
        @Override
        public int apply(int subtotal) {
            return subtotal - 500;
        }
    }

    static final class CheckoutService {
        private DiscountStrategy discountStrategy;

        CheckoutService(DiscountStrategy discountStrategy) {
            this.discountStrategy = discountStrategy;
        }

        void setDiscountStrategy(DiscountStrategy discountStrategy) {
            this.discountStrategy = discountStrategy;
        }

        int total(int subtotal) {
            return discountStrategy.apply(subtotal);
        }
    }

    public static String runDemo() {
        CheckoutService checkout = new CheckoutService(new NoDiscount());
        int regular = checkout.total(5000);
        checkout.setDiscountStrategy(new VipDiscount());
        int vip = checkout.total(5000);
        return "Strategy: regular=" + regular + ", vip=" + vip;
    }
}

