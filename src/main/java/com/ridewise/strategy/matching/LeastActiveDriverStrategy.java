package com.ridewise.strategy.matching;

import com.ridewise.model.Driver;
import com.ridewise.model.Rider;

import java.util.Comparator;
import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {
    @Override
    public List<Driver> findCandidates(Rider rider, List<Driver> drivers) {
        return drivers.stream()
                .filter(Driver::isAvailable)
                .sorted(Comparator.comparingInt(Driver::getActiveRideCount)
                        .thenComparing(Driver::getId))
                .toList();
    }
}
