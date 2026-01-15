package com.codeprep.streams;

/*
* Streams API got introduced in Java eight.
* A stream is a sequence of elements that are created from a collection, are arrays or any kind of IO.
* 1. The main purpose of streams API is to perform some kind of operation on a collection,
* meaning the collection could be a set list, linked list or map and etc.
* 2. Parallel operations are easy to perform with streams API without having to spawn multiple threads
* 3. They can be used with Arrays and any kind of IO
*
* One more thing I would like to highlight here is that streams API manipulate the collections
* in a declarative way.
*
* What does it mean is that instead of writing the code on how to implement the logic,
* just ask the desired result using the functions that are already part of the streams API.
*/

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamsExample {

    public static void main(String[] args) {

        Predicate<Student> gradePredicate = (student -> student.getGradeLevel() >= 3);
        Predicate<Student> gpaPredicate = (student -> student.getGpa() >= 4);

        Map<String, List<String>> studentActivitiesMap = StudentDataBase.getAllStudents()
                .stream()
                .filter(gradePredicate)
                .filter(gpaPredicate)
                .collect(Collectors.toMap(Student::getName, Student::getActivities));
        System.out.println(studentActivitiesMap);
    }
}