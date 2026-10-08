# Design Patterns Examples

This module now includes runnable Java examples for 10 design patterns.

## Implemented patterns

- Singleton
- Factory
- Builder
- Adapter
- Decorator
- Strategy
- Observer
- Command
- Template Method
- Chain of Responsibility

## Source locations

- Main examples: `src/main/java/org/fulfillrouter/lowleveldesignpractice/designpatterns/`
- Test: `src/test/java/org/fulfillrouter/lowleveldesignpractice/designpatterns/DesignPatternsCatalogTest.java`

## Quick run

```bash
cd low-level-design-practice
./mvnw -q test
./mvnw -q -DskipTests exec:java -Dexec.mainClass="org.fulfillrouter.lowleveldesignpractice.designpatterns.DesignPatternsRunner"
```

If `exec:java` is not available in your environment, run `DesignPatternsRunner` directly from the IDE.

