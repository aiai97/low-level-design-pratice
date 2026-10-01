# Java Design Patterns

## 1. Creational — Creating Objects

**Builder**  
→ Build a complex object step by step  
→ `StringBuilder`, `Stream.Builder`, Lombok `@Builder`

**Factory**  
→ Create objects without exposing the creation logic  
→ `List.of()`, `Set.of()`, `Map.of()`, `Calendar.getInstance()`

**Abstract Factory**  
→ Create a family of related objects  
→ `DocumentBuilderFactory`

**Singleton**  
→ Make sure only one shared instance exists  
→ Spring Singleton Bean, `Runtime.getRuntime()`

**Prototype**  
→ Create a new object by copying an existing object  
→ `Object.clone()`

---

## 2. Structural — Organizing Objects

**Adapter**  
→ Make incompatible interfaces work together  
→ `InputStreamReader`, `Arrays.asList()`

**Decorator**  
→ Add behavior by wrapping an object  
→ `BufferedInputStream`, `BufferedReader`, `DataInputStream`

**Facade**  
→ Provide a simple interface for a complex system  
→ JDBC, `java.util.logging.Logger`

**Proxy**  
→ Put an object in front of another object to control or extend access  
→ `java.lang.reflect.Proxy`, Spring AOP, `@Transactional`

**Composite**  
→ Treat a single object and a group of objects in the same way  
→ File / Directory, UI component trees

**Bridge**  
→ Separate an abstraction from its implementation  
→ JDBC: `DriverManager → Driver → Connection`

**Flyweight**  
→ Share reusable objects to reduce memory usage  
→ `Integer.valueOf()`, Integer cache, String Pool

---

## 3. Behavioral — How Objects Work Together

**Strategy**  
→ Encapsulate different algorithms behind the same interface  
→ `Comparator`, `java.util.function.*`

**Template Method**  
→ Define a fixed workflow while allowing some steps to vary  
→ `HttpServlet`, Spring `JdbcTemplate`, `RestTemplate`

**Observer**  
→ Notify multiple objects when something changes  
→ `PropertyChangeListener`, Spring Application Events

**Command**  
→ Turn an operation into an object  
→ `Runnable`, `Callable`, `ExecutorService`

**State**  
→ Change behavior based on the current state  
→ `Thread` states, HTTP request states, order state machines

**Chain of Responsibility**  
→ Pass a request through a chain of handlers  
→ Servlet Filters, Spring Security Filters, Spring Interceptors

**Iterator**  
→ Traverse a collection without exposing its internal structure  
→ `Iterator`, `Iterable`, `for-each`

**Mediator**  
→ Centralize communication between multiple objects  
→ Event systems, UI coordination

**Memento**  
→ Save an object's state so it can be restored later  
→ Undo / Redo implementations

**Visitor**  
→ Add operations to an object structure without changing the objects  
→ `FileVisitor`, `SimpleFileVisitor`

**Interpreter**  
→ Represent and execute a language or grammar  
→ `Pattern`, `Matcher`, expression / rule engines

---
