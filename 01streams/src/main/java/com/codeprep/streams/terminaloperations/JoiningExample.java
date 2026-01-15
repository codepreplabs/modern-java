package com.codeprep.streams.terminaloperations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Joining Terminal Operation
 *
 * The joining() collector is used to concatenate stream elements into a single String.
 * It's a specialized collector from the Collectors class specifically designed for String operations.
 *
 * Three variants:
 * 1. joining() - Concatenates elements with no delimiter
 * 2. joining(delimiter) - Concatenates elements with a delimiter between them
 * 3. joining(delimiter, prefix, suffix) - Concatenates with delimiter, prefix, and suffix
 *
 * Common use cases:
 * - Creating CSV/TSV formatted strings
 * - Building SQL queries dynamically
 * - Formatting output strings
 * - Creating comma-separated lists
 */
public class JoiningExample {

    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry", "Date", "Elderberry");

        // Example 1: joining() - No delimiter
        String result1 = fruits.stream()
                .collect(Collectors.joining());
        System.out.println("No delimiter: " + result1);
        // Output: AppleBananaCherryDateElderberry

        // Example 2: joining(delimiter) - With comma delimiter
        String result2 = fruits.stream()
                .collect(Collectors.joining(", "));
        System.out.println("With delimiter: " + result2);
        // Output: Apple, Banana, Cherry, Date, Elderberry

        // Example 3: joining(delimiter, prefix, suffix) - With all parameters
        String result3 = fruits.stream()
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("With delimiter, prefix, and suffix: " + result3);
        // Output: [Apple, Banana, Cherry, Date, Elderberry]

        // Example 4: Creating a SQL-like string
        List<String> columns = Arrays.asList("id", "name", "email", "age");
        String sqlSelect = columns.stream()
                .collect(Collectors.joining(", ", "SELECT ", " FROM users"));
        System.out.println("\nSQL Query: " + sqlSelect);
        // Output: SELECT id, name, email, age FROM users

        // Example 5: Joining with transformation
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        String numberString = numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" - "));
        System.out.println("\nNumbers joined: " + numberString);
        // Output: 1 - 2 - 3 - 4 - 5

        // Example 6: Joining filtered elements
        String longFruits = fruits.stream()
                .filter(fruit -> fruit.length() > 5)
                .collect(Collectors.joining(" | ", "Long fruits: ", ""));
        System.out.println("\n" + longFruits);
        // Output: Long fruits: Banana | Cherry | Elderberry
    }
}
