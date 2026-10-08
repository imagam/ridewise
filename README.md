# RideWise — Modular Ride-Sharing System

A Core Java ride-sharing system designed to demonstrate OOP, SOLID principles, Strategy Pattern, domain modeling, and separation of concerns.

## Features

- Register riders and drivers
- View available drivers
- Update driver availability
- Request a ride
- Match drivers using a pluggable matching strategy
- Try candidates in priority order and assign the first driver who accepts
- Track ride lifecycle: `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`
- Calculate fares using a pluggable fare strategy
- Generate a fare receipt on ride completion
- In-memory storage for a lightweight console application

## Design

### Domain model

- `Rider`: rider identity and current location
- `Driver`: driver profile, current location, availability, and active ride count
- `Ride`: ride relationship and lifecycle state
- `Location`: latitude/longitude value object with distance calculation
- `FareReceipt`: generated receipt information

### Services

- `RiderService`: rider registration and lookup
- `DriverService`: driver registration and lookup/query operations
- `RideService`: orchestrates the complete ride lifecycle

### Strategy abstractions

`RideMatchingStrategy` allows matching logic to change without modifying `RideService`.

Implementations:
- `NearestDriverStrategy`
- `LeastActiveDriverStrategy`

`FareStrategy` allows pricing logic to change without modifying `RideService`.

Implementations:
- `DefaultFareStrategy`
- `PeakHourFareStrategy`

## SOLID principles demonstrated

- **SRP:** domain objects own their state/invariants; services orchestrate use cases; strategies own algorithms.
- **OCP:** new matching/fare algorithms can be added as new strategy implementations.
- **LSP:** all matching strategies satisfy the same behavioral contract and return ordered candidates without mutating driver state.
- **ISP:** matching and fare concerns use small focused interfaces.
- **DIP:** `RideService` depends on `RideMatchingStrategy` and `FareStrategy` abstractions.

## Other design principles

- **DRY:** location distance calculation is centralized in `Location`; pricing/matching logic is not duplicated in `RideService`.
- **KISS:** the project uses in-memory collections rather than introducing a database/repository layer that is not required by the assignment.
- **YAGNI:** only required domain behavior is implemented for V1.
- **Law of Demeter:** services interact with domain objects through meaningful operations rather than directly manipulating their internal state.

## Important design decisions

1. `Driver.available` and `Driver.activeRideCount` are independent states. A driver may be available for another ride while already fulfilling other rides.
2. Matching strategies filter unavailable drivers and rank the remaining candidates.
3. A strategy only recommends candidates; `Driver.assignRide()` remains the authoritative state transition.
4. `Ride` stores rider/driver IDs rather than object references. A newly requested ride can therefore exist before a driver is assigned.
5. Distance is derived from pickup/drop locations instead of being duplicated as stored state.
6. A ride may be cancelled while `REQUESTED` or `ASSIGNED`. For V1, `CANCELLED` also represents the case where no candidate could be assigned.
7. Dynamic per-ride pricing-policy selection is intentionally outside V1. `FareStrategy` is injected into `RideService`, as required by the assignment.

## Fare assumptions

For demonstration purposes:

- Default fare = `50 + 12 × distanceKm`
- Peak fare = default fare × `1.5`
- Peak windows = 08:00–10:00 and 17:00–20:00 based on ride request time

These values are intentionally simple and can be changed inside the corresponding strategy without changing `RideService`.

## Build and test

Requires Java 21 and Maven 3.9+.

```bash
mvn clean test
```

## Run

```bash
mvn compile
java -cp target/classes com.ridewise.Main
```

## Example flow

1. Add a rider with a location.
2. Add one or more drivers with locations.
3. View available drivers.
4. Request a ride using pickup/drop coordinates.
5. RideWise ranks drivers using the configured matching strategy and assigns the first successful candidate.
6. Complete the ride using its ride ID.
7. A fare receipt is generated.

## Extension examples

New matching behavior can be added without modifying `RideService`:

```java
class HighestRatedDriverStrategy implements RideMatchingStrategy {
    // ranking logic
}
```

Similarly, a new fare algorithm only needs another `FareStrategy` implementation.
