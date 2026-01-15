package com.codeprep.function;

/*
* Using a function interface we can implement a functionality as like we code a method in Java and assign
that functionality to a variable.
* */

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FunctionExample {

    private static final Function<String, String> toUpperCase = String::toUpperCase;
    private static final Function<String, String> appendVersion = (value) -> value.concat("V1");

    private static final List<Student> students = StudentDataBase.getAllStudents();

    private static final Function<List<Student>, Map<String, Double>> getStudentGPA = (students) -> {
      Map<String, Double> studentGpaMap = new HashMap<>();
      students.forEach(student -> {
          studentGpaMap.put(student.getName(), student.getGpa());
      });
      return studentGpaMap;
    };

    public static void main(String[] args) {
        // Function<T, R> - takes T as input and returns R as output
        System.out.println(toUpperCase.apply("Java"));

        System.out.println(toUpperCase.andThen(appendVersion).apply("Java"));

        System.out.println(toUpperCase.compose(appendVersion).apply("Java"));

        Map<String, Double> studentGpaMap = getStudentGPA.apply(students);
        for(Map.Entry<String, Double> entry: studentGpaMap.entrySet()){
            System.out.println("name: " + entry.getKey() + " gpa: " +entry.getValue());
        }
    }
}
