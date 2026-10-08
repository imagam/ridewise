package com.ridewise;

import com.ridewise.exception.NoDriverAvailableException;
import com.ridewise.model.Driver;
import com.ridewise.model.Location;
import com.ridewise.model.RideStatus;
import com.ridewise.model.Rider;
import com.ridewise.model.VehicleType;
import com.ridewise.service.DriverService;
import com.ridewise.service.RideService;
import com.ridewise.service.RiderService;
import com.ridewise.strategy.fare.DefaultFareStrategy;
import com.ridewise.strategy.matching.LeastActiveDriverStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RideServiceTest {
    private RiderService riderService;
    private DriverService driverService;
    private RideService rideService;

    @BeforeEach
    void setUp() {
        riderService = new RiderService();
        driverService = new DriverService();
        riderService.registerRider(new Rider("R1", "John", new Location(17.3850, 78.4867)));
        driverService.registerDriver(new Driver("D1", "Alice", "Hyderabad", "L1", VehicleType.CAR, "V1",
                new Location(17.3860, 78.4870)));
        rideService = new RideService(riderService, driverService,
                new LeastActiveDriverStrategy(), new DefaultFareStrategy());
    }

    @Test
    void requestRideAssignsDriverAndUpdatesState() {
        var ride = rideService.requestRide("R1", new Location(17.3850, 78.4867),
                new Location(17.4000, 78.5000));

        assertEquals(RideStatus.ASSIGNED, ride.getStatus());
        assertEquals("D1", ride.getDriverId());
        assertEquals(1, driverService.findDriverById("D1").getActiveRideCount());
    }

    @Test
    void requestRideCancelsWhenNoDriverCanBeAssigned() {
        driverService.updateAvailability("D1", false);

        assertThrows(NoDriverAvailableException.class,
                () -> rideService.requestRide("R1", new Location(17.3850, 78.4867),
                        new Location(17.4000, 78.5000)));

        assertEquals(RideStatus.CANCELLED, rideService.findAllRides().getFirst().getStatus());
    }

    @Test
    void completingRideDecrementsActiveCountAndGeneratesReceipt() {
        var ride = rideService.requestRide("R1", new Location(17.3850, 78.4867),
                new Location(17.4000, 78.5000));

        var receipt = rideService.completeRide(ride.getId());

        assertEquals(RideStatus.COMPLETED, ride.getStatus());
        assertEquals(0, driverService.findDriverById("D1").getActiveRideCount());
        assertEquals(ride.getId(), receipt.rideId());
        assertTrue(receipt.amount() > 0);
    }

    @Test
    void cancellingAssignedRideReleasesActiveRideCount() {
        var ride = rideService.requestRide("R1", new Location(17.3850, 78.4867),
                new Location(17.4000, 78.5000));

        rideService.cancelRide(ride.getId());

        assertEquals(RideStatus.CANCELLED, ride.getStatus());
        assertEquals(0, driverService.findDriverById("D1").getActiveRideCount());
    }
}
