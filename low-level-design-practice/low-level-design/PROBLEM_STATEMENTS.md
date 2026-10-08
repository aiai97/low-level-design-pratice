# Low Level Design Problem Statements

This document defines interview-style requirements for the implemented low-level design exercises.

## 1) Parking Lot

Implementation: `src/main/java/org/fulfillrouter/lowleveldesignpractice/lowleveldesign/parkinglot/ParkingLotProblem.java`

### Requirements
- Support at least three vehicle types: `BIKE`, `CAR`, `TRUCK`.
- Add parking spots by vehicle type.
- Park a vehicle by allocating the first available spot of the requested type.
- Return a ticket containing `ticketId`, `vehicleId`, `vehicleType`, and `spotId`.
- Unpark by `ticketId` and return the spot to availability.
- Provide helper queries for available spots and active ticket count.

## 2) Movie Ticket Booking

Implementation: `src/main/java/org/fulfillrouter/lowleveldesignpractice/lowleveldesign/moviebooking/MovieBookingProblem.java`

### Requirements
- Create a show with a fixed seat capacity.
- Reserve a seat with `userId` and `seatId`.
- Prevent double booking of the same seat.
- Allow cancellation only by the user who booked that seat.
- Expose currently available seat count.
- Ensure seat reservation/cancellation is thread-safe.

## 3) Sliding Window Rate Limiter

Implementation: `src/main/java/org/fulfillrouter/lowleveldesignpractice/lowleveldesign/ratelimiter/RateLimiterProblem.java`

### Requirements
- Configure a max request count and window size (milliseconds).
- Enforce limits independently per key (user/client/api-key).
- Remove expired timestamps on each request.
- Reject requests when count in current window reaches max.
- Support deterministic tests by accepting explicit `nowMillis`.
- Keep operations thread-safe for shared in-memory usage.

## 4) Car Rental System

Implementation: `src/main/java/org/fulfillrouter/lowleveldesignpractice/lowleveldesign/carrental/CarRentalProblem.java`

### Requirements
- Browse/search cars by type, price range, and requested date range.
- Store car details (`make`, `model`, `year`, `licensePlate`, `rentalPricePerDay`).
- Manage customers with contact and driver's license information.
- Create, modify, and cancel reservations.
- Keep inventory consistent and prevent overlapping reservations per car.
- Process reservation payments through pluggable payment strategies.
- Handle concurrent booking attempts safely.
