package com.codeprep.streams.sort;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Comparator;
import java.util.List;

public class SortWithComparator {

    public static void main(String[] args) {

        /*
         * Using Comparator with Streams:
         *
         * Comparator.comparing() is a static method that creates a comparator based on a key extractor function.
         * In this example:
         * - Comparator.comparing(Student::getName) creates a comparator that compares students by their name
         * - The sorted() method applies this comparator to sort the stream elements
         * - You can chain additional operations like .reversed() for descending order
         * - You can also use .thenComparing() to add secondary sorting criteria
         *
         * Other useful Comparator methods:
         * - Comparator.reverseOrder() - for natural reverse ordering
         * - Comparator.naturalOrder() - for natural ordering
         * - comparator.reversed() - to reverse any comparator
         * - Comparator.comparing(Student::getGpa).thenComparing(Student::getName) - multi-level sorting
         */
        List<String> studentNames = StudentDataBase.getAllStudents()
                .stream()
                .sorted(Comparator.comparing(Student::getName))
                .map(Student::getName)
                .toList();
        System.out.println(studentNames);

    }
}
