package com.codeprep;

import java.util.Optional;

/**
 * This class demonstrates the usage of Optional's isPresent() and ifPresent() methods.
 * <p>
 * Use Cases:
 * - isPresent(): Returns true if a value is present, false otherwise
 *   Useful for conditional logic when you need to check before processing
 * <p>
 * - ifPresent(): Executes the given consumer if a value is present
 *   Functional approach - cleaner than if-else blocks for simple operations
 * <p>
 * Real-world scenarios:
 * - Handling database query results that may return null
 * - Processing configuration values that might not be set
 * - Dealing with user inputs that are optional
 */
public class OptionalIfAndIsPresent {

    public static void main(String[] args) {

        System.out.println("=== Example 1: Basic isPresent() ===");
        Optional<String> presentValue = Optional.of("hello");
        System.out.println("Has value: " + presentValue.isPresent()); // true

        Optional<String> emptyValue = Optional.empty();
        System.out.println("Has value: " + emptyValue.isPresent()); // false


        System.out.println("\n=== Example 2: isPresent() with conditional logic ===");
        Optional<String> username = findUsername(123);
        if (username.isPresent()) {
            System.out.println("User found: " + username.get());
        } else {
            System.out.println("User not found in database");
        }


        System.out.println("\n=== Example 3: ifPresent() - Functional approach ===");
        // Instead of if-else, use ifPresent for cleaner code
        Optional<String> email = Optional.of("user@example.com");
        email.ifPresent(e -> System.out.println("Sending notification to: " + e));

        Optional<String> emptyEmail = Optional.empty();
        emptyEmail.ifPresent(e -> System.out.println("This won't print"));


        System.out.println("\n=== Example 4: Real-world scenario - Processing optional configuration ===");
        Optional<String> apiKey = getApiKeyFromConfig();
        apiKey.ifPresent(key -> {
            System.out.println("API Key found, length: " + key.length());
            System.out.println("Initializing API client with key...");
        });


        System.out.println("\n=== Example 5: Method reference with ifPresent ===");
        Optional<String> message = Optional.of("Hello, Modern Java!");
        message.ifPresent(System.out::println); // Method reference


        System.out.println("\n=== Example 6: Complex processing ===");
        Optional<User> user = findUser(456);
        user.ifPresent(u -> {
            System.out.println("Processing user: " + u.getName());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Account status: Active");
        });
    }

    // Simulating database lookup
    private static Optional<String> findUsername(int userId) {
        // In real scenario, this would query a database
        return userId == 123 ? Optional.of("john_doe") : Optional.empty();
    }

    // Simulating configuration retrieval
    private static Optional<String> getApiKeyFromConfig() {
        // In real scenario, this might read from properties file
        return Optional.of("abc123xyz789");
    }

    // Simulating user lookup
    private static Optional<User> findUser(int userId) {
        return Optional.of(new User("Jane Smith", "jane@example.com"));
    }

    // Simple User class for demonstration
    static class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
