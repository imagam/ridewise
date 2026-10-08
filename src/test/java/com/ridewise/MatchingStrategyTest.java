package com.ridewise;

import com.ridewise.model.Driver;
import com.ridewise.model.Location;
import com.ridewise.model.Rider;
import com.ridewise.model.VehicleType;
import com.ridewise.strategy.matching.LeastActiveDriverStrategy;
import com.ridewise.strategy.matching.NearestDriverStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingStrategyTest {
    private Driver driver(String id, double lat, double lon) {
        return new Driver(id, id, "Hyd", "L-" + id, VehicleType.CAR, "V-" + id,
                new Location(lat, lon));
    }

    @Test
    void nearestStrategyFiltersUnavailableAndRanksByDistance() {
        Rider rider = new Rider("R1", "Rider", new Location(17.3850, 78.4867));
        Driver near = driver("D1", 17.3860, 78.4870);
        Driver far = driver("D2", 17.5000, 78.6000);
        Driver unavailable = driver("D3", 17.3851, 78.4868);
        unavailable.makeUnavailable();

        List<Driver> result = new NearestDriverStrategy().findCandidates(rider,
                List.of(far, unavailable, near));

        assertEquals(List.of(near, far), result);
    }

    @Test
    void leastActiveStrategyRanksAvailableDriversByActiveRideCount() {
        Rider rider = new Rider("R1", "Rider", new Location(17.3850, 78.4867));
        Driver one = driver("D1", 17.4, 78.5);
        Driver three = driver("D3", 17.4, 78.5);
        Driver two = driver("D2", 17.4, 78.5);
        three.assignRide(); three.assignRide(); three.assignRide();
        two.assignRide(); two.assignRide();

        List<Driver> result = new LeastActiveDriverStrategy().findCandidates(rider,
                List.of(three, two, one));

        assertEquals(List.of(one, two, three), result);
    }
}
