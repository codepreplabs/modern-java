package com.codeprep.streams.minandmax;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Min and Max Functions in Java Streams
 *
 * The min() and max() methods are terminal operations that return an Optional containing:
 * - min(): the minimum element according to the provided Comparator
 * - max(): the maximum element according to the provided Comparator
 *
 * Key Points:
 * 1. Both return Optional<T> because the stream might be empty
 * 2. Require a Comparator to determine ordering
 * 3. For primitive streams (IntStream, LongStream, DoubleStream), they return OptionalInt/Long/Double
 * 4. Natural ordering can be used with Comparator.naturalOrder()
 * 5. Custom comparators can be provided for complex objects
 */
public class MinMaxExample {

    public static void main(String[] args) {
        // Example 1: Min and Max with integers
        List<Integer> numbers = Arrays.asList(5, 3, 9, 1, 7, 2, 8);

        Optional<Integer> min = numbers.stream()
                .min(Comparator.naturalOrder());

        Optional<Integer> max = numbers.stream()
                .max(Comparator.naturalOrder());

        System.out.println("Numbers: " + numbers);
        System.out.println("Minimum: " + min.orElse(null));
        System.out.println("Maximum: " + max.orElse(null));

        // Example 2: Min and Max with strings (by length)
        List<String> words = Arrays.asList("Java", "Stream", "API", "Programming");

        Optional<String> shortestWord = words.stream()
                .min(Comparator.comparingInt(String::length));

        Optional<String> longestWord = words.stream()
                .max(Comparator.comparingInt(String::length));

        System.out.println("\nWords: " + words);
        System.out.println("Shortest word: " + shortestWord.orElse("N/A"));
        System.out.println("Longest word: " + longestWord.orElse("N/A"));

        // Example 3: Handling empty streams
        List<Integer> emptyList = Arrays.asList();
        Optional<Integer> minEmpty = emptyList.stream()
                .min(Comparator.naturalOrder());

        System.out.println("\nEmpty list minimum: " + minEmpty.orElse(-1));
    }
}
