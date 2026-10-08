package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class AdapterPatternExample {

    private AdapterPatternExample() {
    }

    interface PaymentGateway {
        String charge(int cents);
    }

    static final class LegacyBankApi {
        String executePayment(int amountInCents) {
            return "BANK_OK:" + amountInCents;
        }
    }

    static final class LegacyBankAdapter implements PaymentGateway {
        private final LegacyBankApi legacyBankApi;

        LegacyBankAdapter(LegacyBankApi legacyBankApi) {
            this.legacyBankApi = legacyBankApi;
        }

        @Override
        public String charge(int cents) {
            return legacyBankApi.executePayment(cents);
        }
    }

    public static String runDemo() {
        PaymentGateway gateway = new LegacyBankAdapter(new LegacyBankApi());
        return "Adapter: " + gateway.charge(2599);
    }
}

