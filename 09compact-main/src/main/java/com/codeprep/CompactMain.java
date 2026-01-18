// Compact Main - Java 21+ Feature (JEP 445)
// No package declaration needed for unnamed class
// No public class declaration needed
// No static keyword needed for main
// Simplified main method signature

String greeting = "Hello World";

void main() {
    IO.println("Hello from Compact Main!");
    IO.println("This demonstrates Java 21+ compact main patterns.");

    // Can call instance methods directly
    greet(greeting);

    // Print some information
    printInfo();
}

// Instance methods (no static needed!)
void greet(String name) {
    IO.println("Welcome to " + name + "!");
}

void printInfo() {
    IO.println("Java version: " + System.getProperty("java.version"));
    IO.println("This is a true compact main - no class declaration, no static, no String[] args!");
}