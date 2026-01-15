package com.codeprep.streams.terminaloperations;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.stream.Collectors;

public class CountingExample {

    public static void main(String[] args) {
        /*
         * Collectors.counting() is a collector that counts the number of elements in a stream.
         *
         * Key characteristics:
         * - Returns a Collector that produces a Long value (not long primitive)
         * - Used with the collect() terminal operation
         * - More flexible than count() as it can be used with groupingBy() for grouped counting
         * - Equivalent to stream.count() but useful in collector compositions
         *
         * Basic usage:
         * Long count = list.stream()
         *                  .filter(predicate)
         *                  .collect(Collectors.counting());
         *
         * Advanced usage with groupingBy:
         * Map<Category, Long> countByCategory = list.stream()
         *     .collect(Collectors.groupingBy(Item::getCategory, Collectors.counting()));
         *
         * This groups items by category and counts how many items are in each category.
         */

        // Example 1: Advanced usage - Count students by grade level using groupingBy
        var countByGrade = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getGradeLevel,
                        Collectors.counting()
                ));
        System.out.println("Count of students by grade level: " + countByGrade);

        // Example 2: Count students by gender
        var countByGender = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getGender,
                        Collectors.counting()
                ));
        System.out.println("Count of students by gender: " + countByGender);
    }
}
