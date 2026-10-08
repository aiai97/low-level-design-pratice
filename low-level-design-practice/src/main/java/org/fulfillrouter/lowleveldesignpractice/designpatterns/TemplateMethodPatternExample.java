package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class TemplateMethodPatternExample {

    private TemplateMethodPatternExample() {
    }

    abstract static class DataExporter {

        final String export(String payload) {
            return open() + transform(payload) + close();
        }

        protected abstract String open();

        protected abstract String transform(String payload);

        protected abstract String close();
    }

    static final class JsonExporter extends DataExporter {

        @Override
        protected String open() {
            return "{";
        }

        @Override
        protected String transform(String payload) {
            return "\"data\":\"" + payload + "\"";
        }

        @Override
        protected String close() {
            return "}";
        }
    }

    public static String runDemo() {
        DataExporter exporter = new JsonExporter();
        return "TemplateMethod: " + exporter.export("ok");
    }
}

