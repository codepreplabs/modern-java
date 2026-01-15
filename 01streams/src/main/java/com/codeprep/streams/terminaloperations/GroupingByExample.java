package com.codeprep.streams.terminaloperations;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.*;
import java.util.stream.Collectors;

/*
 * groupingBy() is a terminal operation in Java Streams that groups elements based on a classifier function.
 * It returns a Collector that implements a "group by" operation on input elements of type T,
 * grouping elements according to a classification function, and returning the results in a Map.
 *
 * Common Use Cases:
 * 1. Simple Grouping: Collectors.groupingBy(classifier)
 *    - Groups elements by a property and returns Map<K, List<T>>
 *    - Example: Group employees by department
 *
 * 2. Grouping with Downstream Collector: Collectors.groupingBy(classifier, downstream)
 *    - Groups elements and applies another collector to the grouped values
 *    - Example: Count employees in each department, get average salary per department
 *
 * 3. Grouping with Custom Map: Collectors.groupingBy(classifier, mapFactory, downstream)
 *    - Groups elements with a specific Map implementation (TreeMap, LinkedHashMap, etc.)
 *    - Useful when you need sorted keys or insertion order
 *
 * Syntax:
 * - Collectors.groupingBy(Function<T, K> classifier)
 * - Collectors.groupingBy(Function<T, K> classifier, Collector<T, A, D> downstream)
 * - Collectors.groupingBy(Function<T, K> classifier, Supplier<Map> mapFactory, Collector<T, A, D> downstream)
 *
 * Common Downstream Collectors:
 * - counting(): Count elements in each group
 * - summingInt/Long/Double(): Sum values in each group
 * - averagingInt/Long/Double(): Calculate average in each group
 * - mapping(): Transform elements before collecting
 * - toSet(): Collect elements as Set instead of List
 * - maxBy/minBy(): Find max/min element in each group
 */
public class GroupingByExample {

    public static void main(String[] args) {

        // Examples on Collectors.groupingBy(Function<T, K> classifier)

        Map<String, List<Student>> studentByGenderMap = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(Student::getGender));
        System.out.println(studentByGenderMap);

        Map<String, List<Student>> studentByGPA = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(student -> student.getGpa() >= 3.8 ? "OUTSTANDING" : "AVERAGE"));
        System.out.println(studentByGPA);

        // Examples on Collectors.groupingBy(Function<T, K> classifier, Collector<T, A, D> downstream)

        Map<String, Map<String, List<Student>>> studentsByGenderAndGPA = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(Student::getGender, Collectors.groupingBy(student -> student.getGpa() >= 3.8 ? "OUTSTANDING" : "AVERAGE")));
        System.out.println(studentsByGenderAndGPA);

        Map<String, Integer> studentsByNameAndNotebooks = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(Student::getName, Collectors.summingInt(Student::getNoteBooks)));
        System.out.println(studentsByNameAndNotebooks);

        // Collectors.groupingBy(Function<T, K> classifier, Supplier<Map> mapFactory, Collector<T, A, D> downstream)

        /*
        * The first one determines the key here.
        * The second parameter determines the output.
        * What kind of output it is, what kind of connection it is going to return.
        * And third parameter determines what is a value for the output that it is going to generate.*/

        LinkedHashMap<String, Set<Student>> studentMap = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.groupingBy(Student::getName, LinkedHashMap::new, Collectors.toSet()));
        System.out.println(studentMap);

        // maxBy() in groupingBy

        Map<Integer, Optional<Student>> studentByGradeWithMaxGPA = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(Student::getGradeLevel, Collectors.maxBy(Comparator.comparing(Student::getGpa))));
        System.out.println(studentByGradeWithMaxGPA);

        // collectingAndThen()

        Map<Integer, Student> studentByGradeWithMaxGPAUsingCollectingAndThen = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(Student::getGradeLevel, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Student::getGpa)), Optional::get)));
        System.out.println(studentByGradeWithMaxGPAUsingCollectingAndThen);

        // minBy() in groupingBy

        Map<Integer, Optional<Student>> studentByGradeWithMinGPA = StudentDataBase.getAllStudents().stream()
                .collect(Collectors.groupingBy(Student::getGradeLevel, Collectors.minBy(Comparator.comparing(Student::getGpa))));
        System.out.println(studentByGradeWithMinGPA);
    }
}
