package com.codeprep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Local Variable Type Inference (var keyword) - Introduced in Java 10
 *
 * Local variable type inference allows developers to declare local variables without
 * explicitly specifying their type. The compiler infers the type from the initializer.
 *
 * Key Points:
 * - Only works with local variables (not fields, method parameters, or return types)
 * - Must be initialized at declaration (var x; is invalid)
 * - Cannot be initialized with null (var x = null; is invalid)
 * - Improves code readability by reducing verbosity
 * - Does NOT make Java dynamically typed - type is still checked at compile time
 *
 * Common Use Cases:
 *
 * 1. Simple Type Declarations:
 *    var message = "Hello World";  // String
 *    var count = 10;                // int
 *    var price = 99.99;             // double
 *
 * 2. Complex Generic Types:
 *    var list = new ArrayList<String>();
 *    var map = new HashMap<String, List<Integer>>();
 *    Instead of: HashMap<String, List<Integer>> map = new HashMap<>();
 *
 * 3. Loop Variables:
 *    for (var item : collection) { }
 *    for (var i = 0; i < 10; i++) { }
 *
 * 4. Try-with-resources:
 *    try (var reader = new BufferedReader(new FileReader("file.txt"))) { }
 *
 * 5. Stream Operations:
 *    var result = list.stream()
 *                     .filter(x -> x > 10)
 *                     .collect(Collectors.toList());
 *
 * 6. Anonymous Classes and Lambda Return Types:
 *    var runnable = new Runnable() { ... };
 *
 * When NOT to Use:
 * - When the type is not obvious from the right-hand side
 * - With diamond operator alone: var list = new ArrayList<>(); (unclear type)
 * - When it reduces code readability
 * - For method parameters, return types, or class fields
 */
public class LocalVariableTypeInferenceExample {

    static void main() {

        // Example 1: Simple type declarations
        var message = "Hello World";  // String
        var count = 10;                // int
        var price = 99.99;             // double
        var isActive = true;           // boolean

        System.out.println("Message: " + message);
        System.out.println("Count: " + count);
        System.out.println("Price: $" + price);
        System.out.println("Is Active: " + isActive);

        // Example 2: Complex Generic Types - Before and After
        // After var - much cleaner!
        var users = new HashMap<String, Map<String, String>>();
        users.put("user1", Map.of("name", "John", "email", "john@example.com"));
        users.put("user2", Map.of("name", "Jane", "email", "jane@example.com"));

        System.out.println("Users: " + users);

        // Example 3: Collections with var
        var list = List.of("adam", "dilip", "charlie", "eve");
        var names = new ArrayList<String>();
        names.add("Alice");
        names.add("Bob");

        System.out.println("List: " + list);
        System.out.println("Names: " + names);

        // Example 4: Loop variables
        System.out.println("\nIterating with var:");
        for (var name : list) {
            System.out.println("Name: " + name);
        }

        for (var i = 0; i < 3; i++) {
            System.out.println("Counter: " + i);
        }

        // Example 5: Stream operations
        var filteredList = list.stream()
                .filter(name -> name.length() > 4)
                .toList();

        System.out.println("\nFiltered names (length > 4): " + filteredList);

        var upperCaseNames = list.stream()
                .map(String::toUpperCase)
                .toList();

        System.out.println("Uppercase names: " + upperCaseNames);

        // Example 6: Map operations with var
        var productPrices = new HashMap<String, Double>();
        productPrices.put("Laptop", 999.99);
        productPrices.put("Mouse", 29.99);
        productPrices.put("Keyboard", 79.99);

        System.out.println("\nProduct Prices:");
        for (var entry : productPrices.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }

        // Example 7: Anonymous class with var
        var runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("\nRunnable executing with var!");
            }
        };
        runnable.run();

        // Example 8: Try-with-resources
        System.out.println("\nReading from file with var:");
        try (var inputStream = LocalVariableTypeInferenceExample.class
                .getClassLoader()
                .getResourceAsStream("example.txt")) {
            assert inputStream != null;
            try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {

                var line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // Example 9: Nested collections
        var studentGrades = new HashMap<String, List<Integer>>();
        studentGrades.put("John", List.of(85, 90, 92));
        studentGrades.put("Jane", List.of(95, 88, 91));

        System.out.println("\nStudent Grades: " + studentGrades);

        // Example 10: StringBuilder with var
        var builder = new StringBuilder();
        builder.append("Hello").append(" ").append("from").append(" ").append("var!");
        var result = builder.toString();

        System.out.println("\nString builder result: " + result);

        // ===== LIMITATIONS OF VAR =====
        System.out.println("\n===== Demonstrating Limitations of var =====");

        // Limitation 1: Cannot be used without initialization
        // var x;  // COMPILE ERROR: Cannot use 'var' on variable without initializer
        var x = 10; // Must initialize

        // Limitation 2: Cannot be initialized with null
        // var nullValue = null;  // COMPILE ERROR: Cannot infer type for 'var'
        var nullValue = (String) null; // Must cast to specific type
        System.out.println("Null value (with cast): " + nullValue);

        // Limitation 3: Cannot use with diamond operator alone
        // var ambiguousList = new ArrayList<>();  // COMPILE ERROR: Cannot infer type argument(s)
        var properList = new ArrayList<String>(); // Must specify type
        System.out.println("Proper list: " + properList);

        // Limitation 4: Cannot be used for fields (instance or class variables)
        // This would need to be declared outside the method and would fail:
        // var instanceField = "test";  // COMPILE ERROR

        // Limitation 5: Cannot be used for method parameters
        // Example: public void process(var input) { }  // COMPILE ERROR

        // Limitation 6: Cannot be used for method return types
        // Example: public var getResult() { return 42; }  // COMPILE ERROR

        // Limitation 7: Cannot be used in compound declarations
        // var a = 1, b = 2;  // COMPILE ERROR: 'var' is not allowed in compound declaration

        // Limitation 8: Cannot be used with array initializer
        // var numbers = {1, 2, 3};  // COMPILE ERROR: Array initializer needs an explicit target-type
        var numbers = new int[]{1, 2, 3}; // Must use explicit array creation
        System.out.println("Numbers array length: " + numbers.length);

        // Limitation 9: Lambda expressions need explicit target type
        // var lambda = (String s) -> s.toUpperCase();  // COMPILE ERROR: Cannot infer type
        // Must use explicit type:
        java.util.function.Function<String, String> function = s -> s.toUpperCase();
        System.out.println("Lambda result: " + function.apply("hello"));

        // Limitation 10: Method reference needs context
        // var methodRef = String::toUpperCase;  // COMPILE ERROR: Cannot infer type
        java.util.function.Function<String, String> methodRef = String::toUpperCase;
        System.out.println("Method reference result: " + methodRef.apply("world"));

        // Limitation 11: Ternary operator with incompatible types
        var ternaryResult = true ? "String" : "Another String"; // OK - both are String
        // var badTernary = true ? "String" : 123;  // Type would be Serializable & Comparable - not intuitive

        // Limitation 12: Reduces readability when type is not obvious
        var obscure = getComplexObject(); // What type is this? Hard to tell without IDE
        System.out.println("Obscure type class: " + obscure.getClass().getSimpleName());

        // Best Practice: Use var when the type is obvious from the right-hand side
        var obviousString = "This is clearly a String";
        var obviousList = new ArrayList<String>();
        var obviousMap = new HashMap<Integer, String>();

        System.out.println("\n===== End of var Limitations Demo =====");
    }

    // Helper method to demonstrate limitation 12
    private static Object getComplexObject() {
        return new java.util.Date();
    }
}
