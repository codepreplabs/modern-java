package com.codeprep;

/*
 * Interfaces Prior to Java 8:
 *
 * Before Java 8, interfaces had several limitations:
 * 1. All methods were implicitly abstract (no method implementations allowed)
 * 2. All methods were implicitly public
 * 3. All fields were implicitly public, static, and final (constants only)
 * 4. No static methods were allowed
 * 5. Classes implementing interfaces had to provide implementations for ALL methods
 *
 * Java 8 introduced:
 *
 * DEFAULT METHODS:
 * - Methods with implementations in interfaces using the 'default' keyword
 * - Provide default behavior that implementing classes can inherit or override
 * - Allow adding new methods to interfaces without breaking existing implementations
 * - Can be overridden by implementing classes
 * - Enable backward compatibility when evolving APIs
 * - Syntax: default returnType methodName() { // implementation }
 * - Example use case: Adding new methods to Collection interface without breaking existing code
 *
 * STATIC METHODS:
 * - Static utility methods directly in interfaces (using 'static' keyword)
 * - Belong to the interface itself, not to implementing classes
 * - Cannot be overridden by implementing classes
 * - Called using the interface name (e.g., InterfaceName.staticMethod())
 * - Useful for grouping related utility methods with the interface
 * - Syntax: static returnType methodName() { // implementation }
 * - Example use case: Factory methods, helper utilities
 *
 * These changes allow for interface evolution without breaking existing implementations
 * and enable better API design with utility methods grouped logically with interfaces.
 */

public interface DefaultAndStaticInterface {

    // Abstract method (must be implemented by classes)
    String getName();

    // DEFAULT METHOD EXAMPLE
    // Provides a default implementation that can be used or overridden
    default void printInfo() {
        System.out.println("Name: " + getName());
        System.out.println("This is a default method implementation");
    }

    // STATIC METHOD EXAMPLE
    // Utility method that belongs to the interface itself
    static void printWelcomeMessage() {
        System.out.println("Welcome to DefaultAndStaticMethodInterface!");
        System.out.println("This is a static method in an interface");
    }

    // Another static method example - factory pattern
    static DefaultAndStaticInterface createDefault() {
        return new DefaultAndStaticInterface() {
            @Override
            public String getName() {
                return "Default Implementation";
            }
        };
    }
}