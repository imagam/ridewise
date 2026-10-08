package com.ridewise.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ride {
    private final String id;
    private final String riderId;
    private String driverId;
    private final Location pickupLocation;
    private final Location dropLocation;
    private final LocalDateTime requestTime;
    private LocalDateTime assignTime;
    private LocalDateTime completionTime;
    private RideStatus status;

    public Ride(String id, String riderId, Location pickupLocation, Location dropLocation,
                LocalDateTime requestTime) {
        this.id = requireText(id, "Ride id");
        this.riderId = requireText(riderId, "Rider id");
        this.pickupLocation = Objects.requireNonNull(pickupLocation, "Pickup location cannot be null");
        this.dropLocation = Objects.requireNonNull(dropLocation, "Drop location cannot be null");
        this.requestTime = Objects.requireNonNull(requestTime, "Request time cannot be null");
        this.status = RideStatus.REQUESTED;
    }

    public String getId() { return id; }
    public String getRiderId() { return riderId; }
    public String getDriverId() { return driverId; }
    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropLocation() { return dropLocation; }
    public LocalDateTime getRequestTime() { return requestTime; }
    public LocalDateTime getAssignTime() { return assignTime; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public RideStatus getStatus() { return status; }

    public double getDistanceKm() {
        return pickupLocation.distanceTo(dropLocation);
    }

    public void assignDriver(String driverId, LocalDateTime assignedAt) {
        if (status != RideStatus.REQUESTED) {
            throw new IllegalStateException("Only requested rides can be assigned");
        }
        this.driverId = requireText(driverId, "Driver id");
        this.assignTime = Objects.requireNonNull(assignedAt, "Assignment time cannot be null");
        this.status = RideStatus.ASSIGNED;
    }

    public void complete(LocalDateTime completedAt) {
        if (status != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only assigned rides can be completed");
        }
        this.completionTime = Objects.requireNonNull(completedAt, "Completion time cannot be null");
        this.status = RideStatus.COMPLETED;
    }

    public void cancel() {
        if (status != RideStatus.REQUESTED && status != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only requested or assigned rides can be cancelled");
        }
        this.status = RideStatus.CANCELLED;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
