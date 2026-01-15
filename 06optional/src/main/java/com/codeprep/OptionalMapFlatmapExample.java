package com.codeprep;

import com.codeprep.data.Bike;
import com.codeprep.data.Student;
import com.codeprep.data.StudentDataBase;

import java.util.Optional;

public class OptionalMapFlatmapExample {

    // filter
    public static void optionalFilter(){
        Optional<Student> studentOptional = Optional.ofNullable(StudentDataBase.studentSupplier.get());
        studentOptional.filter(student -> student.getGpa() >= 3.5)
                .ifPresent(System.out::println);
    }

    // map
    public static void optionalMap(){
        Optional<Student> studentOptional = Optional.ofNullable(StudentDataBase.studentSupplier.get());
        studentOptional.filter(student -> student.getGpa() >= 3.5)
                .map(Student::getName)
                .ifPresent(System.out::println);
    }

    /**
     * Demonstrates the flatMap() method on Optional.
     * flatMap() is used when the mapping function itself returns an Optional.
     * Unlike map(), flatMap() does NOT wrap the result in another Optional.
     * This prevents nested Optionals like Optional<Optional<T>>.
     * Here, Student::getBike returns Optional<Bike>, so we use flatMap() to flatten it.
     */
    public static void optionalFlatmap(){
        Optional<Student> studentOptional = Optional.ofNullable(StudentDataBase.studentSupplier.get());
        Optional<String> optionalBikeName = studentOptional.filter(student -> student.getGpa() >= 3.5)
                .flatMap(Student::getBike)
                .map(Bike::getName);
        System.out.println(optionalBikeName.get());
    }

    public static void main(String[] args) {

        optionalFilter();

        optionalMap();

        optionalFlatmap();
    }
}
