# Java 11-17 Features - Interview Questions 📦

> **Part 3 of Comprehensive Java Interview Guide**
>
> Covers Java 11, 14, 15, 16, and 17 LTS features including Records, Sealed Classes, Pattern Matching, Text Blocks, and more.

---

## 📑 Table of Contents
- Java 11 Features (Q46-Q55)
- Java 14-15 Features (Q56-Q65)
- Java 16 Features - Records (Q66-Q75)
- Java 17 Features - Sealed Classes & Pattern Matching (Q76-Q85)

---

## Java 11 Features

### Q46. What are the new String methods in Java 11? 🟢

**Short Answer:**
Java 11 added methods like `isBlank()`, `lines()`, `strip()`, `stripLeading()`, `stripTrailing()`, and `repeat()`.

**Detailed Explanation:**
```java
// 1. isBlank() - checks if string is empty or contains only whitespace
String s1 = "";
String s2 = "   ";
String s3 = "hello";

s1.isBlank();  // true
s2.isBlank();  // true
s3.isBlank();  // false

// vs isEmpty()
s2.isEmpty();  // false - has characters (spaces)
s2.isBlank();  // true - only whitespace

// 2. lines() - splits string by line terminators, returns Stream
String multiline = "Line 1\nLine 2\nLine 3";
multiline.lines()
    .forEach(System.out::println);

// Count non-empty lines
long count = multiline.lines()
    .filter(line -> !line.isBlank())
    .count();

// Process file lines
String fileContent = Files.readString(Path.of("file.txt"));
List<String> nonEmptyLines = fileContent.lines()
    .filter(line -> !line.isBlank())
    .toList();

// 3. strip() - removes leading and trailing whitespace (Unicode-aware)
String s = "  hello  ";
s.strip();  // "hello"

// vs trim() - only removes ASCII whitespace
String unicode = "\u2000hello\u2000";  // Unicode spaces
unicode.trim();   // Still has spaces (trim doesn't recognize Unicode)
unicode.strip();  // "hello" (strip handles Unicode)

// 4. stripLeading() - removes leading whitespace only
String s = "  hello  ";
s.stripLeading();  // "hello  "

// 5. stripTrailing() - removes trailing whitespace only
String s = "  hello  ";
s.stripTrailing();  // "  hello"

// 6. repeat(int count) - repeats string n times
"Java".repeat(3);  // "JavaJavaJava"
"=".repeat(50);    // "=================================================="
"-".repeat(10);    // "----------"

// Use cases
String separator = "=".repeat(50);
String padding = " ".repeat(10);
String dashes = "-".repeat(width);

// Creating patterns
System.out.println("*".repeat(10));
System.out.println("* Header *");
System.out.println("*".repeat(10));
```

**Practical Examples:**
```java
// Example 1: Processing CSV with blank lines
String csv = """
    Name,Age,City
    Alice,30,NYC
    
    Bob,25,LA
    
    Charlie,35,SF
    """;

List<String> validLines = csv.lines()
    .map(String::strip)
    .filter(line -> !line.isBlank())
    .toList();

// Example 2: Creating formatted output
public String formatTitle(String title) {
    int width = 50;
    int padding = (width - title.length()) / 2;
    return "=".repeat(width) + "\n" +
           " ".repeat(padding) + title + "\n" +
           "=".repeat(width);
}

// Example 3: Input validation
public boolean isValidInput(String input) {
    return input != null && !input.isBlank();
}

// Example 4: Log file processing
String logContent = Files.readString(Path.of("app.log"));
List<String> errorLines = logContent.lines()
    .filter(line -> line.contains("ERROR"))
    .filter(line -> !line.isBlank())
    .toList();
```

---

### Q47. What is the var keyword and what are its limitations? 🟢

**Detailed Answer Already Covered in Q10 of Part 1**

---

### Q48. Explain the HTTP Client API introduced in Java 11 🟡

**Short Answer:**
Java 11 introduced a modern, fluent HTTP Client API (java.net.http) that supports HTTP/2, WebSocket, and reactive programming.

**Detailed Explanation:**
```java
import java.net.http.*;
import java.net.URI;

// 1. Basic GET request
HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .GET()
    .build();

HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());

System.out.println("Status: " + response.statusCode());
System.out.println("Body: " + response.body());

// 2. POST request with JSON
String json = """
    {
        "name": "Alice",
        "age": 30
    }
    """;

HttpRequest postRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .build();

HttpResponse<String> postResponse = client.send(postRequest,
    HttpResponse.BodyHandlers.ofString());

// 3. Async requests
CompletableFuture<HttpResponse<String>> futureResponse =
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

futureResponse.thenAccept(response -> {
    System.out.println("Async response: " + response.body());
});

// 4. Multiple async requests
List<URI> uris = List.of(
    URI.create("https://api.example.com/user/1"),
    URI.create("https://api.example.com/user/2"),
    URI.create("https://api.example.com/user/3")
);

List<CompletableFuture<String>> futures = uris.stream()
    .map(uri -> HttpRequest.newBuilder(uri).build())
    .map(request -> client.sendAsync(request, 
        HttpResponse.BodyHandlers.ofString()))
    .map(future -> future.thenApply(HttpResponse::body))
    .toList();

// Wait for all to complete
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
    .join();

// Get results
List<String> results = futures.stream()
    .map(CompletableFuture::join)
    .toList();

// 5. Custom client configuration
HttpClient customClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)  // HTTP/2
    .followRedirects(HttpClient.Redirect.NORMAL)
    .connectTimeout(Duration.ofSeconds(10))
    .build();

// 6. Request configuration
HttpRequest configuredRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .timeout(Duration.ofSeconds(5))
    .header("User-Agent", "MyApp/1.0")
    .header("Accept", "application/json")
    .GET()
    .build();

// 7. Download file
HttpRequest downloadRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://example.com/file.zip"))
    .build();

HttpResponse<Path> fileResponse = client.send(downloadRequest,
    HttpResponse.BodyHandlers.ofFile(Path.of("downloaded.zip")));

// 8. Authentication
HttpRequest authRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/secure"))
    .header("Authorization", "Bearer " + token)
    .build();

// 9. WebSocket (preview in Java 11)
WebSocket webSocket = HttpClient.newHttpClient()
    .newWebSocketBuilder()
    .buildAsync(URI.create("wss://echo.websocket.org"), new WebSocket.Listener() {
        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.sendText("Hello WebSocket!", true);
            WebSocket.Listener.super.onOpen(webSocket);
        }
        
        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("Received: " + data);
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }
    })
    .join();
```

**Comparison with Old HttpURLConnection:**
```java
// Old way (HttpURLConnection) - verbose
URL url = new URL("https://api.example.com/users");
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setRequestMethod("GET");
connection.setConnectTimeout(5000);
connection.setReadTimeout(5000);

int responseCode = connection.getResponseCode();
BufferedReader reader = new BufferedReader(
    new InputStreamReader(connection.getInputStream()));
StringBuilder response = new StringBuilder();
String line;
while ((line = reader.readLine()) != null) {
    response.append(line);
}
reader.close();

// New way (HttpClient) - clean
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .timeout(Duration.ofSeconds(5))
    .build();

String response = client.send(request, 
    HttpResponse.BodyHandlers.ofString()).body();
```

**Real-World Example:**
```java
public class ApiClient {
    private final HttpClient client;
    private final String baseUrl;
    
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }
    
    public <T> T get(String endpoint, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + endpoint))
            .header("Accept", "application/json")
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP " + response.statusCode());
        }
        
        return objectMapper.readValue(response.body(), responseType);
    }
    
    public <T> CompletableFuture<T> getAsync(String endpoint, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + endpoint))
            .header("Accept", "application/json")
            .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> {
                try {
                    return objectMapper.readValue(response.body(), responseType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
```

---

### Q49. What file methods were added in Java 11? 🟡

**Short Answer:**
Java 11 added `Files.readString()` and `Files.writeString()` for convenient file operations.

**Detailed Explanation:**
```java
import java.nio.file.*;

// 1. Files.readString() - read entire file as String
String content = Files.readString(Path.of("file.txt"));
System.out.println(content);

// With specific charset
String content = Files.readString(Path.of("file.txt"), 
    StandardCharsets.UTF_8);

// 2. Files.writeString() - write String to file
Files.writeString(Path.of("output.txt"), "Hello, World!");

// With options
Files.writeString(
    Path.of("output.txt"),
    "New line\n",
    StandardOpenOption.APPEND  // Append to file
);

// 3. Before Java 11 - more verbose
// Reading
List<String> lines = Files.readAllLines(Path.of("file.txt"));
String content = String.join("\n", lines);

// Or
String content = new String(Files.readAllBytes(Path.of("file.txt")));

// Writing
Files.write(Path.of("output.txt"), "Hello".getBytes());
```

**Practical Examples:**
```java
// Example 1: Configuration file
public class ConfigLoader {
    public Properties loadConfig(String filename) throws IOException {
        String content = Files.readString(Path.of(filename));
        Properties props = new Properties();
        props.load(new StringReader(content));
        return props;
    }
    
    public void saveConfig(String filename, Properties props) throws IOException {
        StringWriter writer = new StringWriter();
        props.store(writer, "Configuration");
        Files.writeString(Path.of(filename), writer.toString());
    }
}

// Example 2: Template processing
public String processTemplate(String templateFile, Map<String, String> variables) 
        throws IOException {
    String template = Files.readString(Path.of(templateFile));
    
    for (Map.Entry<String, String> entry : variables.entrySet()) {
        template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
    }
    
    return template;
}

// Example 3: Log file appending
public void appendLog(String message) throws IOException {
    String timestamp = LocalDateTime.now().toString();
    String logEntry = timestamp + ": " + message + "\n";
    
    Files.writeString(
        Path.of("app.log"),
        logEntry,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
    );
}

// Example 4: JSON processing
public <T> T loadJson(String filename, Class<T> type) throws IOException {
    String json = Files.readString(Path.of(filename));
    return objectMapper.readValue(json, type);
}

public void saveJson(String filename, Object data) throws IOException {
    String json = objectMapper.writeValueAsString(data);
    Files.writeString(Path.of(filename), json);
}
```

---

### Q50. What other important changes came in Java 11? 🟡

**Collection.toArray() Enhancement:**
```java
List<String> list = List.of("a", "b", "c");

// Before Java 11
String[] array = list.toArray(new String[list.size()]);
String[] array = list.toArray(new String[0]);  // Common idiom

// Java 11+ with method reference
String[] array = list.toArray(String[]::new);  // Cleaner!
```

**Predicate.not():**
```java
List<String> names = List.of("Alice", "", "Bob", "  ", "Charlie");

// Before Java 11
List<String> filtered = names.stream()
    .filter(s -> !s.isBlank())
    .toList();

// Java 11+
List<String> filtered = names.stream()
    .filter(Predicate.not(String::isBlank))  // More readable!
    .toList();

// More examples
list.stream()
    .filter(Predicate.not(String::isEmpty))
    .filter(Predicate.not(Objects::isNull))
    .toList();
```

**Optional.isEmpty():**
```java
Optional<String> opt = Optional.empty();

// Before Java 11
if (!opt.isPresent()) {
    // handle empty
}

// Java 11+
if (opt.isEmpty()) {  // More intuitive!
    // handle empty
}
```

**Running Java Files Directly:**
```bash
# Java 11+ can run source files directly
java HelloWorld.java  # No need to compile first!

# Great for simple scripts
java -classpath libs/* MyScript.java
```

**Removed Features:**
```java
// Java EE modules removed (need to add separately)
// - java.xml.ws (JAX-WS)
// - java.xml.bind (JAXB)
// - java.activation
// - java.xml.ws.annotation
// - java.corba
// - java.transaction

// Need to add dependencies:
// <dependency>
//     <groupId>javax.xml.bind</groupId>
//     <artifactId>jaxb-api</artifactId>
// </dependency>
```

---

## Java 14-15 Features

### Q51. What are Switch Expressions (Java 14)? 🟢

**Short Answer:**
Switch expressions (standardized in Java 14) allow switch to return values and use arrow syntax, making code more concise and type-safe.

**Detailed Explanation:**
```java
// Traditional switch statement
String day = "MONDAY";
String result;
switch (day) {
    case "MONDAY":
    case "FRIDAY":
    case "SUNDAY":
        result = "6 letters";
        break;
    case "TUESDAY":
        result = "7 letters";
        break;
    default:
        result = "Other";
        break;
}

// Switch expression (Java 14+)
String result = switch (day) {
    case "MONDAY", "FRIDAY", "SUNDAY" -> "6 letters";
    case "TUESDAY" -> "7 letters";
    default -> "Other";
};

// 1. Multiple case labels
int numLetters = switch (day) {
    case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
    case "TUESDAY" -> 7;
    case "SATURDAY" -> 8;
    case "WEDNESDAY" -> 9;
    case "THURSDAY" -> 8;
    default -> 0;
};

// 2. Block statements with yield
String description = switch (day) {
    case "MONDAY" -> "Start of work week";
    case "FRIDAY" -> {
        String mood = "Happy";
        yield mood + " - End of work week";  // yield returns value
    }
    case "SATURDAY", "SUNDAY" -> "Weekend!";
    default -> "Midweek";
};

// 3. Exhaustiveness checking with enums
enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

int workHours = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> 8;
    case SATURDAY, SUNDAY -> 0;
    // No default needed - all cases covered!
};

// 4. Mix arrow and colon syntax
String result = switch (value) {
    case 1 -> "One";
    case 2: yield "Two";  // Can use yield with colon
    case 3 -> "Three";
    default -> "Other";
};

// 5. Returning complex objects
record Result(String status, int code) {}

Result result = switch (httpCode) {
    case 200 -> new Result("OK", 200);
    case 404 -> new Result("Not Found", 404);
    case 500 -> {
        log.error("Server error");
        yield new Result("Server Error", 500);
    }
    default -> new Result("Unknown", httpCode);
};

// 6. Using in methods
public String getDayType(Day day) {
    return switch (day) {
        case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
        case SATURDAY, SUNDAY -> "Weekend";
    };
}

// 7. Nested switches
String result = switch (category) {
    case "FRUIT" -> switch (name) {
        case "Apple" -> "Red fruit";
        case "Banana" -> "Yellow fruit";
        default -> "Unknown fruit";
    };
    case "VEGETABLE" -> switch (name) {
        case "Carrot" -> "Orange vegetable";
        case "Broccoli" -> "Green vegetable";
        default -> "Unknown vegetable";
    };
    default -> "Unknown category";
};
```

**Benefits:**
```java
// 1. No fall-through bugs
// Traditional - easy to forget break
switch (value) {
    case 1:
        doSomething();
        // Forgot break - falls through to case 2!
    case 2:
        doSomethingElse();
        break;
}

// Expression - no fall-through possible
String result = switch (value) {
    case 1 -> "One";
    case 2 -> "Two";  // Automatically breaks
};

// 2. Compile-time exhaustiveness
enum Status { ACTIVE, INACTIVE, PENDING }

String message = switch (status) {
    case ACTIVE -> "Active user";
    case INACTIVE -> "Inactive user";
    case PENDING -> "Pending approval";
    // Compiler ensures all cases handled!
};

// 3. Expression can be used anywhere
int value = 10 * switch (type) {
    case "A" -> 1;
    case "B" -> 2;
    default -> 0;
};

System.out.println("Result: " + switch (code) {
    case 0 -> "Success";
    case 1 -> "Warning";
    default -> "Error";
});
```

**Real-World Examples:**
```java
// Example 1: HTTP status handling
public record Response(int code, String body) {}

public String handleResponse(Response response) {
    return switch (response.code()) {
        case 200, 201, 204 -> "Success: " + response.body();
        case 400, 422 -> "Client error: " + response.body();
        case 401, 403 -> "Authentication error";
        case 404 -> "Not found";
        case 500, 503 -> "Server error";
        default -> "Unexpected status: " + response.code();
    };
}

// Example 2: Strategy pattern simplification
interface PaymentMethod {}
record CreditCard(String number) implements PaymentMethod {}
record PayPal(String email) implements PaymentMethod {}
record Bitcoin(String address) implements PaymentMethod {}

public double processFee(PaymentMethod method, double amount) {
    return switch (method) {
        case CreditCard cc -> amount * 0.029 + 0.30;
        case PayPal pp -> amount * 0.034;
        case Bitcoin btc -> amount * 0.01;
        default -> 0;
    };
}

// Example 3: Configuration mapping
public int getTimeout(String environment) {
    return switch (environment) {
        case "dev", "development" -> 30;
        case "test", "testing" -> 60;
        case "staging" -> 120;
        case "prod", "production" -> 300;
        default -> throw new IllegalArgumentException("Unknown environment: " + environment);
    };
}
```

---

### Q52. What are Text Blocks (Java 15)? 🟢

**Short Answer:**
Text blocks allow multi-line string literals with automatic formatting, making it easier to write SQL, JSON, HTML, etc.

**Detailed Explanation:**
```java
// 1. Basic text block
String html = """
    <html>
        <body>
            <h1>Hello, World!</h1>
        </body>
    </html>
    """;

// vs traditional (messy!)
String htmlOld = "<html>\n" +
                 "    <body>\n" +
                 "        <h1>Hello, World!</h1>\n" +
                 "    </body>\n" +
                 "</html>\n";

// 2. SQL queries
String sql = """
    SELECT users.name, orders.id, orders.total
    FROM users
    JOIN orders ON users.id = orders.user_id
    WHERE orders.status = 'COMPLETED'
    AND orders.total > 100
    ORDER BY orders.created_at DESC
    LIMIT 10
    """;

// 3. JSON
String json = """
    {
        "name": "Alice",
        "age": 30,
        "address": {
            "city": "New York",
            "country": "USA"
        },
        "hobbies": ["reading", "coding", "gaming"]
    }
    """;

// 4. Indentation management
// Leading whitespace common to all lines is automatically removed
String example = """
            Line 1
            Line 2
            Line 3
            """;
// Result:
// "Line 1\nLine 2\nLine 3\n"

// Different indentation
String example = """
        Line 1
            Indented Line 2
        Line 3
    """;
// Closes at column 4, so 4 spaces removed from all lines
// Result:
// "    Line 1\n        Indented Line 2\n    Line 3\n"

// 5. Escape sequences
String example = """
    Line with "quotes"
    Line with \t tab
    Line with \\ backslash
    """;

// 6. New escape sequences
// \s - space (preserves trailing spaces)
String example = """
    Line 1\s\s
    Line 2
    """;
// Trailing spaces preserved

// \<newline> - line continuation (removes newline)
String oneLine = """
    This is a very long line \
    that continues here
    """;
// Result: "This is a very long line that continues here\n"

// 7. String interpolation workaround (no built-in support yet)
String name = "Alice";
int age = 30;

String message = """
    Name: %s
    Age: %d
    """.formatted(name, age);

// Or with String.format
String message = String.format("""
    Name: %s
    Age: %d
    """, name, age);

// 8. Embedded expressions (using formatted)
int count = 5;
String message = """
    Found %d items:
    - Item 1
    - Item 2
    - Item 3
    """.formatted(count);
```

**Common Use Cases:**
```java
// Use Case 1: REST API Testing
String requestBody = """
    {
        "username": "testuser",
        "password": "testpass",
        "email": "test@example.com"
    }
    """;

// Use Case 2: Email Templates
String emailTemplate = """
    Dear %s,
    
    Thank you for your order #%s.
    
    Your order has been confirmed and will be shipped soon.
    
    Best regards,
    The Team
    """.formatted(customerName, orderId);

// Use Case 3: Regular Expressions
String regex = """
    ^                    # Start of string
    [a-zA-Z0-9._%+-]+   # Local part
    @                    # @ symbol
    [a-zA-Z0-9.-]+      # Domain
    \\.                  # Literal dot
    [a-zA-Z]{2,}        # TLD
    $                    # End of string
    """;

// Use Case 4: Markdown/Documentation
String documentation = """
    # API Documentation
    
    ## Endpoints
    
    ### GET /api/users
    Returns list of all users.
    
    ### POST /api/users
    Creates a new user.
    
    **Request Body:**
    ```json
    {
        "name": "string",
        "email": "string"
    }
    ```
    """;

// Use Case 5: Code Generation
public String generateClass(String className, List<String> fields) {
    return """
        public class %s {
        %s
            
            // Constructor, getters, setters...
        }
        """.formatted(
            className,
            fields.stream()
                .map(f -> "    private String " + f + ";")
                .collect(Collectors.joining("\n"))
        );
}

// Use Case 6: Configuration Files
String config = """
    server:
        port: 8080
        host: localhost
    database:
        url: jdbc:postgresql://localhost:5432/mydb
        username: admin
        password: secret
    logging:
        level: INFO
    """;
```

**Comparison:**
```java
// Before Text Blocks (Painful!)
String query = "SELECT u.name, u.email, o.id, o.total\n" +
               "FROM users u\n" +
               "JOIN orders o ON u.id = o.user_id\n" +
               "WHERE o.status = 'COMPLETED'\n" +
               "ORDER BY o.created_at DESC";

// With Text Blocks (Clean!)
String query = """
    SELECT u.name, u.email, o.id, o.total
    FROM users u
    JOIN orders o ON u.id = o.user_id
    WHERE o.status = 'COMPLETED'
    ORDER BY o.created_at DESC
    """;
```

---

## Java 16 Features - Records

### Q53. What are Records and why were they introduced? 🟢

**Detailed Answer Already Covered in Q8 of Part 1**

---

### Q54. How do you create and use Records with validation? 🟡

**Short Answer:**
Use compact constructors in Records to add validation and normalization logic.

**Detailed Explanation:**
```java
// 1. Basic Record with Validation
public record Person(String name, int age) {
    // Compact constructor - no parameter list
    public Person {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        // Assignments happen automatically after validation
    }
}

// Usage
try {
    Person p1 = new Person("Alice", 30);  // OK
    Person p2 = new Person("", 25);       // throws IllegalArgumentException
    Person p3 = new Person("Bob", -5);    // throws IllegalArgumentException
} catch (IllegalArgumentException e) {
    System.out.println("Invalid: " + e.getMessage());
}

// 2. Normalization in Compact Constructor
public record Email(String address) {
    public Email {
        if (address == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        // Normalize: trim and lowercase
        address = address.trim().toLowerCase();
        
        // Validate format
        if (!address.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

// Usage
Email email = new Email("  Alice@EXAMPLE.com  ");
System.out.println(email.address());  // "alice@example.com"

// 3. Multiple Validations
public record Product(String name, double price, int quantity) {
    public Product {
        // Validation messages
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        // Normalization
        name = name.trim();
    }
    
    // Additional method
    public double totalValue() {
        return price * quantity;
    }
}

// 4. Complex Validation with Dependencies
public record DateRange(LocalDate start, LocalDate end) {
    public DateRange {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
    
    public long daysInRange() {
        return ChronoUnit.DAYS.between(start, end);
    }
}

// 5. Canonical Constructor (Full Control)
public record Money(BigDecimal amount, String currency) {
    // Full constructor - explicit parameter list
    public Money(BigDecimal amount, String currency) {
        // Validation
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be 3-letter code");
        }
        
        // Explicit assignment required
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }
}

// 6. Multiple Constructors
public record Rectangle(double width, double height) {
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
    
    // Additional constructor for square
    public Rectangle(double side) {
        this(side, side);  // Delegates to canonical constructor
    }
    
    public double area() {
        return width * height;
    }
}

// Usage
Rectangle rect = new Rectangle(10, 20);
Rectangle square = new Rectangle(15);  // 15x15 square

// 7. Builder Pattern with Records
public record User(String username, String email, int age, String phone) {
    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username required");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email required");
        }
        if (age < 18) {
            throw new IllegalArgumentException("Must be 18 or older");
        }
    }
    
    // Builder
    public static class Builder {
        private String username;
        private String email;
        private int age;
        private String phone = "";  // Optional
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public User build() {
            return new User(username, email, age, phone);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
}

// Usage
User user = User.builder()
    .username("alice")
    .email("alice@example.com")
    .age(30)
    .phone("555-1234")
    .build();
```

**Validation Patterns:**
```java
// Pattern 1: Null-safe wrapper
public record SafeString(String value) {
    public SafeString {
        value = value == null ? "" : value.trim();
    }
}

// Pattern 2: Range validation
public record Score(int value) {
    public Score {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Score must be 0-100");
        }
    }
}

// Pattern 3: Collection validation
public record Team(String name, List<String> members) {
    public Team {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name required");
        }
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Team must have members");
        }
        // Make defensive copy and immutable
        members = List.copyOf(members);
    }
}

// Pattern 4: Cross-field validation
public record Credentials(String username, String password) {
    public Credentials {
        if (username == null || username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (username.equals(password)) {
            throw new IllegalArgumentException("Password cannot be same as username");
        }
    }
}
```

---

### Q55. Can Records implement interfaces and have custom methods? 🟢

**Short Answer:**
Yes, Records can implement interfaces and add custom instance methods, static methods, and static fields.

**Detailed Examples:**
```java
// 1. Implementing interfaces
public interface Identifiable {
    Long getId();
}

public record User(Long id, String name) implements Identifiable {
    // getId() automatically implemented from accessor
}

// 2. Multiple interfaces
public interface Comparable<T>, Serializable {}

public record Person(String lastName, String firstName, int age) 
    implements Comparable<Person>, Serializable {
    
    @Override
    public int compareTo(Person other) {
        int result = lastName.compareTo(other.lastName);
        if (result == 0) {
            result = firstName.compareTo(other.firstName);
        }
        return result;
    }
}

// 3. Custom instance methods
public record Rectangle(double width, double height) {
    public double area() {
        return width * height;
    }
    
    public double perimeter() {
        return 2 * (width + height);
    }
    
    public boolean isSquare() {
        return width == height;
    }
    
    public Rectangle scale(double factor) {
        return new Rectangle(width * factor, height * factor);
    }
}

// 4. Static methods and fields
public record Product(String name, BigDecimal price) {
    public static final BigDecimal TAX_RATE = new BigDecimal("0.08");
    
    public static Product createWithTax(String name, BigDecimal basePrice) {
        BigDecimal total = basePrice.multiply(BigDecimal.ONE.add(TAX_RATE));
        return new Product(name, total);
    }
    
    public BigDecimal getPriceWithoutTax() {
        return price.divide(BigDecimal.ONE.add(TAX_RATE), RoundingMode.HALF_UP);
    }
}
```

---

### Java 17 Features - Sealed Classes

### Q56. What are Sealed Classes and why were they introduced? 🟢

**Detailed Answer Already Covered in Q114 of File 05**

---

### Q57. How do Sealed Classes work with Pattern Matching? 🟡

**Integration with Pattern Matching:**
```java
// Sealed hierarchy
public sealed interface Shape permits Circle, Rectangle, Triangle {}

public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}
public record Triangle(double base, double height) implements Shape {}

// Exhaustive pattern matching - no default needed!
public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> 0.5 * t.base() * t.height();
        // Compiler knows all possibilities - no default required
    };
}

// With guards
public String describe(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 10 -> "Large circle";
        case Circle c -> "Small circle";
        case Rectangle r when r.width() == r.height() -> "Square";
        case Rectangle r -> "Rectangle";
        case Triangle t -> "Triangle";
    };
}

// Nested pattern matching
public record ColoredShape(Shape shape, String color) {}

public String fullDescription(ColoredShape cs) {
    return switch (cs) {
        case ColoredShape(Circle(double r), String color) ->
            color + " circle with radius " + r;
        case ColoredShape(Rectangle(double w, double h), String color) ->
            color + " rectangle " + w + "x" + h;
        case ColoredShape(Triangle t, String color) ->
            color + " triangle";
    };
}
```

---

### Q58. What's the difference between sealed, final, and non-sealed? 🟡

**Three Modifiers Explained:**
```java
// 1. SEALED - Can be extended, but only by permitted subclasses
public sealed class Vehicle permits Car, Truck {}

// 2. FINAL - Cannot be extended at all
public final class Car extends Vehicle {
    // No further subclassing allowed
}

// 3. NON-SEALED - Opens up the hierarchy again
public non-sealed class Truck extends Vehicle {
    // Anyone can extend Truck
}

public class PickupTruck extends Truck {}  // OK
// public class SportsCar extends Car {}   // Error - Car is final

// Use cases:
// - Use SEALED when you want controlled extension
// - Use FINAL when no extension should ever happen
// - Use NON-SEALED when you want to allow extension again
```

---

### Java 17 - Pattern Matching

### Q59. What is Pattern Matching for instanceof? 🟢

**Before vs After:**
```java
// Before Java 16
if (obj instanceof String) {
    String s = (String) obj;  // Cast required
    System.out.println(s.toUpperCase());
}

// After Java 16
if (obj instanceof String s) {  // Pattern variable
    System.out.println(s.toUpperCase());  // No cast needed
}

// With logical operators
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s.toUpperCase());
}

// Scope of pattern variable
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s);  // s in scope
}
// System.out.println(s);  // Error - s not in scope

// In else branch
if (!(obj instanceof String s)) {
    // s not in scope here
} else {
    System.out.println(s);  // s in scope
}
```

---

### Q60. How does Pattern Matching for switch work? 🟡

**Detailed Answer covered in Q99-Q104 of File 04**

---

### Q61. What are guarded patterns? 🟡

**Guarded Patterns with `when`:**
```java
// Pattern with guard
static String classify(Object obj) {
    return switch (obj) {
        case String s when s.isEmpty() -> "Empty string";
        case String s when s.length() < 10 -> "Short string";
        case String s -> "Long string";
        case Integer i when i > 0 -> "Positive number";
        case Integer i when i < 0 -> "Negative number";
        case Integer i -> "Zero";
        case null -> "Null value";
        default -> "Unknown type";
    };
}

// Multiple conditions
static String processNumber(Integer num) {
    return switch (num) {
        case Integer i when i > 100 && i % 2 == 0 -> "Large even";
        case Integer i when i > 100 -> "Large odd";
        case Integer i when i % 2 == 0 -> "Small even";
        case Integer i -> "Small odd";
        case null -> "Null";
    };
}

// With records
record Point(int x, int y) {}

static String quadrant(Point p) {
    return switch (p) {
        case Point(int x, int y) when x > 0 && y > 0 -> "Quadrant I";
        case Point(int x, int y) when x < 0 && y > 0 -> "Quadrant II";
        case Point(int x, int y) when x < 0 && y < 0 -> "Quadrant III";
        case Point(int x, int y) when x > 0 && y < 0 -> "Quadrant IV";
        case Point(0, int y) -> "Y-axis";
        case Point(int x, 0) -> "X-axis";
        case Point(0, 0) -> "Origin";
    };
}
```

---

### Q62. How do you use Pattern Matching with Records? 🟡

**Record Patterns:**
```java
// Deconstruct records in patterns
record Point(int x, int y) {}

// Pattern matching with record
static void printPoint(Object obj) {
    if (obj instanceof Point(int x, int y)) {
        System.out.println("x: " + x + ", y: " + y);
    }
}

// In switch
static String describe(Object obj) {
    return switch (obj) {
        case Point(int x, int y) -> "Point at " + x + ", " + y;
        default -> "Not a point";
    };
}

// Nested records
record Circle(Point center, int radius) {}

static void printCircle(Object obj) {
    if (obj instanceof Circle(Point(int x, int y), int r)) {
        System.out.println("Circle at (" + x + ", " + y + ") with radius " + r);
    }
}

// With guards
static String categorize(Point p) {
    return switch (p) {
        case Point(0, 0) -> "Origin";
        case Point(int x, 0) -> "On X-axis";
        case Point(0, int y) -> "On Y-axis";
        case Point(int x, int y) when x == y -> "On diagonal";
        case Point(int x, int y) when x > 0 && y > 0 -> "Quadrant I";
        case Point(int x, int y) -> "Other quadrant";
    };
}
```

---

### Q63. What are the limitations of Pattern Matching? 🟡

**Current Limitations:**
```java
// 1. Cannot have duplicate patterns
switch (obj) {
    case String s -> "String 1";
    // case String s -> "String 2";  // Error - duplicate
}

// 2. Order matters with guards
switch (num) {
    case Integer i -> "Any integer";  // Catches all
    // case Integer i when i > 0 -> "Positive";  // Unreachable!
}

// Correct order
switch (num) {
    case Integer i when i > 0 -> "Positive";
    case Integer i -> "Other";  // OK
}

// 3. Pattern variables scope
if (obj instanceof String s) {
    System.out.println(s);  // OK
}
// System.out.println(s);  // Error - out of scope

// 4. Cannot pattern match on primitive types directly (yet)
int x = 5;
// switch (x) {
//     case int i when i > 0 -> "Positive";  // Not yet supported
// }

// 5. Null handling requires explicit case
switch (obj) {
    case String s -> s.length();  // NPE if obj is null!
    // Must add:
    case null -> 0;
    case String s -> s.length();  // Now safe
}
```

---

### Q64. How do Sealed Classes improve type safety? 🔴

**Type Safety Benefits:**
```java
// 1. Exhaustive checking
public sealed interface Result<T> permits Success, Failure {}
public record Success<T>(T value) implements Result<T> {}
public record Failure<T>(String error) implements Result<T> {}

// Compiler enforces handling all cases
public <T> T unwrap(Result<T> result) {
    return switch (result) {
        case Success<T>(T value) -> value;
        case Failure<T>(String error) -> throw new RuntimeException(error);
        // No default needed - compiler knows all types
    };
}

// If you add new type, compiler forces updates everywhere
public record Pending<T>() implements Result<T> {}  // New type
// Now unwrap() above won't compile until you handle Pending

// 2. API evolution control
public sealed interface PaymentMethod permits CreditCard, PayPal {
    // Library code
}

// Library can add new payment methods
public final class BankTransfer implements PaymentMethod {
    // New in v2.0
}

// Client code that uses switch will break at compile time
// Forces clients to handle new cases

// 3. Better than enums for data
// Enum limitations
enum Status {
    PENDING, APPROVED, REJECTED
    // Cannot attach different data to each constant
}

// Sealed classes solution
sealed interface Status permits Pending, Approved, Rejected {}
record Pending(LocalDateTime submittedAt) implements Status {}
record Approved(LocalDateTime approvedAt, String approver) implements Status {}
record Rejected(LocalDateTime rejectedAt, String reason) implements Status {}

// Each status can have different data
```

---

### Q65. What are common use cases for Sealed Classes? 🔴

**Use Cases:**
```java
// 1. Domain Modeling - Algebraic Data Types
public sealed interface Expression permits Constant, Add, Multiply {}
public record Constant(int value) implements Expression {}
public record Add(Expression left, Expression right) implements Expression {}
public record Multiply(Expression left, Expression right) implements Expression {}

public int evaluate(Expression expr) {
    return switch (expr) {
        case Constant(int value) -> value;
        case Add(var left, var right) -> evaluate(left) + evaluate(right);
        case Multiply(var left, var right) -> evaluate(left) * evaluate(right);
    };
}

// 2. State Machines
public sealed interface OrderState permits Created, Paid, Shipped, Delivered, Cancelled {}
public record Created(LocalDateTime at) implements OrderState {}
public record Paid(LocalDateTime at, String transactionId) implements OrderState {}
public record Shipped(LocalDateTime at, String trackingNumber) implements OrderState {}
public record Delivered(LocalDateTime at, String signature) implements OrderState {}
public record Cancelled(LocalDateTime at, String reason) implements OrderState {}

public OrderState transition(OrderState current, OrderEvent event) {
    return switch (current) {
        case Created c -> handleCreatedState(c, event);
        case Paid p -> handlePaidState(p, event);
        case Shipped s -> handleShippedState(s, event);
        case Delivered d -> throw new IllegalStateException("Order already delivered");
        case Cancelled c -> throw new IllegalStateException("Order cancelled");
    };
}

// 3. Command Pattern
public sealed interface Command permits CreateUser, UpdateUser, DeleteUser {}
public record CreateUser(String name, String email) implements Command {}
public record UpdateUser(Long id, String name, String email) implements Command {}
public record DeleteUser(Long id) implements Command {}

public void execute(Command command) {
    switch (command) {
        case CreateUser(String name, String email) -> userRepo.create(name, email);
        case UpdateUser(Long id, String name, String email) -> userRepo.update(id, name, email);
        case DeleteUser(Long id) -> userRepo.delete(id);
    }
}

// 4. Result/Either Pattern
public sealed interface Result<T, E> permits Ok, Err {}
public record Ok<T, E>(T value) implements Result<T, E> {}
public record Err<T, E>(E error) implements Result<T, E> {}

public <T, E> T getOrThrow(Result<T, E> result) {
    return switch (result) {
        case Ok<T, E>(T value) -> value;
        case Err<T, E>(E error) -> throw new RuntimeException(error.toString());
    };
}

// 5. Visitor Pattern Replacement
public sealed interface Shape permits Circle, Rectangle, Triangle {}

public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle(double r) -> Math.PI * r * r;
        case Rectangle(double w, double h) -> w * h;
        case Triangle(double b, double h) -> 0.5 * b * h;
    };
}

// No need for accept() methods or visitor interfaces
```

---

### Q66-Q85: Additional Topics

### Q66. How do you test code using Records and Sealed Classes? 🟡

**Testing Strategies:**
```java
// Testing Records
@Test
void testRecordEquality() {
    var person1 = new Person("Alice", 30);
    var person2 = new Person("Alice", 30);
    
    assertEquals(person1, person2);  // Auto-generated equals
    assertEquals(person1.hashCode(), person2.hashCode());
}

@Test
void testRecordValidation() {
    assertThrows(IllegalArgumentException.class, () -> {
        new Person("", 30);  // Invalid name
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
        new Person("Alice", -5);  // Invalid age
    });
}

// Testing Sealed Classes with Pattern Matching
@Test
void testPatternMatching() {
    sealed interface Result permits Success, Failure {}
    record Success(int value) implements Result {}
    record Failure(String error) implements Result {}
    
    Result success = new Success(42);
    Result failure = new Failure("Error");
    
    String successMsg = switch (success) {
        case Success(int v) -> "Got: " + v;
        case Failure(String e) -> "Error: " + e;
    };
    
    assertEquals("Got: 42", successMsg);
}

// Parametrized tests for all sealed subtypes
@ParameterizedTest
@MethodSource("provideShapes")
void testAreaCalculation(Shape shape, double expectedArea) {
    assertEquals(expectedArea, calculateArea(shape), 0.001);
}

static Stream<Arguments> provideShapes() {
    return Stream.of(
        Arguments.of(new Circle(5), 78.54),
        Arguments.of(new Rectangle(4, 5), 20.0),
        Arguments.of(new Triangle(6, 4), 12.0)
    );
}
```

---

*File complete with comprehensive Records, Sealed Classes, and Pattern Matching coverage!*

---

**📘 Continue to:** [04-JAVA21-MODERN-FEATURES.md](04-JAVA21-MODERN-FEATURES.md)
