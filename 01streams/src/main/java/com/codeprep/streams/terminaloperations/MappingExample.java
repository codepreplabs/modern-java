package com.codeprep.streams.terminaloperations;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Collectors.mapping() - Downstream Collector for Element Transformation
 *
 * The mapping() collector is used as a downstream collector that adapts a collector
 * to accept elements of a different type by applying a mapping function before accumulation.
 *
 * Signature:
 *   Collectors.mapping(Function<T,U> mapper, Collector<U,A,R> downstream)
 *
 * Parameters:
 *   - mapper: Function that transforms input elements to a new type
 *   - downstream: Collector that processes the mapped elements
 *
 * Common Use Cases:
 *   1. With groupingBy() - Transform elements before grouping
 *      Example: groupingBy(Person::getCity, mapping(Person::getName, toList()))
 *      Groups people by city, but collects only their names
 *
 *   2. With partitioningBy() - Transform elements in each partition
 *      Example: partitioningBy(p -> p.getAge() > 18, mapping(Person::getName, toSet()))
 *      Partitions by age, collecting names as a Set
 *
 *   3. Nested transformations - Apply multiple mapping operations
 *      Example: mapping(String::toUpperCase, mapping(String::trim, toList()))
 *
 * Key Benefits:
 *   - Allows transformation and collection in a single operation
 *   - Composable with other collectors for complex aggregations
 *   - More efficient than separate map() and collect() operations in grouped contexts
 */
public class MappingExample {

    public static void main(String[] args) {

        // Example 1: mapping() with groupingBy() - Transform elements after grouping
        // Group students by grade level, but collect only their names (not the whole Student object)
        System.out.println("Example 2: mapping() with groupingBy() - Collect names grouped by grade");
        Map<Integer, List<String>> namesByGrade = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getGradeLevel,
                        Collectors.mapping(Student::getName, Collectors.toList())
                ));
        System.out.println(namesByGrade);
        System.out.println();

        // Example 2: mapping() with partitioningBy() - Transform elements in each partition
        // Partition students by GPA >= 3.8, but collect only their names
        System.out.println("Example 3: mapping() with partitioningBy() - Names of high/low GPA students");
        var namesByHighGPA = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.partitioningBy(
                        student -> student.getGpa() >= 3.8,
                        Collectors.mapping(Student::getName, Collectors.toList())
                ));
        System.out.println("High GPA (>= 3.8): " + namesByHighGPA.get(true));
        System.out.println("Lower GPA (< 3.8): " + namesByHighGPA.get(false));
        System.out.println();

        // Example 3: Multiple level mapping - Collect all activities grouped by grade
        // This flattens the activities list for each student in each grade
        System.out.println("Example 4: Collecting all activities grouped by grade");
        var activitiesByGrade = StudentDataBase.getAllStudents()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getGradeLevel,
                        Collectors.flatMapping(
                                student -> student.getActivities().stream(),
                                Collectors.toList()
                        )
                ));
        System.out.println(activitiesByGrade);
    }
}
