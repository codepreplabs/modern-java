package com.codeprep.streams.terminaloperations;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * maxBy() and minBy() Terminal Operations in Streams
 *
 * maxBy(Comparator):
 * - Returns an Optional containing the maximum element according to the provided Comparator
 * - Returns Optional.empty() if the stream is empty
 * - Terminal operation that consumes the stream
 *
 * minBy(Comparator):
 * - Returns an Optional containing the minimum element according to the provided Comparator
 * - Returns Optional.empty() if the stream is empty
 * - Terminal operation that consumes the stream
 *
 * Both methods:
 * - Require a Comparator to define the ordering
 * - Return Optional<T> to handle empty streams safely
 * - Are useful for finding extreme values based on custom comparison logic
 * - Can be used with method references like Comparator.comparing()
 *
 * Example Usage:
 * Optional<String> max = list.stream().collect(Collectors.maxBy(Comparator.naturalOrder()));
 * Optional<String> min = list.stream().collect(Collectors.minBy(Comparator.comparingInt(String::length)));
 */
public class MaxByAndMinByExample {

    public static void main(String[] args) {

        Optional<Student> studentMin = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.minBy(Comparator.comparing(Student::getGpa)));
        System.out.println(studentMin);

        Optional<Student> studentMax = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.maxBy(Comparator.comparing(Student::getGpa)));
        System.out.println(studentMax);
    }
}
