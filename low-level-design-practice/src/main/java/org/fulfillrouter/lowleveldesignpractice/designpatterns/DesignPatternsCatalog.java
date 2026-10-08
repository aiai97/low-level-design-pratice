package org.fulfillrouter.lowleveldesignpractice.designpatterns;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DesignPatternsCatalog {

    private DesignPatternsCatalog() {
    }

    public static Map<String, String> runAll() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("Singleton", SingletonPatternExample.runDemo());
        result.put("Factory", FactoryPatternExample.runDemo());
        result.put("Builder", BuilderPatternExample.runDemo());
        result.put("Adapter", AdapterPatternExample.runDemo());
        result.put("Decorator", DecoratorPatternExample.runDemo());
        result.put("Strategy", StrategyPatternExample.runDemo());
        result.put("Observer", ObserverPatternExample.runDemo());
        result.put("Command", CommandPatternExample.runDemo());
        result.put("Template Method", TemplateMethodPatternExample.runDemo());
        result.put("Chain of Responsibility", ChainOfResponsibilityPatternExample.runDemo());
        return result;
    }
}

