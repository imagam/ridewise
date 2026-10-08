package com.ridewise;

import com.ridewise.exception.NoDriverAvailableException;
import com.ridewise.model.Driver;
import com.ridewise.model.Location;
import com.ridewise.model.Ride;
import com.ridewise.model.Rider;
import com.ridewise.model.VehicleType;
import com.ridewise.service.DriverService;
import com.ridewise.service.RideService;
import com.ridewise.service.RiderService;
import com.ridewise.strategy.fare.DefaultFareStrategy;
import com.ridewise.strategy.fare.FareStrategy;
import com.ridewise.strategy.matching.NearestDriverStrategy;
import com.ridewise.strategy.matching.RideMatchingStrategy;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        RideMatchingStrategy matchingStrategy = new NearestDriverStrategy();
        FareStrategy fareStrategy = new DefaultFareStrategy();
        RideService rideService = new RideService(riderService, driverService, matchingStrategy, fareStrategy);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addRider(scanner, riderService);
                    case "2" -> addDriver(scanner, driverService);
                    case "3" -> viewAvailableDrivers(driverService);
                    case "4" -> updateDriverAvailability(scanner, driverService);
                    case "5" -> requestRide(scanner, rideService);
                    case "6" -> completeRide(scanner, rideService);
                    case "7" -> viewRides(rideService);
                    case "8" -> cancelRide(scanner, rideService);
                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException | NoDriverAvailableException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== RideWise ===");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Update Driver Availability");
        System.out.println("5. Request Ride");
        System.out.println("6. Complete Ride");
        System.out.println("7. View Rides");
        System.out.println("8. Cancel Ride");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addRider(Scanner scanner, RiderService service) {
        System.out.print("Rider ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        Location location = readLocation(scanner, "Current rider location");
        service.registerRider(new Rider(id, name, location));
        System.out.println("Rider registered.");
    }

    private static void addDriver(Scanner scanner, DriverService service) {
        System.out.print("Driver ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("License number: ");
        String license = scanner.nextLine();
        System.out.print("Vehicle type (BIKE/AUTO/CAR): ");
        VehicleType vehicleType = VehicleType.valueOf(scanner.nextLine().trim().toUpperCase());
        System.out.print("Vehicle number: ");
        String vehicleNumber = scanner.nextLine();
        Location location = readLocation(scanner, "Current driver location");
        service.registerDriver(new Driver(id, name, address, license, vehicleType, vehicleNumber, location));
        System.out.println("Driver registered and available.");
    }

    private static void viewAvailableDrivers(DriverService service) {
        var drivers = service.findAvailableDrivers();
        if (drivers.isEmpty()) {
            System.out.println("No available drivers.");
            return;
        }
        drivers.forEach(d -> System.out.printf("%s | %s | %s | active rides: %d%n",
                d.getId(), d.getName(), d.getVehicleType(), d.getActiveRideCount()));
    }

    private static void updateDriverAvailability(Scanner scanner, DriverService service) {
        System.out.print("Driver ID: ");
        String driverId = scanner.nextLine();
        System.out.print("Available? (true/false): ");
        boolean available = Boolean.parseBoolean(scanner.nextLine());
        service.updateAvailability(driverId, available);
        System.out.println("Driver availability updated.");
    }

    private static void requestRide(Scanner scanner, RideService service) {
        System.out.print("Rider ID: ");
        String riderId = scanner.nextLine();
        Location pickup = readLocation(scanner, "Pickup location");
        Location drop = readLocation(scanner, "Drop location");
        Ride ride = service.requestRide(riderId, pickup, drop);
        System.out.println("Ride assigned: " + ride.getId() + " to driver " + ride.getDriverId());
    }

    private static void completeRide(Scanner scanner, RideService service) {
        System.out.print("Ride ID: ");
        String rideId = scanner.nextLine();
        var receipt = service.completeRide(rideId);
        System.out.printf("Ride completed. Receipt %s | Amount: %.2f%n",
                receipt.invoiceNumber(), receipt.amount());
    }

    private static void cancelRide(Scanner scanner, RideService service) {
        System.out.print("Ride ID: ");
        String rideId = scanner.nextLine();
        service.cancelRide(rideId);
        System.out.println("Ride cancelled.");
    }

    private static void viewRides(RideService service) {
        var rides = service.findAllRides();
        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }
        rides.forEach(r -> System.out.printf("%s | rider=%s | driver=%s | status=%s | distance=%.2f km%n",
                r.getId(), r.getRiderId(), r.getDriverId(), r.getStatus(), r.getDistanceKm()));
    }

    private static Location readLocation(Scanner scanner, String label) {
        System.out.println(label + ":");
        System.out.print("  Latitude: ");
        double latitude = Double.parseDouble(scanner.nextLine());
        System.out.print("  Longitude: ");
        double longitude = Double.parseDouble(scanner.nextLine());
        return new Location(latitude, longitude);
    }
}
