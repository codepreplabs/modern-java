package com.codeprep;

/**
 * Unnamed Variables (Preview in Java 21, Java 22)
 *
 * Unnamed variables use the underscore '_' to indicate variables that are required
 * by syntax but not used in the code. This improves code readability by explicitly
 * showing intent to ignore certain values.
 *
 * USE CASES:
 *
 * 1. Try-Catch Blocks: When you need to catch an exception but don't use it
 *    try {
 *        int result = Integer.parseInt(str);
 *    } catch (NumberFormatException _) {
 *        // Exception not needed, just handle the error
 *        System.out.println("Invalid number");
 *    }
 *
 * 2. Enhanced For Loops: When iterating but only caring about count, not the element
 *    for (String _ : list) {
 *        count++;
 *    }
 *
 * 3. Lambda Expressions: When a parameter is required but not used
 *    list.forEach(_ -> System.out.println("Processing item"));
 *
 * 4. Pattern Matching: When deconstructing records but ignoring some components
 *    if (point instanceof Point(int x, int _)) {
 *        // Only care about x coordinate, not y
 *    }
 *
 * 5. Multiple Variable Declarations: When destructuring but not using all values
 *    var (result, _) = getSomeData();  // Only care about result
 *
 * BENEFITS:
 * - Makes code intent clearer (explicit "I don't need this")
 * - Reduces noise from unused variable warnings
 * - Improves readability by avoiding dummy variable names like 'ignored', 'unused', etc.
 */
public class UnnamedVariablesExample {

    // Example 1: Try-Catch with unnamed exception variable
    private static Integer parseWithUnnamedException(String s){
        try{
            return Integer.parseInt(s);
        }catch (NumberFormatException _){
            // We don't need the exception object, just handle the error
            System.out.println("Failed to parse: " + s);
            return null;
        }
    }

    // Example 2: Enhanced for loop with unnamed variable (counting iterations)
    private static void countIterations() {
        String[] items = {"apple", "banana", "cherry", "date"};
        int count = 0;

        // We only care about the count, not the actual elements
        for (String _ : items) {
            count++;
        }
        System.out.println("Total items: " + count);
    }

    // Example 3: Lambda expressions with unnamed parameters
    private static void lambdaExamples() {
        java.util.List<String> list = java.util.List.of("A", "B", "C");

        // We don't care about the element, just want to execute action N times
        list.forEach(_ -> System.out.println("Processing an item"));

        // Using in stream operations
        long count = list.stream()
                .filter(_ -> Math.random() > 0.5)  // Random filter, element not needed
                .count();
        System.out.println("Randomly filtered count: " + count);
    }

    // Example 4: Multiple catch blocks with unnamed variables
    private static void multipleExceptionHandling(String input) {
        try {
            int value = Integer.parseInt(input);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (NumberFormatException _) {
            System.out.println("Invalid number format");
        } catch (ArithmeticException _) {
            System.out.println("Cannot divide by zero");
        }
    }

    // Example 5: Try-with-resources with unnamed variable
    private static void tryWithResources() {
        // When you need to create a resource but don't use it directly
        try (var _ = new java.io.ByteArrayInputStream("test".getBytes())) {
            System.out.println("Resource will be auto-closed");
        } catch (Exception _) {
            System.out.println("Error occurred");
        }
    }

    // Example 6: Nested loops where inner iterator is unused
    private static void nestedLoopExample() {
        String[] rows = {"Row1", "Row2", "Row3"};
        int[] stars = {1, 2, 3, 4, 5}; // Create dummy array for iteration

        for (String row : rows) {
            // Print 5 stars for each row, don't care about the actual values
            for (int _ : stars) {
                System.out.print("* ");
            }
            System.out.println("(" + row + ")");
        }
    }

    // Example 7: Pattern matching with records (destructuring)
    record Point(int x, int y) {}

    private static void patternMatchingExample() {
        Point point = new Point(10, 20);

        // We only care about x coordinate, ignore y
        if (point instanceof Point(int x, int _)) {
            System.out.println("X coordinate: " + x);
        }

        // Works with switch patterns too
        String description = switch (point) {
            case Point(int x, int _) when x > 0 -> "Positive X";
            case Point(int x, int _) -> "Non-positive X";
        };
        System.out.println(description);
    }

    // Example 8: Side-effect operations where parameter is ignored
    private static void sideEffectExample() {
        java.util.List<String> list = java.util.List.of("Task1", "Task2", "Task3");
        int[] counter = {0}; // Wrapper for counter

        // Execute action for each element without caring about element value
        list.forEach(_ -> {
            counter[0]++;
            System.out.println("Executed task #" + counter[0]);
        });
    }

    static void main() {
        System.out.println("=== Example 1: Try-Catch ===");
        parseWithUnnamedException("hello");
        parseWithUnnamedException("123");

        System.out.println("\n=== Example 2: Counting Iterations ===");
        countIterations();

        System.out.println("\n=== Example 3: Lambda Expressions ===");
        lambdaExamples();

        System.out.println("\n=== Example 4: Multiple Exception Handling ===");
        multipleExceptionHandling("abc");
        multipleExceptionHandling("0");

        System.out.println("\n=== Example 5: Try-with-resources ===");
        tryWithResources();

        System.out.println("\n=== Example 6: Nested Loops ===");
        nestedLoopExample();

        System.out.println("\n=== Example 7: Pattern Matching ===");
        patternMatchingExample();

        System.out.println("\n=== Example 8: Side-Effect Operations ===");
        sideEffectExample();
    }
}
