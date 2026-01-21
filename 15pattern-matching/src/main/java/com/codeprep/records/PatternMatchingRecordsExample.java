package com.codeprep.records;

public class PatternMatchingRecordsExample {

    /**
     * Pattern Matching with Sealed Classes:
     *
     * This method demonstrates pattern matching with sealed classes in switch expressions.
     * The Animal interface is a sealed interface that permits only Cat and Dog as subtypes.
     *
     * Why no default clause is needed:
     * - Sealed classes provide an exhaustive list of permitted subtypes at compile-time
     * - The compiler knows that Animal can only be Cat or Dog (defined by 'permits' clause)
     * - By covering all permitted subtypes (Cat and Dog) plus null, the switch is exhaustive
     * - The compiler can verify completeness, eliminating the need for a default case
     * - This provides compile-time safety: if a new subtype is added to the sealed hierarchy,
     *   the compiler will flag all switch statements that need updating
     *
     * Benefits:
     * - Type-safe pattern matching without casting
     * - Compile-time exhaustiveness checking
     * - No runtime surprises from unhandled cases
     * - Refactoring safety when sealed hierarchy changes
     */
    public String retrieveName(Animal animal){
        return switch (animal){
            case null -> "";
            case Cat cat -> cat.name();
            case Dog dog -> dog.name();
        };
    }

    /**
     * Guarded Patterns (Pattern Guards):
     *
     * Guarded patterns allow you to add conditional logic (guards) to case labels using the 'when' clause.
     * This combines pattern matching with boolean expressions for more fine-grained control.
     *
     * Syntax: case Pattern pattern when BooleanExpression -> result
     *
     * Common Use Cases:
     * 1. Null-safety checks: Validate that extracted fields are not null
     *    Example: case Cat cat when cat.name() != null -> process(cat)
     *
     * 2. Range checks: Verify that numeric values fall within acceptable ranges
     *    Example: case Person p when p.age() >= 18 -> "Adult"
     *
     * 3. String validation: Check string properties (length, content, format)
     *    Example: case User u when u.email().contains("@") -> "Valid email"
     *
     * 4. Complex business logic: Apply domain-specific rules
     *    Example: case Order o when o.total() > 1000 && o.isPremium() -> "Priority shipping"
     *
     * 5. Multi-condition filtering: Combine multiple conditions for precise matching
     *    Example: case Product p when p.inStock() && p.price() < 100 -> "Available bargain"
     *
     * Benefits:
     * - Eliminates nested if statements inside switch cases
     * - More readable and expressive code
     * - Maintains exhaustiveness checking (unguarded pattern acts as fallback)
     * - Guards are evaluated in order, first match wins
     *
     * Note: In this example, we check if cat.name() is null before accessing it,
     * demonstrating defensive programming with guarded patterns.
     */
    public String retrieveNameUsingGuardedPatterns(Animal animal){
        return switch (animal){
            case Cat cat when cat.name() == null -> "";
            case Cat cat -> cat.name();
            case Dog dog -> dog.name();
        };
    }
}
