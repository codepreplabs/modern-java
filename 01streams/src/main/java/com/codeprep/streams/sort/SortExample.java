package com.codeprep.streams.sort;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;

public class SortExample {

    public static void main(String[] args) {

        // distinct example

        List<String> studentActivities = StudentDataBase.getAllStudents().stream()
                .map(Student::getActivities)
                .flatMap(List::stream)
                .distinct()
                .toList();
        System.out.println(studentActivities);

        // count example

        Long activitiesCount = StudentDataBase.getAllStudents().stream()
                .map(Student::getActivities)
                .flatMap(List::stream)
                .distinct()
                .count();
        System.out.println(activitiesCount);

        // sorted activities

        List<String> sortedActivities = StudentDataBase.getAllStudents().stream()
                .map(Student::getActivities)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .toList();
        System.out.println(sortedActivities);
    }
}
