package com.codeprep.streams.terminaloperations;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * partitioningBy() is a specialized collector in Java Streams that divides elements
 * into two groups based on a Predicate.
 *
 * Key characteristics:
 * - Returns a Map<Boolean, List<T>> where:
 *   - true key holds elements that match the predicate
 *   - false key holds elements that don't match the predicate
 * - Always returns both keys (true and false), even if one group is empty
 * - More efficient than groupingBy() when you only need binary classification
 * - Can accept a downstream collector as second parameter for further processing
 *
 * Common use cases:
 * - Separating even/odd numbers
 * - Dividing data into pass/fail categories
 * - Splitting items into valid/invalid groups
 * - Any binary classification scenario
 *
 * Example:
 * Map<Boolean, List<Integer>> partitioned = numbers.stream()
 *     .collect(Collectors.partitioningBy(n -> n % 2 == 0));
 * // partitioned.get(true) contains even numbers
 * // partitioned.get(false) contains odd numbers
 */
public class PartitionByExample {

    public static void main(String[] args) {

        Predicate<Student> gpaPredicate = student -> student.getGpa() >= 3.8;
        Map<Boolean, List<Student>> partitioningMap = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.partitioningBy(gpaPredicate));
        System.out.println(partitioningMap);


        Map<Boolean, Set<Student>> partitioningMapWithSet = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.partitioningBy(gpaPredicate, Collectors.toSet()));
        System.out.println(partitioningMapWithSet);
    }
}
