package com.codeprep.multipleinheritence;

/*
 * Multiple Inheritance in Java after Java 8:
 *
 * Java traditionally didn't support multiple inheritance to avoid the "Diamond Problem"
 * where ambiguity arises when a class inherits from two classes with the same method.
 *
 * However, Java 8 introduced DEFAULT METHODS in interfaces, which enables a form of
 * multiple inheritance of behavior (not state). A class can now implement multiple
 * interfaces that contain default method implementations.
 *
 * Key Points:
 * 1. Interfaces can have concrete methods (default methods) with implementations
 * 2. A class can implement multiple interfaces, inheriting behavior from all of them
 * 3. If multiple interfaces have the same default method, the implementing class must
 *    override it to resolve the conflict (Diamond Problem resolution)
 * 4. This allows code reusability and behavioral inheritance without the complexity
 *    of multiple class inheritance
 * 5. Interfaces still cannot have instance variables (no state inheritance)
 *
 * In this example, MultipleInheritanceClient inherits default method implementations
 * from three different interfaces (InterfaceOne, InterfaceTwo, InterfaceThree),
 * demonstrating multiple inheritance of behavior.
 */
public class MultipleInheritanceClient implements InterfaceOne, InterfaceTwo, InterfaceThree{

    public static void main(String[] args) {

        /*
         * Order of Resolution for Default Methods (Java's Diamond Problem Resolution):
         *
         * When a class implements multiple interfaces with conflicting default methods,
         * Java follows this resolution order:
         *
         * 1. CLASS WINS: If a class has a concrete implementation, it always takes precedence
         *    over any default method from interfaces.
         *
         * 2. SUB-INTERFACE WINS: If an interface extends another interface and both have
         *    the same default method, the more specific (sub) interface's implementation wins.
         *    - In this example: InterfaceTwo extends InterfaceOne and overrides methodA()
         *    - Result: InterfaceTwo's methodA() is used (most specific implementation)
         *
         * 3. EXPLICIT OVERRIDE REQUIRED: If two interfaces at the same level provide the
         *    same default method, the implementing class MUST override the method explicitly
         *    to resolve the ambiguity (compilation error otherwise).
         *
         * In this code:
         * - methodA(): Resolved to InterfaceTwo's implementation (sub-interface wins over InterfaceOne)
         * - methodB(): Resolved to InterfaceTwo's implementation (only one source)
         * - methodC(): Resolved to InterfaceThree's implementation (only one source)
         */

        MultipleInheritanceClient inheritanceClient = new MultipleInheritanceClient();
        inheritanceClient.methodA(); // InterfaceTwo's implementation (sub-interface wins)
        inheritanceClient.methodB(); // InterfaceTwo's implementation
        inheritanceClient.methodC(); // InterfaceThree's implementation
    }
}
