package com.codeprep;

/*
 * ENHANCED SWITCH EXPRESSIONS IN JAVA (Java 14+)
 *
 * Enhanced Switch is a feature introduced in Java 12 (preview) and made standard in Java 14.
 * It extends the traditional switch statement with new capabilities and cleaner syntax.
 *
 * KEY FEATURES:
 *
 * 1. SWITCH EXPRESSIONS:
 *    - Can return a value directly
 *    - Use arrow syntax (->) instead of colon (:)
 *    - No fall-through behavior (no need for break statements)
 *
 * 2. MULTIPLE CASE LABELS:
 *    - Combine multiple cases in a single line using comma separation
 *    - Reduces code duplication
 *
 * 3. YIELD KEYWORD:
 *    - Used to return a value from a switch expression block
 *    - Allows complex logic within case blocks
 *
 * 4. PATTERN MATCHING (Java 17+):
 *    - Test and extract components in a single operation
 *    - Eliminates explicit type casting
 *
 * 5. NULL HANDLING (Java 17+):
 *    - Can handle null cases explicitly
 *    - Prevents NullPointerException
 *
 * SYNTAX COMPARISON:
 *
 * Traditional Switch:
 *   switch(value) {
 *       case 1:
 *           result = "One";
 *           break;
 *       case 2:
 *           result = "Two";
 *           break;
 *       default:
 *           result = "Other";
 *   }
 *
 * Enhanced Switch Expression:
 *   result = switch(value) {
 *       case 1 -> "One";
 *       case 2 -> "Two";
 *       default -> "Other";
 *   };
 *
 * USE CASES:
 *
 * 1. SIMPLIFYING CONDITIONALS:
 *    - Replace verbose if-else chains with concise switch expressions
 *    - Make code more readable and maintainable
 *
 * 2. ENUM HANDLING:
 *    - Map enum values to results cleanly
 *    - Better type safety and exhaustiveness checking
 *
 * 3. TYPE PATTERN MATCHING:
 *    - Handle different types in a type-safe manner
 *    - Eliminate instanceof checks and casting
 *
 * 4. BUSINESS LOGIC:
 *    - Calculate values based on multiple conditions
 *    - Return different results based on input
 *
 * 5. CONFIGURATION MAPPING:
 *    - Map configuration values to behavior
 *    - Handle different modes or states
 *
 * ADVANTAGES:
 * - More concise and readable code
 * - No fall-through bugs (common with traditional switch)
 * - Expression-based (can be used on right-hand side of assignment)
 * - Better null safety
 * - Compiler enforces exhaustiveness for enums
 * - Supports pattern matching (Java 17+)
 */
public class EnhancedSwithExample {
    static void main(String[] args) {
        System.out.println("=== ENHANCED SWITCH EXPRESSIONS EXAMPLES ===\n");

        // Example 1: Basic Switch Expression
        basicSwitchExample(3);

        // Example 2: Multiple Case Labels
        multipleCaseLabelsExample("June");

        // Example 3: Switch with Yield
        switchWithYieldExample();

        // Example 4: Enum Handling
        enumSwitchExample();

        // Example 5: Traditional vs Enhanced
        comparisonExample();

        // Example 6: Pattern Matching (Java 17+)
        patternMatchingExample();
    }

    // Example 1: Basic Switch Expression
    static void basicSwitchExample(int dayOfWeek) {
        System.out.println("=== Example 1: Basic Switch Expression ===");

        String dayName = switch (dayOfWeek) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Invalid day";
        };

        System.out.println("Day " + dayOfWeek + " is: " + dayName);
        System.out.println();
    }

    // Example 2: Multiple Case Labels
    static void multipleCaseLabelsExample(String month) {
        System.out.println("=== Example 2: Multiple Case Labels ===");

        String season = switch (month) {
            case "December", "January", "February" -> "Winter";
            case "March", "April", "May" -> "Spring";
            case "June", "July", "August" -> "Summer";
            case "September", "October", "November" -> "Fall";
            default -> "Unknown season";
        };

        System.out.println(month + " is in: " + season);
        System.out.println();
    }

    // Example 3: Switch with Yield (for complex logic)
    static void switchWithYieldExample() {
        System.out.println("=== Example 3: Switch with Yield ===");

        int score = 85;

        String grade = switch (score / 10) {
            case 10, 9 -> {
                System.out.println("  Excellent performance!");
                yield "A";
            }
            case 8 -> {
                System.out.println("  Great job!");
                yield "B";
            }
            case 7 -> {
                System.out.println("  Good work!");
                yield "C";
            }
            case 6 -> {
                System.out.println("  Satisfactory.");
                yield "D";
            }
            default -> {
                System.out.println("  Needs improvement.");
                yield "F";
            }
        };

        System.out.println("Score: " + score + " -> Grade: " + grade);
        System.out.println();
    }

    // Example 4: Enum Handling
    enum DayType {
        WEEKDAY, WEEKEND
    }

    static void enumSwitchExample() {
        System.out.println("=== Example 4: Enum Switch ===");

        DayType today = DayType.WEEKDAY;

        String activity = switch (today) {
            case WEEKDAY -> "Time to work!";
            case WEEKEND -> "Time to relax!";
        };

        System.out.println("Today is a " + today + ": " + activity);
        System.out.println();
    }

    // Example 5: Traditional vs Enhanced Switch
    static void comparisonExample() {
        System.out.println("=== Example 5: Traditional vs Enhanced Switch ===");

        String operation = "ADD";
        int a = 10, b = 5;

        // OLD WAY - Traditional Switch Statement
        int resultOld;
        switch (operation) {
            case "ADD":
                resultOld = a + b;
                break;
            case "SUBTRACT":
                resultOld = a - b;
                break;
            case "MULTIPLY":
                resultOld = a * b;
                break;
            case "DIVIDE":
                resultOld = a / b;
                break;
            default:
                resultOld = 0;
                break;
        }

        // NEW WAY - Enhanced Switch Expression
        int resultNew = switch (operation) {
            case "ADD" -> a + b;
            case "SUBTRACT" -> a - b;
            case "MULTIPLY" -> a * b;
            case "DIVIDE" -> a / b;
            default -> 0;
        };

        System.out.println("Traditional switch result: " + resultOld);
        System.out.println("Enhanced switch result: " + resultNew);
        System.out.println("Both produce the same result, but enhanced switch is cleaner!");
        System.out.println();
    }

    // Example 6: Pattern Matching with Switch (Java 17+)
    static void patternMatchingExample() {
        System.out.println("=== Example 6: Pattern Matching (Java 17+) ===");

        Object obj1 = "Hello";
        Object obj2 = 42;
        Object obj3 = 3.14;

        processObject(obj1);
        processObject(obj2);
        processObject(obj3);

        System.out.println();
    }

    static void processObject(Object obj) {
        String result = switch (obj) {
            case String s -> "String of length " + s.length() + ": " + s;
            case Integer i -> "Integer value: " + i;
            case Double d -> "Double value: " + d;
            case null -> "Null value";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };

        System.out.println(result);
    }
}
