package com.codeprep.streams.reduce;

import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/*
 * The reduce() function in Java Streams is a terminal operation that performs
 * a reduction on the elements of the stream using an associative accumulation function.
 *
 * Key characteristics:
 * - Combines elements of a stream into a single result
 * - Takes a BinaryOperator as an accumulator function
 * - Can optionally take an identity value (initial value)
 * - Returns an Optional<T> (without identity) or T (with identity)
 *
 * Common use cases:
 * - Sum of numbers: stream.reduce(0, (a, b) -> a + b)
 * - Product of numbers: stream.reduce(1, (a, b) -> a * b)
 * - Finding max/min: stream.reduce(Integer::max)
 * - String concatenation: stream.reduce("", String::concat)
 */
public class ReduceExample {

    public static void main(String[] args) {

        List<Integer> integers = Arrays.asList(1, 3, 5, 7);
        Integer result = integers.stream().reduce(1, (a, b) -> a * b);
        System.out.println(result);

        Optional<Integer> withoutIdentity = integers.stream().reduce((a, b) -> a * b);
        System.out.println(withoutIdentity.get());

        // Example to get the student with the highest GPA

        BinaryOperator<Student> getStudentByGPA = (s1, s2) -> {
          if(s1.getGpa() >= s2.getGpa()){
              return s1;
          }else{
              return s2;
          }
        };

        Optional<Student> student = StudentDataBase.getAllStudents().stream()
                .reduce(getStudentByGPA);
        System.out.println(student);
    }
}
