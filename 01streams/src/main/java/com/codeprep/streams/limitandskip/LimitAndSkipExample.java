package com.codeprep.streams.limitandskip;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Demonstrates the use of limit() and skip() methods in Java Streams.
 *
 * limit(n): Returns a stream consisting of the first n elements of the stream.
 *           Useful for restricting the number of elements processed or returned.
 *
 * skip(n): Returns a stream after discarding the first n elements of the stream.
 *          Useful for pagination or skipping over unwanted elements at the beginning.
 *
 * Both methods are intermediate operations that return a new stream.
 * They can be combined for pagination: skip(n).limit(m) skips n elements and then takes m elements.
 */
public class LimitAndSkipExample {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(6, 7, 8, 9, 10);

        // Example 1: Using limit() - takes first 2 elements (6, 7)
        Optional<Integer> result = numbers.stream()
                .limit(2)
                .reduce(Integer::sum);
        System.out.println("limit(2): " + result); // Output: Optional[13]

        // Example 2: Using skip() - skips first 2 elements, takes remaining (8, 9, 10)
        Optional<Integer> skipResult = numbers.stream()
                .skip(2)
                .reduce(Integer::sum);
        System.out.println("skip(2): " + skipResult); // Output: Optional[27]

    }
}
