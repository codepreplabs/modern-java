package com.codeprep.performance;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;

public class ParallelStreamPerformance {

    public static List<String> sequentialStream(){

        long start = System.currentTimeMillis();
        List<String> studentActivities = StudentDataBase.getAllStudents()
                .stream()
                .map(Student::getActivities) //Stream<List<String>>
                .flatMap(List::stream) //<Stream<String>
                .toList();

        long duration = System.currentTimeMillis()-start;
        System.out.println("Duration in sequential stream : "+ duration);
        return studentActivities;
    }

    public static List<String> parallelStream(){

        long start = System.currentTimeMillis();
        List<String> studentActivities = StudentDataBase.getAllStudents()
                .parallelStream()
                .map(Student::getActivities) //Stream<List<String>>
                .flatMap(List::stream) //<Stream<String>
                .toList();

        long duration = System.currentTimeMillis()-start;
        System.out.println("Duration in parallel stream : "+ duration);
        return studentActivities;

    }

    public static void main(String[] args) {
        System.out.println("sequentialStream : " + sequentialStream());
        System.out.println("parallelStream : " + parallelStream());
    }
}
