package com.codeprep.methodreference;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.function.Consumer;

import static java.lang.System.out;

public class ConsumerMethodReference {

    private static final Consumer<Student> printStudent = out::println;

    private static final Consumer<Student> printActivities = Student::printListOfActivities;

    public static void main(String[] args) {

        StudentDataBase.getAllStudents().forEach(printStudent);

        StudentDataBase.getAllStudents().forEach(printActivities);
    }
}
