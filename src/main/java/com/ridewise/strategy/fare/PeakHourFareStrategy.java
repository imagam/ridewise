package com.ridewise.strategy.fare;

import com.ridewise.model.Ride;

import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 50.0;
    private static final double PER_KM_RATE = 12.0;
    private static final double PEAK_MULTIPLIER = 1.5;

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = BASE_FARE + (PER_KM_RATE * ride.getDistanceKm());
        LocalTime time = ride.getRequestTime().toLocalTime();
        boolean peak = isPeakHour(time);
        return peak ? baseFare * PEAK_MULTIPLIER : baseFare;
    }

    private boolean isPeakHour(LocalTime time) {
        return !time.isBefore(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(10, 0))
                || !time.isBefore(LocalTime.of(17, 0)) && time.isBefore(LocalTime.of(20, 0));
    }
}
