package org.fulfillrouter.lowleveldesignpractice.lowleveldesign;

import java.util.Map;

public final class LowLevelDesignRunner {

    private LowLevelDesignRunner() {
    }

    public static void main(String[] args) {
        Map<String, String> demos = LowLevelDesignCatalog.runAllDemos();
        System.out.println("Low Level Design Problem Demos");
        System.out.println("==============================");
        for (Map.Entry<String, String> entry : demos.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

