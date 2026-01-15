package com.codeprep.function;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BiFunctionExample {

    private static final List<Student> students = StudentDataBase.getAllStudents();
    private static final Predicate<Student> isStudentInThirdGrade = (student) -> student.getGradeLevel() == 3;

    private static final BiFunction<List<Student>, Predicate<Student>, Map<String, Double>> getStudentsByCriteria
            = ((students, studentPredicate) -> {
                Map<String, Double> studentGradeMap = new HashMap<>();
                students.forEach(student -> {
                    if(studentPredicate.test(student)){
                        studentGradeMap.put(student.getName(), student.getGpa());
                    }
                });
                return studentGradeMap;
    });

    public static void main(String[] args) {
        System.out.println(getStudentsByCriteria.apply(students, isStudentInThirdGrade));
    }
}
