package com.codeprep;

/*
 * Optional is a container object introduced in Java 8 to handle null values more elegantly.
 *
 * Key benefits:
 * - Reduces NullPointerException risks by making nullability explicit
 * - Encourages better API design by clearly indicating when a value might be absent
 * - Provides functional-style methods like map(), flatMap(), filter(), and orElse()
 *
 * Common methods:
 * - Optional.of(value): Creates an Optional with a non-null value
 * - Optional.ofNullable(value): Creates an Optional that may contain null
 * - Optional.empty(): Creates an empty Optional
 * - isPresent(): Returns true if value is present
 * - ifPresent(consumer): Executes action if value is present
 * - orElse(defaultValue): Returns value or default if empty
 * - orElseThrow(): Returns value or throws exception if empty
 */

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Objects;
import java.util.Optional;

public class OptionalExample {

    public static Optional<String> getStudentNameOptional(){
        Optional<Student> optionalStudent = Optional.of(StudentDataBase.studentSupplier.get());
        if(optionalStudent.isPresent()){
            return optionalStudent.map(Student::getName);
        }
        return Optional.empty();
    }

    public static String getStudentName(){
        Student student = StudentDataBase.studentSupplier.get();
        if(Objects.nonNull(student)){
            return student.getName();
        }
        return null;
    }

    public static void main(String[] args) {

        String name = getStudentName();
        if(Objects.nonNull(name)){
            System.out.println("length of the student name: " + name.length());
        }else {
            System.out.println("Student not present");
        }

        Optional<String> studentNameOptional = getStudentNameOptional();

        if(studentNameOptional.isPresent()){
            System.out.println("the student is : " + studentNameOptional.get());
        }
    }
}