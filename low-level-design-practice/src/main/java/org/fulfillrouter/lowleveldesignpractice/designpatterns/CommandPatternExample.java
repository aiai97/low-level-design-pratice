package org.fulfillrouter.lowleveldesignpractice.designpatterns;

import java.util.ArrayList;
import java.util.List;

public final class CommandPatternExample {

    private CommandPatternExample() {
    }

    interface Command {
        void execute();
    }

    static final class TaskRunner {
        private final List<Command> queue = new ArrayList<>();

        void add(Command command) {
            queue.add(command);
        }

        int runAll() {
            for (Command command : queue) {
                command.execute();
            }
            return queue.size();
        }
    }

    public static String runDemo() {
        StringBuilder audit = new StringBuilder();
        TaskRunner runner = new TaskRunner();
        runner.add(() -> audit.append("validate>"));
        runner.add(() -> audit.append("charge>"));
        runner.add(() -> audit.append("notify"));
        int executed = runner.runAll();
        return "Command: executed=" + executed + ", flow=" + audit;
    }
}

