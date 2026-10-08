package com.ridewise.strategy.matching;

import com.ridewise.model.Driver;
import com.ridewise.model.Rider;

import java.util.List;

public interface RideMatchingStrategy {
    List<Driver> findCandidates(Rider rider, List<Driver> drivers);
}
