package com.ridewise.strategy.fare;

import com.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 50.0;
    private static final double PER_KM_RATE = 12.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (PER_KM_RATE * ride.getDistanceKm());
    }
}
