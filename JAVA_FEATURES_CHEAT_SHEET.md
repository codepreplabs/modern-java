# Java Features Cheat Sheet by Version 📚

> **A comprehensive standalone learning resource for Java features from Java 8 to Java 24**
> 
> This is an independent reference guide - use it to learn about any Java feature, regardless of what's implemented in your current project.
> Perfect for interview preparation, self-study, or quick reference! 🎓

---

## 📖 How to Use This Guide

### For Learning
- **Beginners**: Start with Java 8 features (Lambda, Streams, Optional) - they form the foundation
- **Intermediate**: Move to Java 11-17 features (var, Records, Sealed Classes)
- **Advanced**: Explore Java 21+ features (Virtual Threads, Pattern Matching)

### For Interview Prep
- Focus on **LTS versions** (8, 11, 17, 21) - most commonly used in production
- Practice **code examples** - interviewers often ask to write or explain code
- Understand **use cases** - know when to use each feature

### For Quick Reference
- Use **Ctrl+F** to search for specific features
- Check **version numbers** to verify compatibility with your project
- Review **code snippets** before implementing features

### Practice Tips
1. **Try each example** in JShell or your IDE
2. **Modify the code** to understand edge cases
3. **Compare old vs new** syntax to appreciate improvements
4. **Build small projects** using 2-3 features together
5. **Read official JEPs** (linked where relevant) for deep understanding

---

## Java 8 (March 2014) - LTS 🌟

### Lambda Expressions
```java
// Before
Runnable r = new Runnable() {
    public void run() { System.out.println("Hello"); }
};

// After
Runnable r = () -> System.out.println("Hello");
```

### Functional Interfaces
- `@FunctionalInterface` annotation
- **Predicate<T>**: boolean test(T t)
- **Function<T,R>**: R apply(T t)
- **Consumer<T>**: void accept(T t)
- **Supplier<T>**: T get()
- **UnaryOperator<T>**: T apply(T t)
- **BinaryOperator<T>**: T apply(T t1, T t2)

### Stream API
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Filter, map, collect
List<String> filtered = names.stream()
    .filter(s -> s.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Common operations
stream.filter(predicate)
stream.map(function)
stream.flatMap(function)
stream.sorted()
stream.distinct()
stream.limit(n)
stream.skip(n)

// Terminal operations
stream.forEach(consumer)
stream.collect(collector)
stream.reduce(accumulator)
stream.count()
stream.findFirst() / findAny()
stream.anyMatch() / allMatch() / noneMatch()
```

### Method References
```java
// Static method reference
Function<String, Integer> parseInt = Integer::parseInt;

// Instance method reference
String str = "Hello";
Supplier<Integer> length = str::length;

// Constructor reference
Supplier<List<String>> listSupplier = ArrayList::new;
```

### Optional<T>
```java
Optional<String> optional = Optional.of("value");
optional.ifPresent(System.out::println);
optional.orElse("default");
optional.orElseGet(() -> "computed default");
optional.orElseThrow(() -> new Exception());

String result = optional
    .map(String::toUpperCase)
    .filter(s -> s.length() > 5)
    .orElse("NONE");
```

### Default & Static Methods in Interfaces
```java
interface MyInterface {
    // Default method
    default void defaultMethod() {
        System.out.println("Default implementation");
    }
    
    // Static method
    static void staticMethod() {
        System.out.println("Static method in interface");
    }
}
```

### Date and Time API (java.time)
```java
// LocalDate, LocalTime, LocalDateTime
LocalDate date = LocalDate.now();
LocalTime time = LocalTime.now();
LocalDateTime dateTime = LocalDateTime.now();

// Period and Duration
Period period = Period.between(date1, date2);
Duration duration = Duration.between(time1, time2);

// ZonedDateTime
ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

// Formatting
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String formatted = date.format(formatter);
```

### Other Features
- **Nashorn JavaScript Engine**
- **Base64 Encoding/Decoding**: `Base64.getEncoder()`, `Base64.getDecoder()`
- **Parallel Arrays**: `Arrays.parallelSort()`
- **Collectors**: `groupingBy()`, `partitioningBy()`, `joining()`, `toMap()`
- **StringJoiner**: `new StringJoiner(", ", "[", "]")`

---

## Java 9 (September 2017)

### Module System (Project Jigsaw)
```java
// module-info.java
module com.example.myapp {
    requires java.base;
    requires java.sql;
    exports com.example.myapp.api;
}
```

### JShell (REPL)
```bash
$ jshell
jshell> int x = 10
x ==> 10
```

### Factory Methods for Collections
```java
List<String> list = List.of("a", "b", "c");
Set<String> set = Set.of("a", "b", "c");
Map<String, Integer> map = Map.of("a", 1, "b", 2);

// Immutable collections
List<String> immutable = List.copyOf(mutableList);
```

### Stream API Enhancements
```java
// takeWhile & dropWhile
Stream.of(1, 2, 3, 4, 5)
    .takeWhile(n -> n < 4)  // [1, 2, 3]
    .forEach(System.out::println);

// ofNullable
Stream<String> stream = Stream.ofNullable(nullableValue);

// iterate with predicate
Stream.iterate(0, n -> n < 10, n -> n + 1)
```

### Optional Enhancements
```java
optional.ifPresentOrElse(
    value -> System.out.println(value),
    () -> System.out.println("Empty")
);

optional.or(() -> Optional.of("alternative"));
optional.stream()  // Convert Optional to Stream
```

### Process API
```java
ProcessHandle current = ProcessHandle.current();
System.out.println("PID: " + current.pid());
```

### Private Methods in Interfaces
```java
interface MyInterface {
    private void helperMethod() {
        System.out.println("Private helper");
    }
    
    default void publicMethod() {
        helperMethod();
    }
}
```

### Other Features
- **Try-With-Resources Enhancement**: Effectively final variables
- **Diamond Operator with Anonymous Classes**
- **@SafeVarargs on Private Methods**
- **Reactive Streams**: `Flow` API

---

## Java 10 (March 2018)

### Local Variable Type Inference (var)
```java
// Instead of
List<String> list = new ArrayList<String>();

// Use
var list = new ArrayList<String>();
var map = new HashMap<String, Integer>();
var stream = list.stream();

// Cannot use for:
// - Method parameters
// - Method return types
// - Fields
// - Null initialization: var x = null; ❌
```

### Unmodifiable Collections
```java
List<String> copy = List.copyOf(collection);
Set<String> copy = Set.copyOf(collection);
Map<String, Integer> copy = Map.copyOf(map);

// Collectors
Collectors.toUnmodifiableList()
Collectors.toUnmodifiableSet()
Collectors.toUnmodifiableMap()
```

### Optional.orElseThrow()
```java
String value = optional.orElseThrow();  // No argument needed
```

### Other Features
- **Application Class-Data Sharing (AppCDS)**
- **Parallel Full GC for G1**
- **Thread-Local Handshakes**

---

## Java 11 (September 2018) - LTS 🌟

### String Enhancements
```java
// isBlank()
"   ".isBlank()  // true

// lines()
"Line1\nLine2\nLine3".lines()
    .forEach(System.out::println);

// strip(), stripLeading(), stripTrailing()
"  Hello  ".strip()  // "Hello" (Unicode-aware)

// repeat()
"Java".repeat(3)  // "JavaJavaJava"
```

### Files Enhancements
```java
// Read/Write strings
String content = Files.readString(Path.of("file.txt"));
Files.writeString(Path.of("file.txt"), "content");
```

### Collection.toArray()
```java
List<String> list = List.of("a", "b");
String[] array = list.toArray(String[]::new);
```

### HTTP Client (Standard)
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com"))
    .build();

HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());
```

### Lambda Parameter with var
```java
// Can use var with type annotations
(var x, var y) -> x + y
(@Nonnull var x) -> x.toString()
```

### Other Features
- **Single-File Source-Code Programs**: `java HelloWorld.java`
- **Epsilon GC** (No-Op Garbage Collector)
- **Nest-Based Access Control**

---

## Java 12 (March 2019)

### Switch Expressions (Preview)
```java
// Preview feature
String result = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> "6";
    case TUESDAY -> "7";
    default -> "Other";
};
```

### String Methods
```java
// indent()
"Hello".indent(4)  // "    Hello"

// transform()
var result = "Hello".transform(s -> s + " World");
```

### Files.mismatch()
```java
long mismatch = Files.mismatch(path1, path2);  // -1 if identical
```

### Collectors.teeing()
```java
// Apply two collectors and merge results
var result = stream.collect(Collectors.teeing(
    Collectors.summingInt(Integer::intValue),
    Collectors.counting(),
    (sum, count) -> sum / count
));
```

---

## Java 13 (September 2019)

### Text Blocks (Preview)
```java
String json = """
    {
        "name": "John",
        "age": 30
    }
    """;

String html = """
    <html>
        <body>
            <h1>Hello</h1>
        </body>
    </html>
    """;
```

### Switch Expressions (Preview - Enhanced)
```java
int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY -> 7;
    default -> {
        String s = day.toString();
        yield s.length();
    }
};
```

---

## Java 14 (March 2020)

### Switch Expressions (Standard)
```java
// Now a standard feature
var result = switch (value) {
    case 1, 2 -> "One or Two";
    case 3 -> "Three";
    default -> "Other";
};

// With yield
var result = switch (value) {
    case 1:
        yield "One";
    case 2:
        yield "Two";
    default:
        yield "Other";
};
```

### Pattern Matching for instanceof (Preview)
```java
// Before
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// After
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

### Records (Preview)
```java
// Immutable data carrier
record Point(int x, int y) {}

Point p = new Point(10, 20);
System.out.println(p.x());  // 10
```

### Helpful NullPointerExceptions
```java
// Detailed NPE messages
a.b.c.d = 99;  // NPE: Cannot read field "c" because "a.b" is null
```

---

## Java 15 (September 2020)

### Text Blocks (Standard)
```java
// Now a standard feature
String query = """
    SELECT id, name, email
    FROM users
    WHERE status = 'active'
    """;
```

### Sealed Classes (Preview)
```java
public sealed class Shape
    permits Circle, Rectangle, Square {
}

final class Circle extends Shape {}
final class Rectangle extends Shape {}
final class Square extends Shape {}
```

### Hidden Classes
- For framework use (dynamic proxy, etc.)

---

## Java 16 (March 2021)

### Records (Standard)
```java
// Now a standard feature
public record Person(String name, int age) {
    // Compact constructor
    public Person {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
    
    // Custom method
    public boolean isAdult() {
        return age >= 18;
    }
}

// Usage
Person person = new Person("Alice", 30);
System.out.println(person.name());  // Alice
System.out.println(person.age());   // 30
```

### Pattern Matching for instanceof (Standard)
```java
// Now a standard feature
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s.toUpperCase());
}
```

### Stream.toList()
```java
// Shortcut for collecting to list
List<String> list = stream
    .filter(s -> s.length() > 5)
    .toList();  // Instead of .collect(Collectors.toList())
```

### Unix-Domain Socket Channels
```java
SocketAddress address = UnixDomainSocketAddress.of("/tmp/socket");
```

---

## Java 17 (September 2021) - LTS 🌟

### Sealed Classes (Standard)
```java
// Now a standard feature
public sealed interface Vehicle
    permits Car, Truck, Motorcycle {
}

final class Car implements Vehicle {}
sealed class Truck implements Vehicle permits PickupTruck {}
non-sealed class Motorcycle implements Vehicle {}
```

### Pattern Matching for Switch (Preview)
```java
static String formatter(Object o) {
    return switch (o) {
        case Integer i -> String.format("int %d", i);
        case Long l -> String.format("long %d", l);
        case Double d -> String.format("double %f", d);
        case String s -> String.format("String %s", s);
        default -> o.toString();
    };
}
```

### Enhanced Pseudo-Random Number Generators
```java
RandomGenerator generator = RandomGenerator.of("L64X128MixRandom");
int random = generator.nextInt(100);
```

### Other Features
- **Always-Strict Floating-Point Semantics**
- **Deprecate Security Manager for Removal**
- **Foreign Function & Memory API (Incubator)**

---

## Java 18 (March 2022)

### Simple Web Server
```bash
jwebserver
# Serves files from current directory on port 8000
```

```java
HttpServer server = SimpleFileServer.createFileServer(
    new InetSocketAddress(8000),
    Path.of("/www/data/"),
    OutputLevel.VERBOSE
);
server.start();
```

### Code Snippets in Javadoc
```java
/**
 * Example:
 * {@snippet :
 * List<String> list = new ArrayList<>();
 * list.add("Hello");
 * }
 */
```

### UTF-8 by Default
- Default charset is now UTF-8 (was platform-dependent)

---

## Java 19 (September 2022)

### Virtual Threads (Preview)
```java
// Lightweight threads
Thread thread = Thread.ofVirtual().start(() -> {
    System.out.println("Hello from virtual thread");
});

// With Executors
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> System.out.println("Task 1"));
    executor.submit(() -> System.out.println("Task 2"));
}
```

### Pattern Matching for Switch (Preview)
```java
static String format(Object obj) {
    return switch (obj) {
        case Integer i -> "int: " + i;
        case String s -> "String: " + s;
        case null -> "null";
        default -> "unknown";
    };
}
```

### Record Patterns (Preview)
```java
record Point(int x, int y) {}

static void printPoint(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println("x: " + x + ", y: " + y);
    }
}
```

### Structured Concurrency (Incubator)
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> findUser());
    Future<Integer> order = scope.fork(() -> fetchOrder());
    
    scope.join();
    scope.throwIfFailed();
    
    // Use results
    String userData = user.resultNow();
    Integer orderData = order.resultNow();
}
```

---

## Java 20 (March 2023)

### Scoped Values (Incubator)
```java
// Alternative to ThreadLocal
final static ScopedValue<String> USER = ScopedValue.newInstance();

ScopedValue.where(USER, "Alice").run(() -> {
    System.out.println(USER.get());  // "Alice"
});
```

### Pattern Matching for Switch (Preview)
```java
// Guarded patterns
static String test(String s) {
    return switch (s) {
        case null -> "null";
        case String str when str.length() > 5 -> "long string";
        case String str -> "short string";
    };
}
```

### Record Patterns (Preview)
```java
// Nested patterns
record Point(int x, int y) {}
record Rectangle(Point upperLeft, Point lowerRight) {}

static void print(Rectangle r) {
    if (r instanceof Rectangle(Point(var x1, var y1), Point(var x2, var y2))) {
        System.out.println("Rectangle from (" + x1 + "," + y1 + 
                         ") to (" + x2 + "," + y2 + ")");
    }
}
```

### Virtual Threads (Second Preview)
- Refinements to Java 19 preview

---

## Java 21 (September 2023) - LTS 🌟

### Unnamed Classes and Instance Main Methods
```java
// Simple programs without class declaration
void main() {
    System.out.println("Hello, World!");
}

// No need for:
// - public class
// - static
// - String[] args
// - public modifier
```

### Pattern Matching for Switch (Standard)
```java
// Now a standard feature
String result = switch (obj) {
    case String s when s.length() > 5 -> "Long string: " + s;
    case String s -> "Short string: " + s;
    case Integer i -> "Integer: " + i;
    case null -> "null value";
    default -> "Unknown type";
};
```

### Record Patterns (Standard)
```java
// Now a standard feature
record Point(int x, int y) {}

if (obj instanceof Point(int x, int y)) {
    System.out.println("Point at: " + x + ", " + y);
}

// In switch
switch (obj) {
    case Point(int x, int y) when x > 0 && y > 0 
        -> System.out.println("Quadrant I");
    case Point(int x, int y) 
        -> System.out.println("Other quadrant");
}
```

### Virtual Threads (Standard)
```java
// Now a standard feature
Thread.startVirtualThread(() -> {
    System.out.println("Virtual thread");
});

// Factory
Thread thread = Thread.ofVirtual()
    .name("worker-", 0)
    .start(task);

// ExecutorService
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}
```

### Sequenced Collections
```java
// New interfaces: SequencedCollection, SequencedSet, SequencedMap
List<String> list = new ArrayList<>(List.of("a", "b", "c"));

// New methods
list.getFirst();     // "a"
list.getLast();      // "c"
list.addFirst("z");  // ["z", "a", "b", "c"]
list.addLast("d");   // ["z", "a", "b", "c", "d"]
list.reversed();     // View in reverse order

// SequencedMap
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.putFirst("a", 1);
map.putLast("z", 26);
map.firstEntry();
map.lastEntry();
map.reversed();
```

### String Templates (Preview)
```java
String name = "World";
int value = 42;

// String interpolation
String message = STR."Hello, \{name}! Value: \{value}";
// "Hello, World! Value: 42"

// With expressions
String result = STR."Sum: \{1 + 2 + 3}";  // "Sum: 6"
```

### Structured Concurrency (Preview)
- Simplifies concurrent programming

### Scoped Values (Preview)
- Share immutable data within and across threads

---

## Java 22 (March 2024)

### Unnamed Variables & Patterns
```java
// Unnamed variables with _
if (obj instanceof Point(int x, int _)) {
    // Only care about x, ignore y
    System.out.println("x: " + x);
}

// In catch blocks
try {
    // code
} catch (Exception _) {
    // Don't need the exception variable
    System.out.println("Error occurred");
}

// In lambda
list.forEach(_ -> count++);  // Ignore parameter

// In pattern matching
switch (obj) {
    case Point(int x, int _) -> System.out.println(x);
}
```

### Statements Before super() (Preview)
```java
class SubClass extends SuperClass {
    public SubClass(int value) {
        // Can have statements before super()
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        super(value);  // Must still call super
    }
}
```

### String Templates (Second Preview)
- Refinements to Java 21 preview

### Foreign Function & Memory API (Preview)
```java
// Interact with native code and memory
Linker linker = Linker.nativeLinker();
SymbolLookup stdlib = linker.defaultLookup();
```

### Stream Gatherers (Preview)
```java
// Custom intermediate operations
Stream.of(1, 2, 3, 4, 5)
    .gather(customGatherer)
    .forEach(System.out::println);
```

---

## Java 23 (September 2024)

### Primitive Types in Patterns (Preview)
```java
// Pattern matching with primitives
Object obj = 42;

switch (obj) {
    case int i -> System.out.println("int: " + i);
    case long l -> System.out.println("long: " + l);
    case double d -> System.out.println("double: " + d);
    default -> System.out.println("other");
}
```

### Module Import Declarations (Preview)
```java
// Import entire module
import module java.base;

// Instead of individual imports
import java.util.*;
import java.io.*;
// etc.
```

### Markdown in Javadoc (Preview)
```java
/**
 * # Heading
 * 
 * This is **bold** and this is *italic*.
 * 
 * - List item 1
 * - List item 2
 */
```

### Stream Gatherers (Second Preview)
- Refinements from Java 22

### Structured Concurrency (Preview)
- Continued refinement

### Scoped Values (Preview)
- Continued refinement

---

## Java 24 (March 2025)

### Late Binding for Exceptions (Preview)
- Enhanced exception handling

### String Templates (Preview)
- Continued refinement

### Statements Before super() (Second Preview)
- Refinements from Java 22

### Primitive Types in Patterns (Second Preview)
- Refinements from Java 23

### Foreign Function & Memory API
- Stabilization work continues

---

## 🎯 Quick Reference

### LTS Versions (Focus on These) 🌟
- **Java 8** (March 2014) - The foundation! Extended support until 2030
- **Java 11** (September 2018) - Major LTS release. Extended support until 2027
- **Java 17** (September 2021) - Current enterprise standard. Extended support until 2029
- **Java 21** (September 2023) - Latest LTS. Extended support until 2031

### Learning Path by Experience Level

**🟢 Beginner**: Java 8 features (Lambda, Streams, Optional, Functional Interfaces)  
**🟡 Intermediate**: Java 11-17 (var, Records, Sealed Classes, Text Blocks)  
**🔴 Advanced**: Java 21+ (Virtual Threads, Pattern Matching, Sequenced Collections)

### Feature Version Checker

| Feature | Minimum Version |
|---------|----------------|
| Lambda Expressions | Java 8 |
| Stream API | Java 8 |
| Optional | Java 8 |
| Date-Time API | Java 8 |
| Modules | Java 9 |
| var keyword | Java 10 |
| String methods (isBlank, lines) | Java 11 |
| HTTP Client | Java 11 |
| Switch Expressions | Java 14 |
| Text Blocks | Java 15 |
| Records | Java 16 |
| Pattern Matching instanceof | Java 16 |
| Sealed Classes | Java 17 |
| Pattern Matching switch | Java 21 |
| Virtual Threads | Java 21 |
| Sequenced Collections | Java 21 |
| Unnamed Variables | Java 22 |

---

## 📚 Additional Resources

### Interview Preparation
📝 **[Java Interview Guide](JAVA_INTERVIEW_GUIDE.md)** - Comprehensive interview prep with:
- Top 20 interview questions with detailed answers
- Code examples and best practices
- Common mistakes to avoid
- 4-week study plan
- Interview tips and tricks

### Official Documentation
- [Oracle Java Documentation](https://docs.oracle.com/en/java/)
- [OpenJDK JEPs](https://openjdk.org/jeps/0) - Java Enhancement Proposals
- [Java Language Specification](https://docs.oracle.com/javase/specs/)

### Practice & Learning
- **LeetCode** - Java Stream API problems
- **HackerRank** - Java practice challenges
- **Exercism** - Java track with mentoring
- **JShell** - Interactive Java REPL (built into JDK)

### Key JEPs to Read
- JEP 395: Records (Java 16)
- JEP 409: Sealed Classes (Java 17)
- JEP 394: Pattern Matching for instanceof (Java 16)
- JEP 441: Pattern Matching for switch (Java 21)
- JEP 444: Virtual Threads (Java 21)
- JEP 431: Sequenced Collections (Java 21)

---

**Last Updated**: January 2025

**Happy Learning! 🚀**
