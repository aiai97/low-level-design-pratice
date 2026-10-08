package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class ChainOfResponsibilityPatternExample {

    private ChainOfResponsibilityPatternExample() {
    }

    abstract static class Handler {
        private Handler next;

        Handler linkWith(Handler nextHandler) {
            this.next = nextHandler;
            return nextHandler;
        }

        final String handle(int amount) {
            if (canHandle(amount)) {
                return name();
            }
            if (next == null) {
                return "no-handler";
            }
            return next.handle(amount);
        }

        protected abstract boolean canHandle(int amount);

        protected abstract String name();
    }

    static final class SmallOrderHandler extends Handler {
        @Override
        protected boolean canHandle(int amount) {
            return amount < 1_000;
        }

        @Override
        protected String name() {
            return "small";
        }
    }

    static final class MediumOrderHandler extends Handler {
        @Override
        protected boolean canHandle(int amount) {
            return amount < 10_000;
        }

        @Override
        protected String name() {
            return "medium";
        }
    }

    static final class LargeOrderHandler extends Handler {
        @Override
        protected boolean canHandle(int amount) {
            return true;
        }

        @Override
        protected String name() {
            return "large";
        }
    }

    public static String runDemo() {
        Handler first = new SmallOrderHandler();
        first.linkWith(new MediumOrderHandler())
                .linkWith(new LargeOrderHandler());

        String firstRouting = first.handle(800);
        String secondRouting = first.handle(6_500);
        String thirdRouting = first.handle(35_000);

        return "Chain: " + firstRouting + "," + secondRouting + "," + thirdRouting;
    }
}

