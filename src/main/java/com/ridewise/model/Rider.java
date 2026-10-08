package com.ridewise.model;

import java.util.Objects;

public class Rider {
    private final String id;
    private String name;
    private Location location;

    public Rider(String id, String name, Location location) {
        this.id = requireText(id, "Rider id");
        this.name = requireText(name, "Rider name");
        this.location = Objects.requireNonNull(location, "Rider location cannot be null");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }

    public void updateProfile(String name, Location location) {
        this.name = requireText(name, "Rider name");
        this.location = Objects.requireNonNull(location, "Rider location cannot be null");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
