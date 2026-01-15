package com.codeprep.predicate;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.function.Predicate;

/*
* Predicate interface has a test method accepts an input, and it is going to perform some kind
* of operation on that input, and then it is going to return a Boolean.
*
* It also supports and, or and negate operation
*/
public class PredicateExample {

    private static final List<Student> students = StudentDataBase.getAllStudents();
    private static final Predicate<Integer> checkEven = (i) -> i % 2 == 0;
    private static final Predicate<Integer> checkDivisibleByFive = (i) -> i % 5 == 0;

    private static final Predicate<Student> isStudentInThirdGrade = (student) -> student.getGradeLevel() == 3;

    public static void main(String[] args) {

        // simple predicate example
        System.out.println(checkEven.test(4));

        // example of the and() method
        System.out.println(checkEven.and(checkDivisibleByFive).test(10));
        System.out.println(checkEven.and(checkDivisibleByFive).test(8));

        // example of or() method
        System.out.println(checkEven.or(checkDivisibleByFive).test(8));

        // example of negate() method
        System.out.println(checkEven.and(checkDivisibleByFive).negate().test(10));

        // real world examples
        System.out.println("total students: " + students.size());
        List<Student> grade3Students = students.stream().filter(isStudentInThirdGrade).toList();
        System.out.println("grade 3 students: " + grade3Students.size());

        students.forEach(student -> {
            if(isStudentInThirdGrade.test(student)){
                System.out.println(student);
            }
        });
    }
}
