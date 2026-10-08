# RideWise Design Decisions

## 1. Ride is a first-class domain entity

A ride is more than a relationship between a rider and driver. It has its own identity, lifecycle, timestamps, locations, and status. It can exist in `REQUESTED` state before a driver is assigned.

## 2. Driver owns driver state

`available` and `activeRideCount` are Driver-owned state. `assignRide()` increments active rides and `completeRide()` decrements them. Availability is independently controlled by the Driver.

## 3. Matching is a policy

Driver selection is variable business logic, so it is represented by `RideMatchingStrategy`. Strategies return ordered candidates and do not mutate driver state.

## 4. Assignment is authoritative

Matching is advisory. Between matching and assignment, state can change. `Driver.assignRide()` therefore validates current availability before changing active ride count. RideService tries the next candidate if assignment fails.

## 5. Fare calculation is a policy

Fare calculation varies independently from ride orchestration, so `FareStrategy` abstracts it. `RideService` depends on the interface rather than a concrete pricing algorithm.

## 6. IDs in Ride

Ride stores `riderId` and `driverId` rather than object references. This keeps the ride model lightweight and naturally supports the period before driver assignment.

## 7. Derived distance

Distance is derived from pickup/drop locations instead of stored separately. This avoids duplicated state that could become inconsistent.

## 8. No repository layer in V1

The assignment does not require persistence. Adding repositories and a database would increase complexity without solving a current requirement. Services use in-memory collections for V1.

## 9. No separate RideMatchingService in V1

Matching and assignment form a coherent but currently small workflow inside RideService. A separate service is a reasonable future extraction if retries, reservations, driver response timeouts, or asynchronous matching make the workflow substantially more complex.

## 10. Dynamic pricing policy selection is parked

A future version could introduce a pricing-rule engine or another composition mechanism to dynamically select/apply multiple pricing policies. V1 follows the assignment's constructor-injected FareStrategy model instead of overengineering this area.
