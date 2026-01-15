package com.codeprep.consumer;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.function.BiConsumer;

/* *
* Consumer interface accepts input and doesn't return any output
* */

public class BiConsumerExample {

    private static List<Student> students = StudentDataBase.getAllStudents();

    private  static BiConsumer<Integer, Integer> multiply = (a, b) -> {
        System.out.println(a * b);
    };

    private static BiConsumer<Integer, Integer> divide = (a, b) -> {
        System.out.println(a / b);
    };

    private static BiConsumer<String, List<String>> printNameAndActivityBiConsumer = (student, activities) -> {
        System.out.println("name: " + student + " activities: "+ activities);
    };

    public static void main(String[] args) {

        // example for Bi Consumer
        printUsingBiConsumer("Hello", "World");

        // consumer chaining
        multiply.andThen(divide).accept(10, 2);

        // real world use case
        students.forEach(student -> printNameAndActivityBiConsumer.accept(student.getName(), student.getActivities()));
    }

    public static void printNameAndActivities(){

    }

    static void printUsingBiConsumer(String a, String b){
        BiConsumer<String, String> biconsumer = (p, q) -> {
            System.out.println("a: " + p + ", b: "+ q);
        };
        biconsumer.accept(a, b);
    }
}
