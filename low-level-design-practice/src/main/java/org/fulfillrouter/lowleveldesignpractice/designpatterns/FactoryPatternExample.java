package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class FactoryPatternExample {

    private FactoryPatternExample() {
    }

    interface Notification {
        String send(String message);
    }

    static final class EmailNotification implements Notification {
        @Override
        public String send(String message) {
            return "EMAIL:" + message;
        }
    }

    static final class SmsNotification implements Notification {
        @Override
        public String send(String message) {
            return "SMS:" + message;
        }
    }

    static final class NotificationFactory {
        static Notification create(String channel) {
            if ("email".equalsIgnoreCase(channel)) {
                return new EmailNotification();
            }
            if ("sms".equalsIgnoreCase(channel)) {
                return new SmsNotification();
            }
            throw new IllegalArgumentException("Unknown channel: " + channel);
        }
    }

    public static String runDemo() {
        Notification email = NotificationFactory.create("email");
        Notification sms = NotificationFactory.create("sms");
        return "Factory: " + email.send("Order Created") + " | " + sms.send("Code 1234");
    }
}

