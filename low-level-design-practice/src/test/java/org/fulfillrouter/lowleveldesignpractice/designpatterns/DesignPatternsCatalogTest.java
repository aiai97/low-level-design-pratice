package org.fulfillrouter.lowleveldesignpractice.designpatterns;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DesignPatternsCatalogTest {

    @Test
    void runAllShouldReturnAllExamples() {
        Map<String, String> result = DesignPatternsCatalog.runAll();

        assertEquals(10, result.size());
        assertTrue(result.containsKey("Singleton"));
        assertTrue(result.containsKey("Factory"));
        assertTrue(result.containsKey("Builder"));
        assertTrue(result.containsKey("Adapter"));
        assertTrue(result.containsKey("Decorator"));
        assertTrue(result.containsKey("Strategy"));
        assertTrue(result.containsKey("Observer"));
        assertTrue(result.containsKey("Command"));
        assertTrue(result.containsKey("Template Method"));
        assertTrue(result.containsKey("Chain of Responsibility"));

        result.values().forEach(value -> {
            assertNotNull(value);
            assertFalse(value.isBlank());
        });
    }
}

