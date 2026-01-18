package com.codeprep;

/**
 * Traditional Main Class - for comparison with compact main
 * This shows the traditional Java main method syntax
 */
public class TraditionalMain {

    public static void main(String[] args) {
        System.out.println("Hello from Traditional Main!");
        System.out.println("This demonstrates traditional Java main method.");

        TraditionalMain app = new TraditionalMain();
        app.greet("Traditional Java");
        app.printInfo();
    }

    // Instance methods need object creation
    void greet(String name) {
        System.out.println("Welcome to " + name + "!");
    }

    void printInfo() {
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("This is a traditional main - requires public class, static, and String[] args");
    }
}
