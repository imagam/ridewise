package com.ridewise;

import com.ridewise.model.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationTest {
    @Test
    void sameLocationHasZeroDistance() {
        Location location = new Location(17.3850, 78.4867);
        assertEquals(0.0, location.distanceTo(location), 0.0001);
    }
}
