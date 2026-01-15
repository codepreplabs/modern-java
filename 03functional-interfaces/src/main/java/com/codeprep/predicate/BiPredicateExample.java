package com.codeprep.predicate;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BiPredicateExample {

    private final List<Student> students = StudentDataBase.getAllStudents();

    private static final Predicate<Student> isStudentInThirdGrade = (student) -> student.getGradeLevel() == 3;

    public static void main(String[] args) {
        // example for BiPredicate

        BiPredicate<Integer, Double> gradeAndGpaPredicate =
            (gradeLevel, gpa) -> gradeLevel >= 3 && gpa >= 4;

        BiPredicateExample example = new BiPredicateExample();

        System.out.println("Students with grade level >= 3 and GPA >= 3.5:");
        example.students.forEach(student -> {
            if (gradeAndGpaPredicate.test(student.getGradeLevel(), student.getGpa())) {
                System.out.println(student.getName() + " - Grade: "
                    + student.getGradeLevel() + ", GPA: " + student.getGpa());
            }
        });

        // BiPredicate to check if student's GPA is above threshold AND gender matches
        BiPredicate<Double, String> gpaAndGenderPredicate =
            (gpa, gender) -> gpa >= 4 && gender.equals("male");

        System.out.println("\nMale students with GPA >= 3.9:");
        example.students.forEach(student -> {
            if (gpaAndGenderPredicate.test(student.getGpa(), student.getGender())) {
                System.out.println(student.getName() + " - GPA: " + student.getGpa());
            }
        });
    }
}
