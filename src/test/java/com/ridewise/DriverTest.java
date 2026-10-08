package com.ridewise;

import com.ridewise.model.Driver;
import com.ridewise.model.Location;
import com.ridewise.model.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {
    private Driver driver() {
        return new Driver("D1", "Alice", "Hyderabad", "L1", VehicleType.CAR, "TS01AB1234",
                new Location(17.3850, 78.4867));
    }

    @Test
    void driverStartsAvailableWithNoActiveRides() {
        Driver driver = driver();
        assertTrue(driver.isAvailable());
        assertEquals(0, driver.getActiveRideCount());
    }

    @Test
    void assigningAndCompletingRideUpdatesActiveCount() {
        Driver driver = driver();
        driver.assignRide();
        driver.assignRide();
        assertEquals(2, driver.getActiveRideCount());
        assertTrue(driver.isAvailable());

        driver.completeRide();
        assertEquals(1, driver.getActiveRideCount());
    }

    @Test
    void unavailableDriverCannotAcceptRide() {
        Driver driver = driver();
        driver.makeUnavailable();
        assertThrows(IllegalStateException.class, driver::assignRide);
    }
}
