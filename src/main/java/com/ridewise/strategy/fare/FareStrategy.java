package com.ridewise.strategy.fare;

import com.ridewise.model.Ride;

public interface FareStrategy {
    double calculateFare(Ride ride);
}
