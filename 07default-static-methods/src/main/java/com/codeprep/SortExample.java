package com.codeprep;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class SortExample {

    private static final Consumer<Student> studentConsumer = System.out::println;

    public static void sortByName(List<Student> students){
        System.out.println("After sort by Name");
        Comparator<Student> nameComparator = Comparator.comparing(Student::getName);
        students.sort(nameComparator);
    }

    public static void sortByGPA(List<Student> students){
        System.out.println("After sort by GPA");
        Comparator<Student> nameComparator = Comparator.comparingDouble(Student::getGpa).reversed();
        students.sort(nameComparator);
    }

    public static void sortByNameAndGPA(List<Student> students){
        Comparator<Student> nameComparator = Comparator.comparing(Student::getName);
        Comparator<Student> gpaComparator = Comparator.comparingDouble(Student::getGpa);

        System.out.println("After sorting by name and gpa");
        students.sort(gpaComparator.reversed().thenComparing(nameComparator));
        students.forEach(studentConsumer);
    }

    public static void sortWithNullsFirst(List<Student> students){
        System.out.println("After sort by Name with nulls first");
        Comparator<Student> nameComparator = Comparator.comparing(Student::getName);
        Comparator<Student> nullsFirstComparator = Comparator.nullsFirst(nameComparator);
        students.sort(nullsFirstComparator);
        students.forEach(studentConsumer);
    }

    public static void sortWithNullsLast(List<Student> students){
        System.out.println("After sort by Name with nulls last");
        Comparator<Student> nameComparator = Comparator.comparing(Student::getName);
        Comparator<Student> nullsLastComparator = Comparator.nullsLast(nameComparator);
        students.sort(nullsLastComparator);
        students.forEach(studentConsumer);
    }

    public static void main(String[] args) {

        List<Student> students = StudentDataBase.getAllStudents();
        System.out.println("before sort");
        students.forEach(studentConsumer);

        sortByName(students);
        students.forEach(studentConsumer);

        sortByGPA(students);
        students.forEach(studentConsumer);

        sortByNameAndGPA(students);
        students.forEach(studentConsumer);

        // Create a list with some null students to demonstrate nullsFirst and nullsLast
        System.out.println("\n========== Demonstrating nullsFirst and nullsLast ==========");
        List<Student> studentsWithNulls = new java.util.ArrayList<>(StudentDataBase.getAllStudents());
        studentsWithNulls.add(2, null); // Add null at index 2
        studentsWithNulls.add(5, null); // Add null at index 5
        studentsWithNulls.add(null);    // Add null at end

        System.out.println("\nBefore sorting (with nulls):");
        studentsWithNulls.forEach(System.out::println);

        sortWithNullsFirst(studentsWithNulls);

        sortWithNullsLast(studentsWithNulls);
    }
}
