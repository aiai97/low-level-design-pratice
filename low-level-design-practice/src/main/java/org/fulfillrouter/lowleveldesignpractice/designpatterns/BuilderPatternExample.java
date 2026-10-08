package org.fulfillrouter.lowleveldesignpractice.designpatterns;

public final class BuilderPatternExample {

    private BuilderPatternExample() {
    }

    static final class User {
        private final String id;
        private final String name;
        private final String role;

        private User(Builder builder) {
            this.id = builder.id;
            this.name = builder.name;
            this.role = builder.role;
        }

        static final class Builder {
            private String id;
            private String name;
            private String role = "viewer";

            Builder id(String value) {
                this.id = value;
                return this;
            }

            Builder name(String value) {
                this.name = value;
                return this;
            }

            Builder role(String value) {
                this.role = value;
                return this;
            }

            User build() {
                if (id == null || name == null) {
                    throw new IllegalStateException("id and name are required");
                }
                return new User(this);
            }
        }

        @Override
        public String toString() {
            return "User{id='" + id + "', name='" + name + "', role='" + role + "'}";
        }
    }

    public static String runDemo() {
        User admin = new User.Builder()
                .id("u-1")
                .name("Alice")
                .role("admin")
                .build();
        return "Builder: " + admin;
    }
}

