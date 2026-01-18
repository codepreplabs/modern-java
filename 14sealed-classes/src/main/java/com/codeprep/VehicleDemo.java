package com.codeprep;

/**
 * Demonstration class for Sealed Classes
 * Run this class to see sealed classes in action
 */
public class VehicleDemo {

    public static void main(String[] args) {
        System.out.println("=== Sealed Classes Demo ===\n");

        // Create instances of permitted subclasses
        Vehicle car = new Car("Toyota", 2024, 4);
        Vehicle truck = new Truck("Ford", 2023, 5000.0);

        // Test 1: Basic usage
        System.out.println("Test 1: Basic Creation");
        System.out.println("Car: " + car.getBrand() + " (" + car.getYear() + ")");
        System.out.println("Truck: " + truck.getBrand() + " (" + truck.getYear() + ")");
        System.out.println();

        // Test 2: Polymorphism
        System.out.println("Test 2: Polymorphism");
        printVehicleDetails(car);
        printVehicleDetails(truck);
        System.out.println();

        // Test 3: Exhaustive pattern matching
        System.out.println("Test 3: Exhaustive Pattern Matching");
        System.out.println(getVehicleDescription(car));
        System.out.println(getVehicleDescription(truck));
        System.out.println();

        // Test 4: Switch expression with sealed classes
        System.out.println("Test 4: Switch Expression");
        System.out.println(getVehicleInfo(car));
        System.out.println(getVehicleInfo(truck));
        System.out.println();

        // Test 5: Verify sealed class properties
        System.out.println("Test 5: Sealed Class Properties");
        System.out.println("Vehicle is sealed: " + Vehicle.class.isSealed());
        System.out.println("Number of permitted subclasses: " +
                          Vehicle.class.getPermittedSubclasses().length);
        System.out.println("Car is final: " +
                          java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()));
        System.out.println("Truck is final: " +
                          java.lang.reflect.Modifier.isFinal(Truck.class.getModifiers()));
        System.out.println();

        System.out.println("=== All tests passed! ===");
    }

    // Demonstrates polymorphism with sealed classes
    private static void printVehicleDetails(Vehicle vehicle) {
        System.out.println("Vehicle Type: " + vehicle.getVehicleType());
        System.out.println("  Brand: " + vehicle.getBrand());
        System.out.println("  Year: " + vehicle.getYear());
    }

    // Demonstrates exhaustive pattern matching with if-else
    private static String getVehicleDescription(Vehicle vehicle) {
        if (vehicle instanceof Car car) {
            return "Car: " + car.getBrand() + " with " + car.getNumberOfDoors() + " doors";
        } else if (vehicle instanceof Truck truck) {
            return "Truck: " + truck.getBrand() + " with " +
                   truck.getCargoCapacity() + " kg cargo capacity";
        }
        // No else needed - compiler knows all cases are covered with sealed classes
        throw new AssertionError("Unknown vehicle type");
    }

    // Demonstrates switch expression with sealed classes
    private static String getVehicleInfo(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        return switch (vehicle) {
            case Car car ->
                "Car Info: " + car.getBrand() + " (" + car.getYear() + ") - " +
                car.getNumberOfDoors() + " doors";
            case Truck truck ->
                "Truck Info: " + truck.getBrand() + " (" + truck.getYear() + ") - " +
                truck.getCargoCapacity() + " kg capacity";
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicle);
        };
    }
}
