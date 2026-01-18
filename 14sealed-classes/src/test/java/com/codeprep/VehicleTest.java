package com.codeprep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class demonstrating Sealed Classes functionality in Java 17+
 *
 * This test showcases:
 * 1. Creation of sealed class hierarchy (Vehicle -> Car, Truck)
 * 2. Verification that only permitted subclasses can extend sealed class
 * 3. Exhaustive pattern matching with sealed classes
 * 4. Type checking and instanceof operations
 * 5. Runtime class hierarchy inspection
 */
public class VehicleTest {

    @Test
    @DisplayName("Test Car creation and properties")
    public void testCarCreation() {
        // Arrange & Act
        Car car = new Car("Toyota", 2024, 4);

        // Assert
        assertNotNull(car, "Car instance should not be null");
        assertEquals("Toyota", car.getBrand(), "Car brand should be Toyota");
        assertEquals(2024, car.getYear(), "Car year should be 2024");
        assertEquals(4, car.getNumberOfDoors(), "Car should have 4 doors");
        assertEquals("Car", car.getVehicleType(), "Vehicle type should be Car");
    }

    @Test
    @DisplayName("Test Truck creation and properties")
    public void testTruckCreation() {
        // Arrange & Act
        Truck truck = new Truck("Ford", 2023, 5000.0);

        // Assert
        assertNotNull(truck, "Truck instance should not be null");
        assertEquals("Ford", truck.getBrand(), "Truck brand should be Ford");
        assertEquals(2023, truck.getYear(), "Truck year should be 2023");
        assertEquals(5000.0, truck.getCargoCapacity(), "Truck cargo capacity should be 5000.0");
        assertEquals("Truck", truck.getVehicleType(), "Vehicle type should be Truck");
    }

    @Test
    @DisplayName("Test sealed class hierarchy - Car is a Vehicle")
    public void testCarIsVehicle() {
        // Arrange
        Vehicle car = new Car("Honda", 2024, 2);

        // Act & Assert
        assertTrue(car instanceof Vehicle, "Car should be an instance of Vehicle");
        assertTrue(car instanceof Car, "Car should be an instance of Car");
        assertFalse(car instanceof Truck, "Car should not be an instance of Truck");
    }

    @Test
    @DisplayName("Test sealed class hierarchy - Truck is a Vehicle")
    public void testTruckIsVehicle() {
        // Arrange
        Vehicle truck = new Truck("Volvo", 2023, 10000.0);

        // Act & Assert
        assertTrue(truck instanceof Vehicle, "Truck should be an instance of Vehicle");
        assertTrue(truck instanceof Truck, "Truck should be an instance of Truck");
        assertFalse(truck instanceof Car, "Truck should not be an instance of Car");
    }

    @Test
    @DisplayName("Test exhaustive pattern matching with sealed classes")
    public void testExhaustivePatternMatching() {
        // Arrange
        Vehicle car = new Car("BMW", 2024, 4);
        Vehicle truck = new Truck("Mercedes", 2023, 7500.0);

        // Act & Assert - Pattern matching with instanceof
        String carDescription = getVehicleDescription(car);
        String truckDescription = getVehicleDescription(truck);

        assertEquals("This is a Car made by BMW", carDescription,
            "Car description should match expected format");
        assertEquals("This is a Truck made by Mercedes with cargo capacity: 7500.0 kg",
            truckDescription, "Truck description should match expected format");
    }

    @Test
    @DisplayName("Test sealed class permits only specified subclasses")
    public void testSealedClassPermits() {

        // Act - Get permitted subclasses using reflection
        Class<?>[] permittedSubclasses = Vehicle.class.getPermittedSubclasses();

        // Assert
        assertNotNull(permittedSubclasses, "Permitted subclasses should not be null");
        assertEquals(2, permittedSubclasses.length,
            "Vehicle should permit exactly 2 subclasses");

        // Verify that Car and Truck are the only permitted subclasses
        boolean hasCarClass = false;
        boolean hasTruckClass = false;

        for (Class<?> subclass : permittedSubclasses) {
            if (subclass.equals(Car.class)) {
                hasCarClass = true;
            } else if (subclass.equals(Truck.class)) {
                hasTruckClass = true;
            }
        }

        assertTrue(hasCarClass, "Car should be a permitted subclass");
        assertTrue(hasTruckClass, "Truck should be a permitted subclass");
    }

    @Test
    @DisplayName("Test that Vehicle class is sealed")
    public void testVehicleIsSealed() {
        // Act & Assert
        assertTrue(Vehicle.class.isSealed(), "Vehicle class should be sealed");
    }

    @Test
    @DisplayName("Test that Car class is final (required for sealed subclass)")
    public void testCarIsFinal() {
        // Act & Assert
        assertTrue(java.lang.reflect.Modifier.isFinal(Car.class.getModifiers()),
            "Car class should be final as a permitted subclass of sealed Vehicle");
    }

    @Test
    @DisplayName("Test that Truck class is final (required for sealed subclass)")
    public void testTruckIsFinal() {
        // Act & Assert
        assertTrue(java.lang.reflect.Modifier.isFinal(Truck.class.getModifiers()),
            "Truck class should be final as a permitted subclass of sealed Vehicle");
    }

    @Test
    @DisplayName("Test polymorphism with sealed classes")
    public void testPolymorphism() {
        // Arrange - Store different types in Vehicle reference
        Vehicle[] vehicles = {
            new Car("Tesla", 2024, 4),
            new Truck("Kenworth", 2023, 15000.0),
            new Car("Porsche", 2024, 2)
        };

        // Act & Assert
        assertEquals(3, vehicles.length, "Should have 3 vehicles");

        for (Vehicle vehicle : vehicles) {
            assertNotNull(vehicle, "Vehicle should not be null");
            assertNotNull(vehicle.getBrand(), "Vehicle brand should not be null");
            assertTrue(vehicle.getYear() > 2000, "Vehicle year should be reasonable");
            assertNotNull(vehicle.getVehicleType(), "Vehicle type should not be null");
        }

        // Verify type-specific behavior
        assertTrue(vehicles[0] instanceof Car, "First vehicle should be a Car");
        assertTrue(vehicles[1] instanceof Truck, "Second vehicle should be a Truck");
        assertTrue(vehicles[2] instanceof Car, "Third vehicle should be a Car");
    }

    @Test
    @DisplayName("Test switch expression with sealed classes (Java 17+ pattern matching)")
    public void testSwitchExpressionWithSealedClasses() {
        // Arrange
        Vehicle car = new Car("Mazda", 2024, 4);
        Vehicle truck = new Truck("Iveco", 2023, 8000.0);

        // Act
        String carInfo = getVehicleInfo(car);
        String truckInfo = getVehicleInfo(truck);

        // Assert
        assertTrue(carInfo.contains("Car"), "Car info should contain 'Car'");
        assertTrue(carInfo.contains("doors"), "Car info should mention doors");
        assertTrue(truckInfo.contains("Truck"), "Truck info should contain 'Truck'");
        assertTrue(truckInfo.contains("cargo"), "Truck info should mention cargo");
    }

    // Helper method demonstrating exhaustive pattern matching with sealed classes
    private String getVehicleDescription(Vehicle vehicle) {
        // With sealed classes, the compiler knows all possible subtypes
        // This enables exhaustive pattern matching without a default case
        if (vehicle instanceof Car car) {
            return "This is a Car made by " + car.getBrand();
        } else if (vehicle instanceof Truck truck) {
            return "This is a Truck made by " + truck.getBrand() +
                   " with cargo capacity: " + truck.getCargoCapacity() + " kg";
        }
        // No default needed - compiler knows all cases are covered
        throw new AssertionError("Unknown vehicle type");
    }

    // Helper method demonstrating switch pattern matching with sealed classes
    private String getVehicleInfo(Vehicle vehicle) {
        // Modern switch expression with pattern matching (Java 21+)
        return switch (vehicle) {
            case Car car -> "Car: " + car.getBrand() + " with " +
                           car.getNumberOfDoors() + " doors";
            case Truck truck -> "Truck: " + truck.getBrand() + " with " +
                               truck.getCargoCapacity() + " kg cargo capacity";
            default -> throw new IllegalArgumentException("Unknown vehicle type: " + vehicle);
        };
    }
}
