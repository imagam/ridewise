package com.ridewise.service;

import com.ridewise.exception.NoDriverAvailableException;
import com.ridewise.exception.NotFoundException;
import com.ridewise.model.Driver;
import com.ridewise.model.FareReceipt;
import com.ridewise.model.Location;
import com.ridewise.model.Ride;
import com.ridewise.model.RideStatus;
import com.ridewise.model.Rider;
import com.ridewise.strategy.fare.FareStrategy;
import com.ridewise.strategy.matching.RideMatchingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RideService {
    private final DriverService driverService;
    private final RiderService riderService;
    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;
    private final Map<String, Ride> rides = new LinkedHashMap<>();
    private final Map<String, FareReceipt> receipts = new LinkedHashMap<>();

    public RideService(RiderService riderService, DriverService driverService,
                       RideMatchingStrategy matchingStrategy, FareStrategy fareStrategy) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(String riderId, Location pickupLocation, Location dropLocation) {
        Rider rider = riderService.findRiderById(riderId);
        Ride ride = new Ride(UUID.randomUUID().toString(), rider.getId(), pickupLocation,
                dropLocation, LocalDateTime.now());
        rides.put(ride.getId(), ride);

        List<Driver> candidates = matchingStrategy.findCandidates(rider, driverService.findAllDrivers());
        for (Driver driver : candidates) {
            try {
                driver.assignRide();
                ride.assignDriver(driver.getId(), LocalDateTime.now());
                return ride;
            } catch (IllegalStateException ignored) {
                // Candidate may have become unavailable after matching. Try the next candidate.
            }
        }

        ride.cancel();
        throw new NoDriverAvailableException("No driver could be assigned to ride " + ride.getId());
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = findRideById(rideId);
        if (ride.getDriverId() == null) {
            throw new IllegalStateException("Ride has no assigned driver: " + rideId);
        }

        Driver driver = driverService.findDriverById(ride.getDriverId());
        driver.completeRide();
        ride.complete(LocalDateTime.now());

        double amount = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(
                "INV-" + UUID.randomUUID(), ride.getId(), amount, LocalDateTime.now());
        receipts.put(ride.getId(), receipt);
        return receipt;
    }

    public void cancelRide(String rideId) {
        Ride ride = findRideById(rideId);
        if (ride.getStatus() == RideStatus.ASSIGNED) {
            Driver driver = driverService.findDriverById(ride.getDriverId());
            driver.completeRide();
        }
        ride.cancel();
    }

    public Ride findRideById(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new NotFoundException("Ride not found: " + rideId);
        }
        return ride;
    }

    public List<Ride> findAllRides() {
        return new ArrayList<>(rides.values());
    }

    public FareReceipt findReceiptByRideId(String rideId) {
        FareReceipt receipt = receipts.get(rideId);
        if (receipt == null) {
            throw new NotFoundException("Receipt not found for ride: " + rideId);
        }
        return receipt;
    }
}
