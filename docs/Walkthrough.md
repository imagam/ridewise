# 2–3 Minute Walkthrough

1. Start with the domain model: Rider, Driver, Ride, Location, and FareReceipt.
2. Explain that Driver owns availability and active ride count.
3. Explain that RideService orchestrates the lifecycle but delegates matching and pricing algorithms to interfaces.
4. Show `RideMatchingStrategy` and its two implementations.
5. Show `FareStrategy` and its two implementations.
6. Run `mvn clean test`.
7. Run the console application, add a rider, add drivers, request a ride, and complete it.
8. Show the generated receipt and ride status.
