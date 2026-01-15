package constructorreference;

/*
* The syntax is almost the same as the method reference, but here we have the new keyword
* after the double colon
*
* Syntax:
* className::new
* */

import com.codeprep.data.Student;

import java.util.function.Function;
import java.util.function.Supplier;

public class ConstructorReference {

    // Constructor reference for no-arg constructor
    private static final Supplier<Student> studentSupplier = Student::new;

    // Constructor reference for constructor with String parameter
    private static final Function<String, Student> studentFunction = Student::new;

    public static void main(String[] args) {
        // Using no-arg constructor reference
        System.out.println("Using Supplier (no-arg constructor):");
        Student student1 = studentSupplier.get();
        System.out.println(student1);

        // Using constructor reference with String parameter
        System.out.println("\nUsing Function (constructor with String parameter):");
        Student student2 = studentFunction.apply("John Doe");
        System.out.println(student2);
    }
}
