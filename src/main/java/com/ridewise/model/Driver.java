package com.ridewise.model;

import java.util.Objects;

public class Driver {
    private final String id;
    private String name;
    private String address;
    private String licenseNumber;
    private VehicleType vehicleType;
    private String vehicleNumber;
    private Location currentLocation;
    private boolean available;
    private int activeRideCount;

    public Driver(String id, String name, String address, String licenseNumber,
                  VehicleType vehicleType, String vehicleNumber, Location currentLocation) {
        this.id = requireText(id, "Driver id");
        this.name = requireText(name, "Driver name");
        this.address = requireText(address, "Driver address");
        this.licenseNumber = requireText(licenseNumber, "License number");
        this.vehicleType = Objects.requireNonNull(vehicleType, "Vehicle type cannot be null");
        this.vehicleNumber = requireText(vehicleNumber, "Vehicle number");
        this.currentLocation = Objects.requireNonNull(currentLocation, "Current location cannot be null");
        this.available = true;
        this.activeRideCount = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getLicenseNumber() { return licenseNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public String getVehicleNumber() { return vehicleNumber; }
    public Location getCurrentLocation() { return currentLocation; }
    public boolean isAvailable() { return available; }
    public int getActiveRideCount() { return activeRideCount; }

    public void makeAvailable() {
        this.available = true;
    }

    public void makeUnavailable() {
        this.available = false;
    }

    public void updateLocation(Location location) {
        this.currentLocation = Objects.requireNonNull(location, "Location cannot be null");
    }

    public void assignRide() {
        if (!available) {
            throw new IllegalStateException("Driver " + id + " is not available");
        }
        activeRideCount++;
    }

    public void completeRide() {
        if (activeRideCount <= 0) {
            throw new IllegalStateException("Driver " + id + " has no active rides");
        }
        activeRideCount--;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
