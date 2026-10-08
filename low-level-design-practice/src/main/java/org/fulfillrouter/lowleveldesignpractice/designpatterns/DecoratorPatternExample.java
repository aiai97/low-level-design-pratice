package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class DecoratorPatternExample {

    private DecoratorPatternExample() {
    }

    interface Text {
        String render();
    }

    static final class PlainText implements Text {
        private final String value;

        PlainText(String value) {
            this.value = value;
        }

        @Override
        public String render() {
            return value;
        }
    }

    static class TextDecorator implements Text {
        protected final Text delegate;

        TextDecorator(Text delegate) {
            this.delegate = delegate;
        }

        @Override
        public String render() {
            return delegate.render();
        }
    }

    static final class BracketsDecorator extends TextDecorator {
        BracketsDecorator(Text delegate) {
            super(delegate);
        }

        @Override
        public String render() {
            return "[" + super.render() + "]";
        }
    }

    static final class UppercaseDecorator extends TextDecorator {
        UppercaseDecorator(Text delegate) {
            super(delegate);
        }

        @Override
        public String render() {
            return super.render().toUpperCase();
        }
    }

    public static String runDemo() {
        Text decorated = new UppercaseDecorator(new BracketsDecorator(new PlainText("hello")));
        return "Decorator: " + decorated.render();
    }
}

