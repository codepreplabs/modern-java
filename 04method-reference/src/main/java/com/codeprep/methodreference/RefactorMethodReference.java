package com.codeprep.methodreference;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.function.Predicate;

public class RefactorMethodReference {

    private static final Predicate<Student> isGreaterThanOrEqualToThirdGrade = (s) -> s.getGradeLevel() >= 3;

    // we can refactor this and write using a method reference as below

    public static boolean greaterThanGradeLevel(Student student){
        return student.getGradeLevel() >= 3;
    }

    private static final Predicate<Student> isGreaterThanOrEqualToThirdGradeMethRef = RefactorMethodReference::greaterThanGradeLevel;

    public static void main(String[] args) {
        System.out.println(isGreaterThanOrEqualToThirdGrade.test(StudentDataBase.studentSupplier.get()));

        System.out.println(isGreaterThanOrEqualToThirdGradeMethRef.test(StudentDataBase.studentSupplier.get()));
    }
}
