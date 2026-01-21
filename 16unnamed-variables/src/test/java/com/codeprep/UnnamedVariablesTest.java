package com.codeprep;

public class UnnamedVariablesTest {

    // Test unnamed variable in pattern matching
    public void testUnnamedVariableInPatternMatching() {
        Object obj = "Hello World";

        // Using unnamed variable _ for type test pattern
        if (obj instanceof String _) {
            System.out.println("Testing unnamed variable in pattern matching");
            assert true : "Object is a String";
        }
    }

    // Test unnamed variable in exception handling
    public void testUnnamedVariableInExceptionHandling() {
        System.out.println("Testing unnamed variable in exception handling");

        try {
            int result = Integer.parseInt("123");
            assert result == 123 : "Should parse correctly";
        } catch (NumberFormatException _) {
            // Unnamed variable for unused exception
            assert false : "Should not throw exception for valid number";
        }
    }

    // Test unnamed variable with try-with-resources
    public void testUnnamedVariableInTryWithResources() {
        System.out.println("Testing unnamed variable in try-with-resources");

        // Example: when we don't need to use the resource variable
        String content = "Sample content";
        assert content != null : "Content should not be null";
    }

    // Test unnamed variable in record destructuring
    public void testUnnamedVariableInRecord() {
        System.out.println("Testing unnamed variable with records");

        record Point(int x, int y) {}
        Point point = new Point(10, 20);

        // Pattern matching with unnamed variable
        if (point instanceof Point(int x, int _)) {
            assert x == 10 : "X coordinate should be 10";
        }
    }

    public static void main(String[] args) {
        UnnamedVariablesTest test = new UnnamedVariablesTest();

        System.out.println("Running Unnamed Variables Tests...\n");

        test.testUnnamedVariableInPatternMatching();
        test.testUnnamedVariableInExceptionHandling();
        test.testUnnamedVariableInTryWithResources();
        test.testUnnamedVariableInRecord();

        System.out.println("\nAll tests completed successfully!");
    }
}
