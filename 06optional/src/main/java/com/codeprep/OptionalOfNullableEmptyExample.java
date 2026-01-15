package com.codeprep;

import java.util.Optional;

/*
 * Optional.of() in Java:
 *
 * Optional.of() is used to create an Optional that MUST contain a non-null value.
 * - If the value is non-null, it returns an Optional containing the value
 * - If the value is null, it throws NullPointerException
 *
 * Use Cases:
 * - When you are certain the value is never null
 * - When you want to fail fast if a null value is passed unexpectedly
 * - When wrapping values from sources that guarantee non-null results
 *
 * ---------------------------------------------------------------------------------
 *
 * Optional.ofNullable() in Java:
 *
 * Optional.ofNullable() is used to create an Optional that may or may not contain a value.
 * - If the value is non-null, it returns an Optional containing the value
 * - If the value is null, it returns an empty Optional (Optional.empty())
 *
 * This is different from Optional.of() which throws NullPointerException if the value is null.
 *
 * Use Cases:
 * - When you're unsure if a value might be null (e.g., database results, external API responses)
 * - When wrapping potentially null return values from legacy code
 * - When you want to safely chain operations on values that might be null
 *
 * ---------------------------------------------------------------------------------
 *
 * Optional.empty() in Java:
 *
 * Optional.empty() is used to create an empty Optional instance with no value present.
 * - It returns a singleton empty Optional instance
 * - Calling get() on an empty Optional throws NoSuchElementException
 * - Use isPresent() or isEmpty() to check before accessing the value
 *
 * Use Cases:
 * - When you explicitly want to return "no value" from a method
 * - As a default return value in conditional logic
 * - When initializing an Optional variable that will be assigned later
 * - In stream operations when filtering results in nothing
 *
 * ---------------------------------------------------------------------------------
 *
 * Optional.orElseThrow() in Java:
 *
 * orElseThrow() is used to get the value from an Optional or throw an exception if empty.
 * - If a value is present, it returns the value
 * - If the Optional is empty, it throws the specified exception
 * - Java 10+ added a no-arg version that throws NoSuchElementException
 *
 * Use Cases:
 * - When the absence of a value is an error condition
 * - When you want to fail fast with a meaningful exception
 * - When you need to throw a custom exception for business logic
 * - Better alternative to calling get() which also throws but with less control
 */
public class OptionalOfNullableEmptyExample {

    public static void main(String[] args) {
        // ==================== Optional.of() Examples ====================

        // Example 1: Optional.of() with a non-null value
        String validName = "Alice";
        Optional<String> optionalOf = Optional.of(validName);
        System.out.println("Optional.of() with value: " + optionalOf); // Optional[Alice]
        System.out.println("Value: " + optionalOf.get()); // Alice

        // Example 2: Optional.of() with null - This would throw NullPointerException!
        // String nullValue = null;
        // Optional<String> willThrow = Optional.of(nullValue); // NullPointerException!

        // Example 3: Chaining operations with Optional.of()
        String greeting = Optional.of("hello")
                .map(String::toUpperCase)
                .orElse("NO GREETING");
        System.out.println("Greeting: " + greeting); // HELLO

        // ==================== Optional.ofNullable() Examples ====================

        // Example 1: With a non-null value
        String name = "John";
        Optional<String> optionalName = Optional.ofNullable(name);
        System.out.println("Optional with value: " + optionalName); // Optional[John]
        System.out.println("Is present: " + optionalName.isPresent()); // true

        // Example 2: With a null value
        String nullName = null;
        Optional<String> optionalNullName = Optional.ofNullable(nullName);
        System.out.println("Optional with null: " + optionalNullName); // Optional.empty
        System.out.println("Is present: " + optionalNullName.isPresent()); // false

        // Example 3: Using orElse() to provide a default value
        String result = Optional.ofNullable(nullName).orElse("Default Name");
        System.out.println("With default: " + result); // Default Name

        // Example 4: Chaining operations safely with map()
        String upperName = Optional.ofNullable(name)
                .map(String::toUpperCase)
                .orElse("NO NAME");
        System.out.println("Uppercase name: " + upperName); // JOHN

        // Example 5: Difference between of() and ofNullable()
        // Optional.of(null); // This would throw NullPointerException!
        Optional<String> safeOptional = Optional.ofNullable(null); // This is safe
        System.out.println("Safe optional from null: " + safeOptional); // Optional.empty

        // ==================== Optional.empty() Examples ====================

        // Example 1: Creating an empty Optional
        Optional<String> emptyOptional = Optional.empty();
        System.out.println("Empty optional: " + emptyOptional); // Optional.empty
        System.out.println("Is empty: " + emptyOptional.isEmpty()); // true (Java 11+)

        // Example 2: Using orElse() with empty Optional
        String defaultValue = emptyOptional.orElse("No value present");
        System.out.println("Default from empty: " + defaultValue); // No value present

        // Example 3: Using orElseGet() with empty Optional (lazy evaluation)
        String lazyDefault = emptyOptional.orElseGet(() -> "Computed default");
        System.out.println("Lazy default: " + lazyDefault); // Computed default

        // Example 4: Returning empty Optional from a method
        Optional<String> found = findUserById(999);
        System.out.println("User found: " + found); // Optional.empty

        // Example 5: Chaining with ifPresentOrElse() (Java 9+)
        emptyOptional.ifPresentOrElse(
                value -> System.out.println("Value: " + value),
                () -> System.out.println("No value present!") // This will execute
        );

        // ==================== orElseThrow() Examples ====================

        // Example 1: orElseThrow() with a present value - returns the value
        Optional<String> presentOptional = Optional.of("Success");
        String successValue = presentOptional.orElseThrow(() -> new RuntimeException("Value not found"));
        System.out.println("orElseThrow with value: " + successValue); // Success

        // Example 2: orElseThrow() with empty Optional - throws exception
        try {
            Optional<String> emptyOpt = Optional.empty();
            String value = emptyOpt.orElseThrow(() -> new IllegalStateException("No value available!"));
        } catch (IllegalStateException e) {
            System.out.println("Caught exception: " + e.getMessage()); // No value available!
        }

        // Example 3: orElseThrow() with no argument (Java 10+) - throws NoSuchElementException
        try {
            Optional<String> emptyOpt = Optional.empty();
            String value = emptyOpt.orElseThrow(); // Throws NoSuchElementException
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Caught NoSuchElementException: " + e.getMessage());
        }

        // Example 4: Practical use case - finding user or throwing custom exception
        try {
            String user = findUserById(999).orElseThrow(() -> new UserNotFoundException("User with ID 999 not found"));
        } catch (UserNotFoundException e) {
            System.out.println("User error: " + e.getMessage()); // User with ID 999 not found
        }
    }

    // Example method returning Optional.empty() when no result is found
    private static Optional<String> findUserById(int id) {
        if (id == 1) {
            return Optional.of("John Doe");
        }
        return Optional.empty(); // Return empty when user not found
    }
}

// Custom exception for demonstration
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

