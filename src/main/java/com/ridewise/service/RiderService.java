package com.ridewise.service;

import com.ridewise.exception.NotFoundException;
import com.ridewise.model.Rider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RiderService {
    private final Map<String, Rider> riders = new LinkedHashMap<>();

    public Rider registerRider(Rider rider) {
        if (riders.containsKey(rider.getId())) {
            throw new IllegalArgumentException("Rider already exists: " + rider.getId());
        }
        riders.put(rider.getId(), rider);
        return rider;
    }

    public Rider findRiderById(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            throw new NotFoundException("Rider not found: " + riderId);
        }
        return rider;
    }

    public List<Rider> findAllRiders() {
        return new ArrayList<>(riders.values());
    }
}
