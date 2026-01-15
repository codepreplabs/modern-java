package com.codeprep.streams.pattern;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.function.Predicate;

/*
 * Map-Filter-Reduce Pattern
 *
 * This is a fundamental pattern in functional programming and stream processing:
 *
 * 1. MAP: Transform each element in a collection to another form
 *    - Example: Convert a list of numbers to their squares
 *    - Operation: stream.map(x -> x * x)
 *
 * 2. FILTER: Select only elements that match a certain condition
 *    - Example: Keep only even numbers from a list
 *    - Operation: stream.filter(x -> x % 2 == 0)
 *
 * 3. REDUCE: Combine all elements into a single result
 *    - Example: Sum all numbers in a list
 *    - Operation: stream.reduce(0, (a, b) -> a + b)
 *
 * These operations can be chained together to create powerful data processing pipelines.
 * Example: Find the sum of squares of all even numbers in a list
 *   list.stream()
 *       .filter(x -> x % 2 == 0)    // Keep only even numbers
 *       .map(x -> x * x)             // Square each number
 *       .reduce(0, Integer::sum)     // Sum all results
 */
public class MapFilterReduceExample {

    public static void main(String[] args) {

        Predicate<Student> gradeGreaterThanThree = (s) -> s.getGpa() >= 4;
        Integer noOfNotebooks = StudentDataBase.getAllStudents().stream()
                .filter(gradeGreaterThanThree)
                .map(Student::getNoteBooks)
                .reduce(0, Integer::sum);
        System.out.println(noOfNotebooks);
    }
}
