package com.codeprep.records;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PatternMatchingRecordsExample class
 */
public class PatternMechingRecordsExampleTest {

    private final PatternMatchingRecordsExample example = new PatternMatchingRecordsExample();

    /**
     * Provides test data for pattern matching with sealed records
     * @return Stream of Arguments containing Animal instance and expected name
     */
    static Stream<Arguments> provideAnimalTestData() {
        return Stream.of(
                Arguments.of(new Cat("Whiskers", "orange"), "Whiskers"),
                Arguments.of(new Cat("Mittens", "white"), "Mittens"),
                Arguments.of(new Cat("Shadow", "black"), "Shadow"),
                Arguments.of(new Dog("Buddy", "brown"), "Buddy"),
                Arguments.of(new Dog("Max", "golden"), "Max"),
                Arguments.of(new Dog("Rex", "black"), "Rex"),
                Arguments.of(null, "")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnimalTestData")
    @DisplayName("Test retrieveName method with various Animal instances")
    void testRetrieveName(Animal animal, String expectedName) {
        String result = example.retrieveName(animal);
        assertEquals(expectedName, result,
                "Failed for animal: " + (animal != null ? animal.getClass().getSimpleName() : "null"));
    }

    /**
     * Provides test data for guarded pattern matching
     * Tests various scenarios including null names and normal cases
     * @return Stream of Arguments containing Animal instance and expected name
     */
    static Stream<Arguments> provideGuardedPatternTestData() {
        return Stream.of(
                // Normal cases - animals with valid names
                Arguments.of(new Cat("Whiskers", "orange"), "Whiskers"),
                Arguments.of(new Cat("Mittens", "white"), "Mittens"),
                Arguments.of(new Dog("Buddy", "brown"), "Buddy"),
                Arguments.of(new Dog("Max", "golden"), "Max"),

                // Edge case - Cat with null name (triggers guard)
                Arguments.of(new Cat(null, "gray"), ""),

                // Normal dog case - dogs don't have the null guard
                Arguments.of(new Dog("Rex", "black"), "Rex")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGuardedPatternTestData")
    @DisplayName("Test retrieveNameUsingGuardedPatterns with various scenarios including null names")
    void testRetrieveNameUsingGuardedPatterns(Animal animal, String expectedName) {
        String result = example.retrieveNameUsingGuardedPatterns(animal);
        assertEquals(expectedName, result,
                "Failed for animal: " + animal.getClass().getSimpleName() +
                " with name: " + (animal instanceof Cat cat ? cat.name() :
                                  animal instanceof Dog dog ? dog.name() : "unknown"));
    }
}
