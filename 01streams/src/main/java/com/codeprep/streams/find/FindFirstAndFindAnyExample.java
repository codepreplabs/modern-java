package com.codeprep.streams.find;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Optional;

/**
 * findFirst() vs findAny() in Java Streams
 *
 * findFirst():
 * - Returns the first element of the stream wrapped in an Optional
 * - Deterministic: always returns the same element for a given stream
 * - Use case: When the order matters and you need the first element
 * - Example: Finding the first matching item in a sorted list
 * - Performance: May be slower in parallel streams as it must maintain order
 *
 * findAny():
 * - Returns any element from the stream wrapped in an Optional
 * - Non-deterministic in parallel streams: may return different elements
 * - Use case: When any matching element is acceptable and order doesn't matter
 * - Example: Finding any available resource from a pool
 * - Performance: Faster in parallel streams as it can return any element without coordination
 *
 * Both return Optional<T> to handle cases where no element is found
 */
public class FindFirstAndFindAnyExample {

    public static void main(String[] args) {

        Optional<Student> optionalStudent1 = StudentDataBase.getAllStudents().stream()
                .filter(student -> student.getGpa() >= 3.9)
                .findAny();
        System.out.println(optionalStudent1);

        Optional<Student> optionalStudent2 = StudentDataBase.getAllStudents().stream()
                .filter(student -> student.getGpa() >= 3.9)
                .findFirst();
        System.out.println(optionalStudent2);
    }
}
