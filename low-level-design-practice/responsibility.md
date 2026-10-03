# Common Java Classes: Responsibility → Class


## Input / API

- Receive HTTP requests → `Controller`
- Carry request parameters → `Request / DTO`
- Represent a command → `Command`

## Business

- Execute business logic → `Service`
- Represent core business concepts → `Domain / Entity`
- Represent business values → `Value Object`
- Represent business rules → `Policy / Rule`
- Represent a business workflow → `UseCase`

## Data

- Access the database → `Repository`
- Perform low-level database operations → `DAO`
- Represent database records → `Entity / Record`
- Convert between database objects and application objects → `Mapper`

## External Systems

- Call external APIs → `Client`
- Communicate with third-party systems → `Gateway`
- Adapt an external system to your application → `Adapter`

## Object Creation

- Create objects → `Factory`
- Create a family of related objects → `AbstractFactory`
- Build complex objects step by step → `Builder`
- Manage a single shared instance → `Singleton`

## Behavior

- Choose between different algorithms → `Strategy`
- Change behavior based on state → `State`
- Handle a specific event or situation → `Handler`
- Pass a request through a sequence of handlers → `Chain`
- Notify other objects when state changes → `Listener / Observer`

## Transformation

- Convert one object into another → `Mapper`
- Convert between data formats or types → `Converter`
- Format data for presentation → `Formatter`
- Serialize / deserialize data → `Serializer / Deserializer`

## Validation

- Validate input → `Validator`
- Validate business rules → `Rule / Policy`
- Check permissions → `Authorization / Permission`

## Error Handling

- Represent business errors → `BusinessException`
- Represent a missing resource → `NotFoundException`
- Represent invalid input → `ValidationException`
- Handle exceptions globally → `ExceptionHandler`

## Configuration

- Configure the application → `Config / Configuration`
- Configure the database → `DatabaseConfig`
- Configure security → `SecurityConfig`
- Configure external clients → `ClientConfig`

## Event / Messaging

- Represent something that happened → `Event`
- Publish events or messages → `Publisher / Producer`
- Consume messages → `Consumer`
- Handle events → `EventHandler`
- Represent a message → `Message`

## Scheduling

- Trigger scheduled tasks → `Scheduler`
- Execute background tasks → `Job / Task`
- Define when a task should run → `Trigger`

## Caching

- Store cached data → `Cache`
- Manage cached data → `CacheService`
- Define caching rules → `CachePolicy`

## Security

- Represent user identity → `Principal`
- Represent authentication → `Authentication`
- Check authorization → `Authorization`
- Represent permissions → `Permission`
- Represent roles → `Role`

## Logging / Monitoring

- Record logs → `Logger`
- Collect metrics → `Metrics`
- Monitor system state → `Monitor`
- Trace requests across services → `Tracer`

## Presentation / Output

- Return API data → `Response / DTO`
- Represent paginated results → `Page / PageResponse`
- Represent an operation result → `Result`
- Format output → `Formatter`

---

