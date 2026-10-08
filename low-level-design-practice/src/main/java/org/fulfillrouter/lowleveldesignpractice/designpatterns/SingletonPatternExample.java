package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class SingletonPatternExample {

    private SingletonPatternExample() {
    }

    private static final class IdGenerator {

        private static final IdGenerator INSTANCE = new IdGenerator();
        private long nextId = 1;

        private IdGenerator() {
        }

        private static IdGenerator getInstance() {
            return INSTANCE;
        }

        private synchronized long nextId() {
            return nextId++;
        }
    }

    public static String runDemo() {
        IdGenerator generator = IdGenerator.getInstance();
        long first = generator.nextId();
        long second = generator.nextId();
        return "Singleton: ids=" + first + "," + second;
    }
}

