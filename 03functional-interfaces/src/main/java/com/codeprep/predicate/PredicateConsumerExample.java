package com.codeprep.predicate;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredicateConsumerExample {

    private final List<Student> students = StudentDataBase.getAllStudents();

    private final Predicate<Student> isGradeLevelThree = (student) -> student.getGradeLevel() == 3;
    private final Predicate<Student> isGpaGreaterThenThree = (student) -> student.getGpa() > 3;

    private final BiConsumer<String, List<String>> printStudentNameAndActivities = (name, activities) -> System.out.println("name: "+ name + " activities: "+ activities);

    public void printGradeThreeStudentsWithGpaGreaterThanThree(){
        students.forEach(student -> {
            if(isGradeLevelThree.and(isGpaGreaterThenThree).test(student)){
                printStudentNameAndActivities.accept(student.getName(), student.getActivities());
            }
        });
    }

    public static void main(String[] args) {
        PredicateConsumerExample example = new PredicateConsumerExample();
        example.printGradeThreeStudentsWithGpaGreaterThanThree();
    }
}
