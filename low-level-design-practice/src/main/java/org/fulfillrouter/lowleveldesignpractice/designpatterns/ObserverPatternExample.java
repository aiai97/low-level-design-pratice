package org.fulfillrouter.lowleveldesignpractice.designpatterns;

import java.util.ArrayList;
import java.util.List;

public final class ObserverPatternExample {

    private ObserverPatternExample() {
    }

    interface Listener {
        void onStatusChanged(String status);
    }

    static final class Order {
        private final List<Listener> listeners = new ArrayList<>();

        void addListener(Listener listener) {
            listeners.add(listener);
        }

        void setStatus(String status) {
            for (Listener listener : listeners) {
                listener.onStatusChanged(status);
            }
        }
    }

    static final class RecordingListener implements Listener {
        private final String name;
        private final List<String> events = new ArrayList<>();

        RecordingListener(String name) {
            this.name = name;
        }

        @Override
        public void onStatusChanged(String status) {
            events.add(name + ":" + status);
        }

        String lastEvent() {
            return events.isEmpty() ? "" : events.get(events.size() - 1);
        }
    }

    public static String runDemo() {
        Order order = new Order();
        RecordingListener inventory = new RecordingListener("inventory");
        RecordingListener shipping = new RecordingListener("shipping");
        order.addListener(inventory);
        order.addListener(shipping);
        order.setStatus("PAID");
        return "Observer: " + inventory.lastEvent() + " | " + shipping.lastEvent();
    }
}

