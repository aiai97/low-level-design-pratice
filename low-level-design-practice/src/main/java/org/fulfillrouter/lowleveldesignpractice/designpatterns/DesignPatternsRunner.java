package org.fulfillrouter.lowleveldesignpractice.designpatterns;

import java.util.Map;

public final class DesignPatternsRunner {

    private DesignPatternsRunner() {
    }

    public static void main(String[] args) {
        Map<String, String> demos = DesignPatternsCatalog.runAll();
        System.out.println("Design Pattern Demos");
        System.out.println("====================");
        for (Map.Entry<String, String> entry : demos.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

