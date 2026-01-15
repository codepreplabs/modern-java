package com.codeprep.streams.flatmap;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * FlatMap Method Overview:
 *
 * The flatMap() method is an intermediate operation in Java Streams that transforms
 * each element of a stream into another stream and then flattens all the resulting
 * streams into a single stream.
 *
 * Key Characteristics:
 * - Takes a Function<T, Stream<R>> as a parameter
 * - Maps each element to a stream of values (one-to-many mapping)
 * - Flattens the resulting streams into a single stream
 *
 * Why Use FlatMap:
 * 1. Flattening nested structures: Convert a stream of collections into a single stream
 *    Example: List<List<Integer>> -> Stream<Integer>
 *
 * 2. One-to-many transformations: When one input element produces multiple output elements
 *    Example: Split sentences into words, where each sentence produces multiple words
 *
 * 3. Removing empty or null elements: Filter and flatten in one operation
 *    Example: Stream<Optional<T>> -> Stream<T>
 *
 * 4. Working with hierarchical data: Process nested data structures efficiently
 *    Example: Stream of departments -> Stream of employees
 *
 * Common Use Cases:
 * - Converting List<String[]> to a single stream of strings
 * - Processing nested JSON or XML structures
 * - Combining multiple collections into one stream
 * - Breaking down complex objects into their components
 *
 * Difference from map():
 * - map(): Transforms each element to exactly one new element (1-to-1)
 * - flatMap(): Transforms each element to zero or more elements (1-to-many)
 */
public class FlatMapExample {

    public static void main(String[] args) {

        Set<String> studentActivities = StudentDataBase.getAllStudents().stream()
                .map(Student::getActivities)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        System.out.println(studentActivities);
    }
}
