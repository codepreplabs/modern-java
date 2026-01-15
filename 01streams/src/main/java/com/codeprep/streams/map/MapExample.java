package com.codeprep.streams.map;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.ArrayList;
import java.util.List;

/*
 * The map() method is used in Java Streams to transform elements from one form to another.
 * It applies a given function to each element of the stream and returns a new stream
 * containing the transformed elements.
 *
 * Key use cases:
 * - Converting data types (e.g., String to Integer)
 * - Extracting specific properties from objects
 * - Performing calculations or modifications on each element
 * - Creating derived values from existing data
 *
 * The map operation is intermediate, lazy, and produces a one-to-one transformation
 * (each input element produces exactly one output element).
 */
public class MapExample {

    public static void main(String[] args) {

        List<String> studentNames = StudentDataBase.getAllStudents().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .toList();
        System.out.println(studentNames);
    }
}
