package com.codeprep;

/*
 * Pattern Matching in Java
 * ========================
 * Pattern matching is a feature that allows you to extract components from objects in a concise way.
 * It simplifies code by combining type checking and casting into a single operation.
 *
 * Key Features:
 * -------------
 * 1. Pattern Matching for instanceof (JDK 14-16)
 *    - Eliminates explicit casting after instanceof checks
 *    - Pattern variable is automatically declared and scoped
 *
 * 2. Pattern Matching for switch (JDK 17+)
 *    - Allows switching on any type, not just primitives and strings
 *    - Supports multiple patterns and guards
 *    - Works seamlessly with sealed classes and records
 *
 * Common Use Cases:
 * -----------------
 * - Type-safe down-casting: Checking object type and using it without explicit cast
 * - Polymorphic dispatch: Handling different types in a switch statement
 * - Data extraction: Decomposing complex objects (with record patterns in JDK 19+)
 * - Validation logic: Combining type checks with conditional guards
 * - Visitor pattern replacement: Simplifying type-based operations
 * - API response handling: Processing different response types elegantly
 * - Command pattern: Dispatching different command types
 *
 * Benefits:
 * ---------
 * - Reduces boilerplate code
 * - Improves code readability and maintainability
 * - Eliminates ClassCastException risks
 * - Makes code more declarative
 * - Better compiler support for exhaustiveness checking
 */
public class PatternMatchingExample {

    public static String priorToPatternMatching(Object o){
        if(o instanceof Integer){
            var i = (Integer) o;
            return "Integer:" + i;
        }
        if(o instanceof String){
            var i = (String) o;
            return "String of length: " + i.length();
        }
        return "Not a string or an Integer";
    }

    /*
    * Checks the type, Casts the type and creates a binding variable if it's a match
    * This kind of pattern matching is called Type patterns
    * there are other patterns like record patterns and guarded patterns (Java 21)
    * */
    public static String patternMatching(Object o){

        if(o instanceof Integer i){
            return "Integer:" + i;
        }
        if(o instanceof String i){
            return "String of length: " + i.length();
        }
        return "Not a string or an Integer";
    }

    public static String patternMatchingUsingSwitch(Object o){
        return switch (o) {
            case String s -> "String is of length: " + s.length();
            case Integer i -> "Integer " + i;
            case null, default -> "Not a String or Integer";
        };
    }

    static void main() {

        String s = priorToPatternMatching(10);
        System.out.println(s);

        String p = patternMatching("Hello");
        System.out.println(p);
    }
}
