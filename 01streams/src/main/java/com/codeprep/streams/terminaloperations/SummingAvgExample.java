package com.codeprep.streams.terminaloperations;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.stream.Collectors;

public class SummingAvgExample {
    public static void main(String[] args) {

        Integer count = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.summingInt(Student::getNoteBooks));
        System.out.println(count);

        Double avgNoOfNotebooks = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.averagingInt(Student::getNoteBooks));
        System.out.println(avgNoOfNotebooks);
    }
}
