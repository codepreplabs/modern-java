package com.codeprep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PatternMatchingExample class
 */
class PatternMatchingExampleTest {

    /**
     * Provides test data for pattern matching tests
     * @return Stream of Arguments containing input object and expected output
     */
    static Stream<Arguments> provideTestDataForPatternMatching() {
        return Stream.of(
                Arguments.of(10, "Integer:10"),
                Arguments.of(42, "Integer:42"),
                Arguments.of("Hello", "String of length: 5"),
                Arguments.of("Java", "String of length: 4"),
                Arguments.of("", "String of length: 0"),
                Arguments.of(3.14, "Not a string or an Integer"),
                Arguments.of(true, "Not a string or an Integer"),
                Arguments.of(new Object(), "Not a string or an Integer")
        );
    }

    /**
     * Provides test data for switch pattern matching tests
     * @return Stream of Arguments containing input object and expected output
     */
    static Stream<Arguments> provideTestDataForSwitchPatternMatching() {
        return Stream.of(
                Arguments.of(10, "Integer 10"),
                Arguments.of(42, "Integer 42"),
                Arguments.of("Hello", "String is of length: 5"),
                Arguments.of("Java", "String is of length: 4"),
                Arguments.of("", "String is of length: 0"),
                Arguments.of(3.14, "Not a String or Integer"),
                Arguments.of(true, "Not a String or Integer"),
                Arguments.of(new Object(), "Not a String or Integer"),
                Arguments.of(null, "Not a String or Integer")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForPatternMatching")
    @DisplayName("Test priorToPatternMatching method with various inputs")
    void testPriorToPatternMatching(Object input, String expected) {
        String result = PatternMatchingExample.priorToPatternMatching(input);
        assertEquals(expected, result,
                "Failed for input: " + input + " (type: " +
                (input != null ? input.getClass().getSimpleName() : "null") + ")");
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForPatternMatching")
    @DisplayName("Test patternMatching method with various inputs")
    void testPatternMatching(Object input, String expected) {
        String result = PatternMatchingExample.patternMatching(input);
        assertEquals(expected, result,
                "Failed for input: " + input + " (type: " +
                (input != null ? input.getClass().getSimpleName() : "null") + ")");
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForSwitchPatternMatching")
    @DisplayName("Test patternMatchingUsingSwitch method with various inputs")
    void testPatternMatchingUsingSwitch(Object input, String expected) {
        String result = PatternMatchingExample.patternMatchingUsingSwitch(input);
        assertEquals(expected, result,
                "Failed for input: " + input + " (type: " +
                (input != null ? input.getClass().getSimpleName() : "null") + ")");
    }

    @Test
    @DisplayName("Test consistency between traditional and pattern matching approaches")
    void testConsistencyBetweenApproaches() {
        Object[] testInputs = {10, "Hello", 3.14, true, new Object()};

        for (Object input : testInputs) {
            String priorResult = PatternMatchingExample.priorToPatternMatching(input);
            String patternResult = PatternMatchingExample.patternMatching(input);

            assertEquals(priorResult, patternResult,
                    "Results should be consistent for input: " + input);
        }
    }
}
