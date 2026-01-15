package com.codeprep.streams.match;

import java.util.Arrays;
import java.util.List;

public class MatchExample {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // anyMatch() - Returns true if ANY element in the stream matches the given predicate
        // Short-circuits: stops processing as soon as a match is found
        // Returns false if the stream is empty
        boolean hasEvenNumber = numbers.stream()
                .anyMatch(n -> n % 2 == 0);
        System.out.println("Has any even number: " + hasEvenNumber); // true

        // allMatch() - Returns true if ALL elements in the stream match the given predicate
        // Short-circuits: stops processing as soon as a non-match is found
        // Returns true if the stream is empty (vacuous truth)
        boolean allPositive = numbers.stream()
                .allMatch(n -> n > 0);
        System.out.println("All numbers are positive: " + allPositive); // true

        boolean allEven = numbers.stream()
                .allMatch(n -> n % 2 == 0);
        System.out.println("All numbers are even: " + allEven); // false

        // noneMatch() - Returns true if NO elements in the stream match the given predicate
        // Short-circuits: stops processing as soon as a match is found
        // Returns true if the stream is empty
        boolean noneNegative = numbers.stream()
                .noneMatch(n -> n < 0);
        System.out.println("No negative numbers: " + noneNegative); // true

        boolean noneEven = numbers.stream()
                .noneMatch(n -> n % 2 == 0);
        System.out.println("No even numbers: " + noneEven); // false
    }
}
