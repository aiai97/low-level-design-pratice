# Car Rental Problem

Entry point: `CarRentalProblem.java`

This exercise is now split into layered classes instead of one giant file:
- `DomainModel.java`: entities, enums, value objects, and `SearchCriteria` builder
- `Repositories.java`: repository contracts + in-memory implementations
- `Specifications.java`: Specification pattern for filtering
- `InventoryManager.java`: per-car lock based availability control
- `Strategies.java`: pricing/payment strategy + payment factory
- `Services.java`: search and reservation application services

Patterns used:
- **Builder pattern** for `SearchCriteria`
- **Specification pattern** for composable search filters
- **Strategy pattern** for pricing and payment
- **Factory pattern** for payment strategy selection
