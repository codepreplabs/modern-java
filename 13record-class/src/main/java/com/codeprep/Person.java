package com.codeprep;

/**
 * Record classes in Java (introduced in Java 14 as preview, finalized in Java 16)
 *
 * What are Records?
 * - Records are a special kind of class designed to be simple data carriers
 * - They automatically provide implementations for:
 *   * Constructor (canonical constructor)
 *   * Getters (accessor methods)
 *   * equals() method
 *   * hashCode() method
 *   * toString() method
 * - Records are implicitly final and cannot be extended
 * - All fields in a record are final and immutable
 *
 * Common Use Cases:
 * 1. DTOs (Data Transfer Objects) - transferring data between layers
 * 2. Value Objects - representing immutable domain values
 * 3. Configuration objects - holding application configuration
 * 4. API responses/requests - modeling JSON or API data structures
 * 5. Database query results - encapsulating query result sets
 * 6. Tuples - returning multiple values from methods
 * 7. Event objects - representing domain events in event-driven systems
 * 8. Message payloads - in messaging systems
 *
 * Benefits:
 * - Reduces boilerplate code significantly
 * - Ensures immutability by default
 * - Clear intent - signals that this is a data carrier
 * - Thread-safe due to immutability
 * - More concise and readable code
 */
public record Person(String name, int age, String email) {

    /**
     * Compact constructor - validates the record's state
     * This is executed before the canonical constructor
     */
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }

    /**
     * Custom constructor - creates a Person with a default email
     * Records can have additional constructors that delegate to the canonical constructor
     *
     * @param name the person's name
     * @param age the person's age
     */
    public Person(String name, int age) {
        this(name, age, name.toLowerCase().replace(" ", ".") + "@example.com");
    }

    /**
     * Custom method example - records can have methods just like regular classes
     */
    public String getDisplayName() {
        return name + " (" + age + " years old)";
    }
}
