package com.codeprep.streams.filter;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;

public class FilterExample {

    public static void main(String[] args) {

        List<Student> students = StudentDataBase.getAllStudents();

        /*
         * The filter() method is an intermediate operation in the Stream API that
         * allows you to select elements from a stream based on a given condition (Predicate).
         *
         * How to use filter():
         * 1. Call filter() on a stream object
         * 2. Provide a Predicate (a boolean-valued function) as an argument
         * 3. Only elements for which the Predicate returns true will pass through to the next operation
         * 4. filter() returns a new stream, so you can chain other operations
         *
         * Syntax: stream.filter(element -> boolean_condition)
         *
         * Common Use Cases:
         * - Filtering based on object properties (e.g., age, grade, gender)
         * - Combining multiple conditions using logical operators (&& , ||)
         * - Removing null values or empty elements
         *
         * Examples below demonstrate various filter operations on student data:
         */

        // Example 1: Filter students by gender
        System.out.println("=== Female Students ===");
        students.stream()
                .filter(student -> student.getGender().equals("female"))
                .forEach(student -> System.out.println(student.getName()));

        System.out.println("\n=== Male Students ===");
        students.stream()
                .filter(student -> student.getGender().equals("male"))
                .forEach(student -> System.out.println(student.getName()));

        // Example 2: Filter students with GPA greater than or equal to 3.9
        System.out.println("\n=== Students with GPA >= 3.9 ===");
        students.stream()
                .filter(student -> student.getGpa() >= 3.9)
                .forEach(student -> System.out.println(student.getName() + " - GPA: " + student.getGpa()));
    }
}
