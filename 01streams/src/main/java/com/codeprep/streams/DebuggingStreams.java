package com.codeprep.streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Debugging Streams:
 *
 * Since streams use lazy evaluation and internal iteration,
 * traditional debugging can be challenging. Here are some techniques:
 *
 * 1. peek() - Allows you to see elements as they flow through the stream
 * 2. Breaking into smaller operations - Split complex streams into steps
 * 3. Using IntelliJ Stream Debugger - Set breakpoints on stream operations
 * 4. Collecting intermediate results - Store results at each stage
 */
public class DebuggingStreams {
    public static void main(String[] args) {

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank");

        System.out.println("=== Example 1: Using peek() to debug ===");
        // peek() allows you to perform an action on each element without consuming the stream
        List<String> result1 = names.stream()
                .peek(name -> System.out.println("Original: " + name))
                .filter(name -> name.length() > 3)
                .peek(name -> System.out.println("After filter: " + name))
                .map(String::toUpperCase)
                .peek(name -> System.out.println("After map: " + name))
                .toList();

        System.out.println("Final result: " + result1);

        System.out.println("\n=== Example 2: Debugging with numbers ===");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sum = numbers.stream()
                .peek(n -> System.out.println("Processing: " + n))
                .filter(n -> n % 2 == 0)
                .peek(n -> System.out.println("Even number: " + n))
                .map(n -> n * n)
                .peek(n -> System.out.println("Squared: " + n))
                .reduce(0, Integer::sum);

        System.out.println("Sum of squares of even numbers: " + sum);

        System.out.println("\n=== Example 3: Breaking stream into steps ===");
        // Instead of chaining everything, break it down for easier debugging
        List<String> longNames = names.stream()
                .filter(name -> name.length() > 4)
                .toList();
        System.out.println("Step 1 - Long names: " + longNames);

        List<String> upperCaseNames = longNames.stream()
                .map(String::toUpperCase)
                .toList();
        System.out.println("Step 2 - Uppercase: " + upperCaseNames);

        List<String> sortedNames = upperCaseNames.stream()
                .sorted()
                .toList();
        System.out.println("Step 3 - Sorted: " + sortedNames);

        System.out.println("\n=== Example 4: Debugging with custom logic ===");
        List<Integer> debugNumbers = Arrays.asList(10, 15, 20, 25, 30);

        List<Integer> result4 = debugNumbers.stream()
                .peek(n -> {
                    System.out.println("Input: " + n);
                    if (n > 20) {
                        System.out.println("  -> Greater than 20");
                    }
                })
                .filter(n -> n > 15)
                .peek(n -> System.out.println("Passed filter: " + n))
                .map(n -> {
                    int doubled = n * 2;
                    System.out.println("  " + n + " * 2 = " + doubled);
                    return doubled;
                })
                .toList();

        System.out.println("Final result: " + result4);

        /*
         * Tips for debugging streams:
         * 1. Use peek() to inspect elements at different stages
         * 2. Set breakpoints in IntelliJ on lambda expressions
         * 3. Use the "Trace Current Stream Chain" feature in IntelliJ
         * 4. Break complex streams into smaller, testable pieces
         * 5. Consider extracting lambdas to named methods for easier debugging
         */
    }
}
