package com.codeprep;

/*
 * Sealed Classes (Java 17+)
 *
 * Sealed classes restrict which other classes can extend or implement them.
 * They provide more control over the class hierarchy and enable exhaustive pattern matching.
 *
 * Key Features:
 * - Use 'sealed' modifier to declare a sealed class
 * - Use 'permits' clause to specify allowed subclasses
 * - Permitted subclasses must be one of: final, sealed, or non-sealed
 * - All permitted subclasses must be accessible to the sealed class
 *
 * Use Cases:
 * 1. Domain Modeling: Represent a fixed set of domain concepts (e.g., payment types, user roles)
 * 2. Type Safety: Ensure only known subtypes exist for exhaustive pattern matching
 * 3. API Design: Control extensibility and prevent unwanted inheritance
 * 4. Security: Prevent malicious extensions of sensitive classes
 * 5. Code Maintenance: Make the complete type hierarchy explicit and easier to understand
 * 6. Pattern Matching: Enable compiler to verify all cases are handled in switch expressions
 *
 * Example Use Cases:
 * - Payment methods: CreditCard, DebitCard, PayPal (closed set)
 * - Shapes: Circle, Rectangle, Triangle (geometric operations)
 * - Results: Success, Failure, Pending (operation outcomes)
 * - Vehicle types: Car, Truck, Motorcycle (transportation system)
 */
public sealed class Vehicle permits Car, Truck {

    private final String brand;
    private final int year;

    public Vehicle(String brand, int year) {
        this.brand = brand;
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public int getYear() {
        return year;
    }

    // Method to demonstrate exhaustive pattern matching with sealed classes
    public String getVehicleType() {
        return "Generic Vehicle";
    }
}
