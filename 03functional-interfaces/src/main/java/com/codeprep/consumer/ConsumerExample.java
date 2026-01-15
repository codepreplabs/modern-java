package com.codeprep.consumer;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.function.Consumer;

public class ConsumerExample {

    public static List<Student> students = StudentDataBase.getAllStudents();

    public static Consumer<Student> printStudentConsumer = (System.out::println);
    public static Consumer<Student> printNameConsumer = (student -> System.out.println(student.getName()));
    public static Consumer<Student> printActivitiesConsumer = (student -> System.out.println(student.getActivities()));

    public static void main(String[] args) {

        consumerExample();

        // example of a real world use case of Consumer interface
        printName();

        // consumer chaining example
        printNameAndActivities();

        printNameAndActivitiesWithFilter();
    }

    private static void printNameAndActivitiesWithFilter() {
        students.forEach(student -> {
            if(student.getGradeLevel() >= 3){
                printNameConsumer.andThen(printActivitiesConsumer).accept(student);
            }
        });
    }

    private static void consumerExample() {
        Consumer<String> upperCaseConsumer = message -> System.out.println("Consumed message: " + message.toUpperCase());
        upperCaseConsumer.accept("hello world");
    }

    private static void printNameAndActivities() {
        students.forEach(printNameConsumer.andThen(printActivitiesConsumer));

    }

    public static void printName(){
        students.forEach(printStudentConsumer);
    }
}
