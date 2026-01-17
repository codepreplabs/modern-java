# Modern Java

A comprehensive multi-module Maven project demonstrating modern Java features and best practices.

## 📋 Overview

This repository contains practical examples and exercises for learning modern Java features, including streams, lambdas, functional interfaces, method references, parallel processing, and optional handling.

## 🏗️ Project Structure

This is a multi-module Maven project with the following modules:

### Modules

- **00data** - Data models and utilities
  - Contains domain classes like `Student`, `Bike`, and `StudentDataBase`
  - Provides sample data for use across other modules

- **01streams** - Java Streams API
  - Stream operations and transformations
  - Intermediate and terminal operations
  - FlatMap examples
  - Find operations (findFirst, findAny)
  - Collectors (partitionBy, summingInt, averaging, maxBy, minBy, mapping)
  - Stateful and stateless operations

- **02lambda** - Lambda Expressions
  - Lambda syntax and usage
  - Runnable with lambdas
  - Comparator with lambdas
  - Functional programming concepts

- **03functional-interfaces** - Functional Interfaces
  - Consumer and BiConsumer
  - Function and BiFunction
  - Predicate and BiPredicate
  - UnaryOperator and BinaryOperator
  - Built-in functional interfaces

- **04method-reference** - Method References
  - Instance method references
  - Static method references
  - Constructor references
  - Method reference syntax

- **05parallel-streams** - Parallel Streams
  - Parallel stream processing
  - Performance comparisons
  - Best practices for parallel operations
  - Thread pool behavior

- **06optional** - Optional API
  - Optional creation (of, ofNullable, empty)
  - Optional operations (map, flatMap)
  - Conditional execution (ifPresent, isPresent)
  - Best practices for null handling

- **07default-static-methods** - Default and Static Methods in Interfaces
  - Default method implementation
  - Static methods in interfaces
  - Multiple inheritance with default methods
  - Resolving conflicts with default methods

- **08date-time** - Date and Time API
  - LocalDate, LocalTime, and LocalDateTime
  - Instant and ZonedDateTime
  - Period and Duration
  - Date and time formatting
  - Converting legacy Date to LocalDate

## 🚀 Getting Started

### Prerequisites

- Java 24 or higher
- Maven 3.6+

### Building the Project

```bash
# Build all modules
mvn clean install

# Build a specific module
cd <module-name>
mvn clean install

# Run tests
mvn test
```

## 💻 Running Examples

Each module contains executable examples in the `src/main/java` directory. You can run them directly from your IDE or using Maven:

```bash
cd <module-name>
mvn exec:java -Dexec.mainClass="com.codeprep.YourMainClass"
```

## 📚 Topics Covered

- **Functional Programming**: Embracing functional paradigms in Java
- **Stream API**: Processing collections declaratively
- **Lambda Expressions**: Writing concise, functional code
- **Method References**: Simplifying lambda expressions
- **Optional**: Avoiding null pointer exceptions
- **Parallel Processing**: Leveraging multi-core processors
- **Default and Static Methods**: Interface evolution and multiple inheritance
- **Date and Time API**: Modern temporal data handling with java.time package

## 🛠️ Technology Stack

- Java 24
- Maven 3.11.0
- JUnit (for testing)

## 📖 Learning Path

Recommended order for studying the modules:

1. **00data** - Understand the data models
2. **02lambda** - Learn lambda syntax and basics
3. **03functional-interfaces** - Explore built-in functional interfaces
4. **04method-reference** - Simplify lambdas with method references
5. **01streams** - Master the Streams API
6. **05parallel-streams** - Optimize with parallel processing
7. **06optional** - Handle nulls gracefully
8. **07default-static-methods** - Learn interface evolution with default and static methods
9. **08date-time** - Master the modern Date and Time API

## 🤝 Contributing

This is a learning project. Feel free to:
- Add more examples
- Improve existing code
- Fix bugs or issues
- Enhance documentation

## 📝 License

This project is for educational purposes.

---

**Happy Learning! 🎓**
