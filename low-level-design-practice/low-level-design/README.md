# Low Level Design Package

This package contains practical low-level design interview problems with requirements and runnable Java code.

Detailed requirement statements are available in `low-level-design/PROBLEM_STATEMENTS.md`.

## Location

- Source code: `src/main/java/org/fulfillrouter/lowleveldesignpractice/lowleveldesign/`
- Tests: `src/test/java/org/fulfillrouter/lowleveldesignpractice/lowleveldesign/`

## Problems Included

### 1) Parking Lot (`parkinglot/ParkingLotProblem.java`)

Requirements:
- Support multiple vehicle types (`BIKE`, `CAR`, `TRUCK`)
- Allocate the first available spot of the requested type
- Return a parking ticket for successful parking
- Allow unparking by ticket id
- Expose available spot count and active ticket count

### 2) Movie Ticket Booking (`moviebooking/MovieBookingProblem.java`)

Requirements:
- Initialize a show with fixed seat capacity
- Reserve a seat by seat id and user id
- Prevent double booking of the same seat
- Allow cancellation only by the same user
- Expose available seat count

### 3) Sliding Window Rate Limiter (`ratelimiter/RateLimiterProblem.java`)

Requirements:
- Limit requests by key in a fixed time window
- Reject requests beyond configured quota
- Auto-expire old timestamps
- Support deterministic tests by accepting `nowMillis`

### 4) Car Rental System (`carrental/CarRentalProblem.java`)

Requirements:
- Search cars by type, price, and availability window
- Keep customer profile and driver's license information
- Create, modify, and cancel reservations
- Process payments with strategy-based payment handlers
- Prevent overlapping reservations during concurrent requests

## Runner

Use `LowLevelDesignRunner` to run all demos.

```bash
cd low-level-design-practice
./mvnw -q test
```

From IDE, run:
- `org.fulfillrouter.lowleveldesignpractice.lowleveldesign.LowLevelDesignRunner`

If your local JDK is not 17+, Maven compile may fail because this module targets Java 17.
