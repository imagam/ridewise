package com.ridewise.service;

import com.ridewise.exception.NotFoundException;
import com.ridewise.model.Driver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DriverService {
    private final Map<String, Driver> drivers = new LinkedHashMap<>();

    public Driver registerDriver(Driver driver) {
        if (drivers.containsKey(driver.getId())) {
            throw new IllegalArgumentException("Driver already exists: " + driver.getId());
        }
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Driver findDriverById(String driverId) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            throw new NotFoundException("Driver not found: " + driverId);
        }
        return driver;
    }

    public List<Driver> findAllDrivers() {
        return new ArrayList<>(drivers.values());
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = findDriverById(driverId);
        if (available) {
            driver.makeAvailable();
        } else {
            driver.makeUnavailable();
        }
    }

    public List<Driver> findAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .toList();
    }
}
