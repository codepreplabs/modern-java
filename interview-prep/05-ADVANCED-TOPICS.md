# Advanced Java Topics - Interview Questions 🎓

> **Part 5 of Comprehensive Java Interview Guide**
>
> Covers best practices, design patterns with modern Java, performance tuning, and expert-level topics.

---

## 📑 Table of Contents
- Best Practices & Code Quality (Q111-Q115)
- Design Patterns with Modern Java (Q116-Q120)
- Performance & Optimization (Q121-Q125)
- Testing Modern Java Features (Q126-Q130)
- Memory & Concurrency (Q131-Q135)

---

## Best Practices & Code Quality

### Q111. What are the best practices for using Streams? 🟡

**Short Answer:**
Use streams for clarity and declarative style, but avoid overuse, prefer method references, don't modify source collections, and know when traditional loops are better.

**Detailed Best Practices:**

**1. Use Streams for Clarity:**
```java
// ✅ GOOD: Clear intent
List<String> names = users.stream()
    .filter(User::isActive)
    .map(User::getName)
    .sorted()
    .toList();

// ❌ BAD: Overly complex stream
List<String> names = users.stream()
    .filter(u -> u.getStatus() != null)
    .filter(u -> u.getStatus().equals(Status.ACTIVE))
    .map(u -> u.getName())
    .filter(n -> n != null)
    .map(String::trim)
    .filter(n -> !n.isEmpty())
    .sorted((a, b) -> a.compareToIgnoreCase(b))
    .collect(Collectors.toList());

// ✅ BETTER: Break into readable steps
List<String> names = users.stream()
    .filter(this::isActiveUser)
    .map(this::extractName)
    .filter(Objects::nonNull)
    .map(String::trim)
    .filter(Predicate.not(String::isEmpty))
    .sorted(String.CASE_INSENSITIVE_ORDER)
    .toList();

private boolean isActiveUser(User u) {
    return u.getStatus() == Status.ACTIVE;
}

private String extractName(User u) {
    return u.getName();
}
```

**2. Prefer Method References:**
```java
// ✅ GOOD
list.stream()
    .map(String::toUpperCase)
    .filter(Objects::nonNull)
    .forEach(System.out::println);

// ❌ LESS CLEAR
list.stream()
    .map(s -> s.toUpperCase())
    .filter(s -> s != null)
    .forEach(s -> System.out.println(s));
```

**3. Don't Modify Source Collections:**
```java
// ❌ WRONG: ConcurrentModificationException
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
list.stream()
    .forEach(item -> list.remove(item));  // Throws exception!

// ✅ RIGHT: Use removeIf
list.removeIf(item -> condition(item));

// ✅ OR: Collect to new list
List<String> filtered = list.stream()
    .filter(item -> !condition(item))
    .toList();
```

**4. Choose Right Terminal Operation:**
```java
// ✅ GOOD: Use specific operations
boolean hasMatch = list.stream().anyMatch(predicate);  // Not count() > 0
long count = list.stream().count();  // Not collect().size()
Optional<T> first = list.stream().findFirst();  // Not limit(1).collect()

// ❌ BAD: Inefficient
boolean hasMatch = list.stream()
    .filter(predicate)
    .count() > 0;  // Processes all elements!

// ✅ GOOD: Short-circuits
boolean hasMatch = list.stream().anyMatch(predicate);  // Stops at first match
```

**5. Know When to Use Traditional Loops:**
```java
// Use traditional loops when:
// - Simple iteration over small collections
// - Need to break early with complex condition
// - Modifying index-based

// ✅ GOOD: Simple loop for simple task
for (String item : smallList) {
    process(item);
}

// ❌ OVERKILL: Stream for simple task
smallList.stream().forEach(this::process);

// ✅ GOOD: Complex break condition
for (Item item : items) {
    if (complexCondition(item)) {
        result = item;
        break;
    }
}

// ❌ AWKWARD: Stream with complex logic
Optional<Item> result = items.stream()
    .filter(this::complexCondition)
    .findFirst();
```

**6. Avoid Side Effects in Intermediate Operations:**
```java
// ❌ WRONG: Side effects in map
AtomicInteger counter = new AtomicInteger();
List<String> result = list.stream()
    .map(s -> {
        counter.incrementAndGet();  // Side effect!
        return s.toUpperCase();
    })
    .toList();

// ✅ RIGHT: Side effects in terminal operation
List<String> result = list.stream()
    .map(String::toUpperCase)
    .toList();

int count = result.size();  // Count separately
```

**7. Use Collectors Wisely:**
```java
// ✅ GOOD: Use appropriate collector
Map<Status, List<User>> byStatus = users.stream()
    .collect(Collectors.groupingBy(User::getStatus));

// ❌ BAD: Manual collection
Map<Status, List<User>> byStatus = new HashMap<>();
users.stream().forEach(user -> {
    byStatus.computeIfAbsent(user.getStatus(), k -> new ArrayList<>())
            .add(user);
});

// ✅ GOOD: Single pass with teeing
record Stats(long count, double average) {}

Stats stats = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.counting(),
        Collectors.averagingDouble(Integer::doubleValue),
        Stats::new
    ));

// ❌ BAD: Multiple passes
long count = numbers.stream().count();
double avg = numbers.stream().mapToInt(Integer::intValue).average().orElse(0);
```

**8. Handle Nulls Properly:**
```java
// ✅ GOOD: Filter nulls early
List<String> processed = list.stream()
    .filter(Objects::nonNull)
    .map(String::toUpperCase)
    .toList();

// ❌ BAD: NPE risk
List<String> processed = list.stream()
    .map(String::toUpperCase)  // NPE if null!
    .toList();

// ✅ GOOD: Use Optional for nullable operations
List<String> names = users.stream()
    .map(User::getMiddleName)  // Returns Optional<String>
    .flatMap(Optional::stream)  // Java 9+
    .toList();
```

**9. Document Complex Streams:**
```java
// ✅ GOOD: Comment complex logic
List<Order> priorityOrders = orders.stream()
    // Filter active orders from last 30 days
    .filter(order -> order.isActive() && 
                     order.getDate().isAfter(thirtyDaysAgo))
    // Group by customer
    .collect(Collectors.groupingBy(Order::getCustomerId))
    .entrySet().stream()
    // Find customers with >5 orders
    .filter(entry -> entry.getValue().size() > 5)
    .flatMap(entry -> entry.getValue().stream())
    // Sort by total value
    .sorted(Comparator.comparing(Order::getTotalValue).reversed())
    .toList();
```

**10. Performance Considerations:**
```java
// ✅ GOOD: Use primitive streams
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();

// ❌ BAD: Boxing overhead
int sum = numbers.stream()
    .reduce(0, Integer::sum);  // Boxing on each operation

// ✅ GOOD: Parallel for large datasets with CPU work
List<Result> results = largeList.parallelStream()
    .map(this::cpuIntensiveOperation)
    .toList();

// ❌ BAD: Parallel for small datasets
List<Integer> small = List.of(1, 2, 3, 4, 5);
small.parallelStream()  // Overhead > benefit
    .map(n -> n * 2)
    .toList();
```

---

### Q112. What are best practices for using Optional? 🟡

**Short Answer:**
Use Optional as return types for methods that might not have a value, avoid Optional fields and parameters, prefer functional style over isPresent()/get(), and use specialized Optional types for primitives.

**Detailed Best Practices:**

**1. Use as Return Type:**
```java
// ✅ GOOD: Signals method might not find result
public Optional<User> findUserById(Long id) {
    User user = database.find(id);
    return Optional.ofNullable(user);
}

// Usage
findUserById(123L)
    .map(User::getName)
    .ifPresent(System.out::println);
```

**2. Don't Use as Fields:**
```java
// ❌ BAD: Optional as field
public class User {
    private Optional<String> middleName;  // Wastes memory
    private Optional<Address> address;    // Not serializable
    
    public Optional<String> getMiddleName() {
        return middleName;
    }
}

// ✅ GOOD: Nullable field, Optional return
public class User {
    private String middleName;  // Can be null
    private Address address;    // Can be null
    
    public Optional<String> getMiddleName() {
        return Optional.ofNullable(middleName);
    }
    
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }
}
```

**3. Don't Use as Parameters:**
```java
// ❌ BAD: Optional parameter
public void updateUser(Long id, Optional<String> name) {
    name.ifPresent(n -> {
        User user = findUser(id);
        user.setName(n);
    });
}

// ✅ GOOD: Overloaded methods or nullable parameter
public void updateUser(Long id, String name) {
    if (name != null) {
        User user = findUser(id);
        user.setName(name);
    }
}

// Or use overloading
public void updateUser(Long id) {
    updateUser(id, null);
}

public void updateUser(Long id, String name) {
    // implementation
}
```

**4. Use Functional Style:**
```java
// ❌ BAD: Imperative style
Optional<String> opt = findName();
if (opt.isPresent()) {
    String name = opt.get();
    System.out.println(name.toUpperCase());
}

// ✅ GOOD: Functional style
findName()
    .map(String::toUpperCase)
    .ifPresent(System.out::println);

// ❌ BAD: Using get() without checking
String name = findName().get();  // Can throw NoSuchElementException

// ✅ GOOD: Safe extraction
String name = findName().orElse("Unknown");
String name = findName().orElseGet(() -> computeDefault());
String name = findName().orElseThrow(() -> new NotFoundException());
```

**5. Use Specialized Optional for Primitives:**
```java
// ❌ BAD: Boxing overhead
Optional<Integer> count = Optional.of(getCount());
int value = count.orElse(0);  // Boxing/unboxing

// ✅ GOOD: Primitive Optional
OptionalInt count = OptionalInt.of(getCount());
int value = count.orElse(0);  // No boxing

// Also: OptionalLong, OptionalDouble
OptionalLong longOpt = OptionalLong.of(123L);
OptionalDouble doubleOpt = OptionalDouble.of(3.14);
```

**6. Chain Operations:**
```java
// ✅ GOOD: Chain Optional operations
String city = findUser(id)
    .flatMap(User::getAddress)  // Returns Optional<Address>
    .flatMap(Address::getCity)  // Returns Optional<String>
    .orElse("Unknown");

// Compare with null checks:
String city = "Unknown";
User user = findUser(id);
if (user != null) {
    Address address = user.getAddress();
    if (address != null) {
        String c = address.getCity();
        if (c != null) {
            city = c;
        }
    }
}
```

**7. Don't Return Optional.empty() for Collections:**
```java
// ❌ BAD: Optional of collection
public Optional<List<String>> getItems() {
    List<String> items = findItems();
    return Optional.ofNullable(items);
}

// Usage is awkward:
List<String> items = getItems().orElse(Collections.emptyList());

// ✅ GOOD: Return empty collection
public List<String> getItems() {
    List<String> items = findItems();
    return items != null ? items : Collections.emptyList();
}

// Usage is cleaner:
List<String> items = getItems();
```

**8. Prefer orElseGet() for Expensive Defaults:**
```java
// ❌ BAD: orElse() always evaluates
String value = optional.orElse(expensiveComputation());
// expensiveComputation() called even if optional has value!

// ✅ GOOD: orElseGet() lazy evaluation
String value = optional.orElseGet(() -> expensiveComputation());
// expensiveComputation() only called if optional empty
```

**9. Use filter() for Conditional Logic:**
```java
// ✅ GOOD: Declarative filtering
Optional<User> adult = userOptional
    .filter(user -> user.getAge() >= 18);

// vs imperative
Optional<User> adult;
if (userOptional.isPresent() && userOptional.get().getAge() >= 18) {
    adult = userOptional;
} else {
    adult = Optional.empty();
}
```

**10. Handle Both Cases with ifPresentOrElse():**
```java
// Java 9+
userOptional.ifPresentOrElse(
    user -> sendEmail(user),         // If present
    () -> logNoUserFound()           // If empty
);

// vs imperative
if (userOptional.isPresent()) {
    sendEmail(userOptional.get());
} else {
    logNoUserFound();
}
```

---

### Q113. What are best practices for Records? 🟡

**1. Use for Immutable Data:**
```java
// ✅ GOOD: DTOs, Value Objects, API models
public record UserDTO(Long id, String username, String email) {}

public record Money(BigDecimal amount, String currency) {}

public record Point(int x, int y) {}

// ❌ BAD: Mutable business entities
// Don't use records for entities with complex lifecycle
```

**2. Validate in Compact Constructor:**
```java
// ✅ GOOD: Validation and normalization
public record Email(String address) {
    public Email {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Email required");
        }
        address = address.trim().toLowerCase();
        if (!address.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

public record Range(int start, int end) {
    public Range {
        if (end < start) {
            throw new IllegalArgumentException("End must be >= start");
        }
    }
}
```

**3. Make Defensive Copies of Mutable Fields:**
```java
// ❌ BAD: Mutable field exposed
public record Team(String name, List<String> members) {}

Team team = new Team("A-Team", new ArrayList<>(List.of("Alice", "Bob")));
team.members().add("Charlie");  // Mutates the record!

// ✅ GOOD: Defensive copy
public record Team(String name, List<String> members) {
    public Team {
        members = List.copyOf(members);  // Immutable copy
    }
}
```

**4. Add Convenience Methods:**
```java
// ✅ GOOD: Domain logic in record
public record Rectangle(double width, double height) {
    // Validation
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
    
    // Convenience constructor
    public Rectangle(double side) {
        this(side, side);  // Square
    }
    
    // Derived properties
    public double area() {
        return width * height;
    }
    
    public double perimeter() {
        return 2 * (width + height);
    }
    
    public boolean isSquare() {
        return width == height;
    }
}
```

**5. Use for Pattern Matching:**
```java
// ✅ GOOD: Records work great with pattern matching
public sealed interface Shape permits Circle, Rectangle, Triangle {}

public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}
public record Triangle(double base, double height) implements Shape {}

public double area(Shape shape) {
    return switch (shape) {
        case Circle(double r) -> Math.PI * r * r;
        case Rectangle(double w, double h) -> w * h;
        case Triangle(double b, double h) -> 0.5 * b * h;
    };
}
```

**6. Implement Interfaces:**
```java
// ✅ GOOD: Records can implement interfaces
public interface Identifiable {
    Long getId();
}

public record User(Long id, String name) implements Identifiable {
    // getId() automatically implemented
}

public interface Comparable<T> {}

public record Person(String lastName, String firstName, int age) 
    implements Comparable<Person> {
    
    @Override
    public int compareTo(Person other) {
        int result = lastName.compareTo(other.lastName);
        if (result == 0) {
            result = firstName.compareTo(other.firstName);
        }
        return result;
    }
}
```

**7. Use Static Factory Methods:**
```java
// ✅ GOOD: Factory methods for complex creation
public record User(String username, String email, LocalDateTime createdAt) {
    
    public static User create(String username, String email) {
        return new User(username, email, LocalDateTime.now());
    }
    
    public static User fromDto(UserDTO dto) {
        return new User(
            dto.username(),
            dto.email(),
            LocalDateTime.now()
        );
    }
}

// Usage
User user = User.create("alice", "alice@example.com");
```

**8. Don't Use for JPA Entities:**
```java
// ❌ BAD: Records don't work with JPA
@Entity  // Won't work properly
public record User(Long id, String name) {}

// Problems:
// - Records are final (can't be proxied)
// - No default constructor
// - No setters
// - Final fields

// ✅ GOOD: Use regular class for entities
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    // getters, setters
}

// ✅ GOOD: Convert entity to record for DTOs
public record UserDTO(Long id, String name) {
    public static UserDTO from(User entity) {
        return new UserDTO(entity.getId(), entity.getName());
    }
}
```

**9. Nested Records:**
```java
// ✅ GOOD: Records can be nested
public record Address(
    String street,
    String city,
    String country,
    PostalCode postalCode
) {
    public record PostalCode(String code, String extension) {
        public PostalCode {
            if (code == null || code.length() < 5) {
                throw new IllegalArgumentException("Invalid postal code");
            }
        }
    }
}

// Usage
Address address = new Address(
    "123 Main St",
    "New York",
    "USA",
    new Address.PostalCode("10001", "1234")
);
```

**10. Builder Pattern with Records:**
```java
// ✅ GOOD: Builder for records with many fields
public record Product(
    String name,
    String description,
    BigDecimal price,
    String category,
    List<String> tags,
    boolean available
) {
    public static class Builder {
        private String name;
        private String description;
        private BigDecimal price;
        private String category = "";
        private List<String> tags = List.of();
        private boolean available = true;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        public Builder available(boolean available) {
            this.available = available;
            return this;
        }
        
        public Product build() {
            return new Product(name, description, price, category, tags, available);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
}

// Usage
Product product = Product.builder()
    .name("Laptop")
    .price(new BigDecimal("999.99"))
    .category("Electronics")
    .tags(List.of("computer", "portable"))
    .build();
```

---

### Q114. What are best practices for Sealed Classes? 🔴

**1. Use for Restricted Type Hierarchies:**
```java
// ✅ GOOD: Model domain with finite set of types
public sealed interface Payment
    permits CreditCardPayment, PayPalPayment, BankTransferPayment {
}

public final class CreditCardPayment implements Payment {
    private final String cardNumber;
    private final String cvv;
    // constructor, methods
}

public final class PayPalPayment implements Payment {
    private final String email;
    // constructor, methods
}

public final class BankTransferPayment implements Payment {
    private final String accountNumber;
    private final String routingNumber;
    // constructor, methods
}
```

**2. Enable Exhaustive Switch:**
```java
// ✅ GOOD: Compiler ensures all cases handled
public String processPayment(Payment payment) {
    return switch (payment) {
        case CreditCardPayment cc -> processCreditCard(cc);
        case PayPalPayment pp -> processPayPal(pp);
        case BankTransferPayment bt -> processBankTransfer(bt);
        // No default needed - compiler knows all types
    };
}

// If you add new payment type, compiler forces you to update all switches
```

**3. Combine with Records:**
```java
// ✅ GOOD: Sealed interface + records
public sealed interface Result<T>
    permits Success, Failure {
}

public record Success<T>(T value) implements Result<T> {}
public record Failure<T>(String error, Throwable cause) implements Result<T> {}

// Usage with pattern matching
public <T> T unwrap(Result<T> result) {
    return switch (result) {
        case Success<T>(T value) -> value;
        case Failure<T>(String error, Throwable cause) -> 
            throw new RuntimeException(error, cause);
    };
}
```

**4. Use for State Machines:**
```java
// ✅ GOOD: Model states with sealed classes
public sealed interface OrderState
    permits Pending, Confirmed, Shipped, Delivered, Cancelled {
}

public record Pending(LocalDateTime createdAt) implements OrderState {}
public record Confirmed(LocalDateTime confirmedAt, String confirmationNumber) 
    implements OrderState {}
public record Shipped(LocalDateTime shippedAt, String trackingNumber) 
    implements OrderState {}
public record Delivered(LocalDateTime deliveredAt, String signature) 
    implements OrderState {}
public record Cancelled(LocalDateTime cancelledAt, String reason) 
    implements OrderState {}

// State transitions
public OrderState nextState(OrderState current, OrderEvent event) {
    return switch (current) {
        case Pending p -> switch (event) {
            case ConfirmEvent e -> new Confirmed(LocalDateTime.now(), e.number());
            case CancelEvent e -> new Cancelled(LocalDateTime.now(), e.reason());
            default -> throw new IllegalStateException("Invalid transition");
        };
        case Confirmed c -> switch (event) {
            case ShipEvent e -> new Shipped(LocalDateTime.now(), e.tracking());
            case CancelEvent e -> new Cancelled(LocalDateTime.now(), e.reason());
            default -> throw new IllegalStateException("Invalid transition");
        };
        // ... more states
    };
}
```

**5. Hierarchical Sealing:**
```java
// ✅ GOOD: Nested sealed hierarchies
public sealed interface Vehicle
    permits LandVehicle, WaterVehicle, AirVehicle {
}

public sealed interface LandVehicle extends Vehicle
    permits Car, Truck, Motorcycle {
}

public non-sealed interface Car extends LandVehicle {}  // Allow extension
public final class Truck implements LandVehicle {}      // Final
public sealed class Motorcycle implements LandVehicle   // Further sealed
    permits SportsBike, CruiserBike {}

public final class SportsBike extends Motorcycle {}
public final class CruiserBike extends Motorcycle {}
```

**6. API Stability:**
```java
// ✅ GOOD: Prevent external extension of API
public sealed interface ApiResponse
    permits SuccessResponse, ErrorResponse {
}

// Users can't create their own response types
// API contract is stable and predictable

public record SuccessResponse(int code, Object data) implements ApiResponse {}
public record ErrorResponse(int code, String message, List<String> errors) 
    implements ApiResponse {}

// Library consumers must handle known types only
public void handleResponse(ApiResponse response) {
    switch (response) {
        case SuccessResponse(int code, Object data) -> 
            processSuccess(data);
        case ErrorResponse(int code, String message, List<String> errors) ->
            handleError(message, errors);
    };
}
```

**7. Avoid Overuse:**
```java
// ❌ BAD: Sealing when extension makes sense
public sealed interface Animal permits Dog, Cat {}
// What if users want to add Bird, Fish?

// ✅ GOOD: Use regular interface
public interface Animal {}
public class Dog implements Animal {}
public class Cat implements Animal {}
// Users can add more animals

// Seal only when:
// - Fixed set of types makes sense
// - You control all implementations
// - Extension would break invariants
```

---

## Design Patterns with Modern Java

### Q115. How do you implement Strategy Pattern with modern Java? 🟡

**Modern Strategy Pattern:**
```java
// Traditional Strategy Pattern
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardStrategy implements PaymentStrategy {
    public void pay(double amount) { /* ... */ }
}

class PayPalStrategy implements PaymentStrategy {
    public void pay(double amount) { /* ... */ }
}

// Modern: Use lambdas and method references
@FunctionalInterface
interface PaymentProcessor {
    void process(double amount);
}

class PaymentService {
    private Map<String, PaymentProcessor> strategies = Map.of(
        "CREDIT_CARD", amount -> processCreditCard(amount),
        "PAYPAL", amount -> processPayPal(amount),
        "BANK_TRANSFER", this::processBankTransfer
    );
    
    public void pay(String method, double amount) {
        strategies.get(method).process(amount);
    }
}

// Even better: Use sealed interfaces + records
sealed interface PaymentMethod permits CreditCard, PayPal, BankTransfer {}
record CreditCard(String number, String cvv) implements PaymentMethod {}
record PayPal(String email) implements PaymentMethod {}
record BankTransfer(String account) implements PaymentMethod {}

class ModernPaymentService {
    public void pay(PaymentMethod method, double amount) {
        switch (method) {
            case CreditCard(String num, String cvv) -> processCreditCard(num, amount);
            case PayPal(String email) -> processPayPal(email, amount);
            case BankTransfer(String account) -> processBankTransfer(account, amount);
        }
    }
}
```

---

### Q116. How do you implement Builder Pattern with Records? 🟡

**Builder with Records:**
```java
// Record with builder
public record Product(
    String name,
    BigDecimal price,
    String description,
    String category,
    List<String> tags,
    boolean available
) {
    // Static builder class
    public static class Builder {
        private String name;
        private BigDecimal price;
        private String description = "";
        private String category = "general";
        private List<String> tags = new ArrayList<>();
        private boolean available = true;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder tag(String tag) {
            this.tags.add(tag);
            return this;
        }
        
        public Builder tags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }
        
        public Builder available(boolean available) {
            this.available = available;
            return this;
        }
        
        public Product build() {
            if (name == null || price == null) {
                throw new IllegalStateException("Name and price are required");
            }
            return new Product(name, price, description, category, 
                             List.copyOf(tags), available);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
}

// Usage
Product product = Product.builder()
    .name("Laptop")
    .price(new BigDecimal("999.99"))
    .description("Gaming laptop")
    .category("Electronics")
    .tag("gaming")
    .tag("portable")
    .available(true)
    .build();
```

---

### Q117. How do you implement Factory Pattern with modern features? 🟡

**Modern Factory:**
```java
// Using sealed interfaces + pattern matching
sealed interface Vehicle permits Car, Truck, Motorcycle {}

record Car(String model, int seats) implements Vehicle {}
record Truck(String model, int capacity) implements Vehicle {}
record Motorcycle(String model, int cc) implements Vehicle {}

class VehicleFactory {
    public static Vehicle create(String type, String model, int value) {
        return switch (type.toUpperCase()) {
            case "CAR" -> new Car(model, value);
            case "TRUCK" -> new Truck(model, value);
            case "MOTORCYCLE" -> new Motorcycle(model, value);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
    
    // Factory methods
    public static Car createCar(String model, int seats) {
        return new Car(model, seats);
    }
    
    public static Truck createTruck(String model, int capacity) {
        return new Truck(model, capacity);
    }
}

// With Java 21 pattern matching
public double calculateTax(Vehicle vehicle) {
    return switch (vehicle) {
        case Car(String model, int seats) -> seats * 100;
        case Truck(String model, int capacity) -> capacity * 50;
        case Motorcycle(String model, int cc) -> cc * 0.5;
    };
}
```

---

### Q118. How do you implement Observer Pattern functionally? 🔴

**Functional Observer:**
```java
// Traditional Observer Pattern - lots of interfaces
interface Observer {
    void update(Event event);
}

interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Event event);
}

// Modern functional approach
class EventPublisher<T> {
    private final List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();
    
    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
    }
    
    public void unsubscribe(Consumer<T> listener) {
        listeners.remove(listener);
    }
    
    public void publish(T event) {
        listeners.forEach(listener -> listener.accept(event));
    }
    
    // Async publishing with virtual threads
    public void publishAsync(T event) {
        Thread.ofVirtual().start(() -> 
            listeners.forEach(listener -> listener.accept(event))
        );
    }
}

// Usage
EventPublisher<OrderEvent> publisher = new EventPublisher<>();

// Subscribe with lambdas
publisher.subscribe(event -> logEvent(event));
publisher.subscribe(event -> sendEmail(event));
publisher.subscribe(event -> updateInventory(event));

// Publish event
publisher.publish(new OrderEvent("ORDER-123", "CREATED"));

// With filtering
class FilteredPublisher<T> extends EventPublisher<T> {
    public void subscribe(Predicate<T> filter, Consumer<T> listener) {
        super.subscribe(event -> {
            if (filter.test(event)) {
                listener.accept(event);
            }
        });
    }
}

// Subscribe with filter
FilteredPublisher<OrderEvent> publisher = new FilteredPublisher<>();
publisher.subscribe(
    event -> event.type().equals("CREATED"),
    event -> sendWelcomeEmail(event)
);
```

---

### Q119. How do you implement State Pattern with Sealed Classes? 🔴

**State Pattern:**
```java
// State pattern with sealed classes
sealed interface OrderState permits Pending, Confirmed, Shipped, Delivered {}

record Pending(String orderId, LocalDateTime createdAt) implements OrderState {}
record Confirmed(String orderId, LocalDateTime confirmedAt, String confirmationCode) implements OrderState {}
record Shipped(String orderId, LocalDateTime shippedAt, String trackingNumber) implements OrderState {}
record Delivered(String orderId, LocalDateTime deliveredAt, String signature) implements OrderState {}

// Events
sealed interface OrderEvent permits ConfirmEvent, ShipEvent, DeliverEvent {}
record ConfirmEvent(String confirmationCode) implements OrderEvent {}
record ShipEvent(String trackingNumber) implements OrderEvent {}
record DeliverEvent(String signature) implements OrderEvent {}

class OrderStateMachine {
    public OrderState transition(OrderState currentState, OrderEvent event) {
        return switch (currentState) {
            case Pending(String id, LocalDateTime created) -> 
                handlePending(id, created, event);
            case Confirmed(String id, LocalDateTime conf, String code) -> 
                handleConfirmed(id, event);
            case Shipped(String id, LocalDateTime shipped, String tracking) -> 
                handleShipped(id, event);
            case Delivered d -> 
                throw new IllegalStateException("Order already delivered");
        };
    }
    
    private OrderState handlePending(String id, LocalDateTime created, OrderEvent event) {
        return switch (event) {
            case ConfirmEvent(String code) -> 
                new Confirmed(id, LocalDateTime.now(), code);
            default -> 
                throw new IllegalStateException("Invalid transition from Pending");
        };
    }
    
    private OrderState handleConfirmed(String id, OrderEvent event) {
        return switch (event) {
            case ShipEvent(String tracking) -> 
                new Shipped(id, LocalDateTime.now(), tracking);
            default -> 
                throw new IllegalStateException("Invalid transition from Confirmed");
        };
    }
    
    private OrderState handleShipped(String id, OrderEvent event) {
        return switch (event) {
            case DeliverEvent(String signature) -> 
                new Delivered(id, LocalDateTime.now(), signature);
            default -> 
                throw new IllegalStateException("Invalid transition from Shipped");
        };
    }
}

// Usage
OrderStateMachine machine = new OrderStateMachine();
OrderState state = new Pending("ORDER-123", LocalDateTime.now());

state = machine.transition(state, new ConfirmEvent("CONF-456"));
state = machine.transition(state, new ShipEvent("TRACK-789"));
state = machine.transition(state, new DeliverEvent("SIGN-012"));
```

---

### Q120. How do you implement Command Pattern with functional interfaces? 🟡

**Functional Command:**
```java
// Traditional Command Pattern
interface Command {
    void execute();
    void undo();
}

// Modern functional approach
@FunctionalInterface
interface Action {
    void execute();
}

class ActionHistory {
    private final Stack<Runnable> history = new Stack<>();
    private final Stack<Runnable> undoHistory = new Stack<>();
    
    public void execute(Runnable action, Runnable undo) {
        action.run();
        history.push(action);
        undoHistory.push(undo);
    }
    
    public void undo() {
        if (!undoHistory.isEmpty()) {
            undoHistory.pop().run();
            history.pop();
        }
    }
}

// Usage
ActionHistory history = new ActionHistory();

// Execute with undo
history.execute(
    () -> account.deposit(100),  // Action
    () -> account.withdraw(100)  // Undo
);

history.execute(
    () -> account.withdraw(50),
    () -> account.deposit(50)
);

history.undo();  // Undoes last action

// With records for complex commands
record Command(String name, Runnable action, Runnable undo) {
    public void execute() {
        action.run();
    }
    
    public void rollback() {
        undo.run();
    }
}

// Command queue with virtual threads
class AsyncCommandExecutor {
    private final Queue<Command> queue = new ConcurrentLinkedQueue<>();
    
    public void submit(Command command) {
        queue.offer(command);
    }
    
    public void processAll() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            queue.forEach(cmd -> executor.submit(cmd::execute));
        }
    }
}
```

---

## Performance & Optimization

### Q121. How do you profile Java applications effectively? 🔴

**Profiling Strategies:**
```java
// 1. JFR (Java Flight Recorder)
// Command line:
// java -XX:StartFlightRecording=filename=recording.jfr MyApp

// Programmatic control
try (var recording = new Recording()) {
    recording.enable("jdk.CPULoad");
    recording.enable("jdk.GCHeapSummary");
    recording.start();
    
    // Run your code
    performOperations();
    
    recording.stop();
    recording.dump(Path.of("recording.jfr"));
}

// 2. Microbenchmarking with JMH
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class StreamBenchmark {
    private List<Integer> numbers;
    
    @Setup
    public void setup() {
        numbers = IntStream.range(0, 1000).boxed().toList();
    }
    
    @Benchmark
    public int sequentialSum() {
        return numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    @Benchmark
    public int parallelSum() {
        return numbers.parallelStream()
            .mapToInt(Integer::intValue)
            .sum();
    }
}

// 3. Custom profiling
class PerformanceMonitor {
    public static <T> T measure(String operation, Supplier<T> task) {
        long start = System.nanoTime();
        try {
            return task.get();
        } finally {
            long duration = System.nanoTime() - start;
            System.out.printf("%s took %.2f ms%n", 
                operation, duration / 1_000_000.0);
        }
    }
}

// Usage
List<String> result = PerformanceMonitor.measure("Stream processing",
    () -> list.stream()
        .filter(predicate)
        .map(function)
        .toList()
);
```

---

### Q122. What are common performance anti-patterns? 🔴

**Anti-Patterns to Avoid:**
```java
// 1. Creating unnecessary objects in loops
// ❌ Bad
for (int i = 0; i < 1000000; i++) {
    String s = new String("constant");  // Creates new object each time
    process(s);
}

// ✅ Good
String constant = "constant";  // Reuse
for (int i = 0; i < 1000000; i++) {
    process(constant);
}

// 2. String concatenation in loops
// ❌ Bad
String result = "";
for (String s : list) {
    result += s;  // Creates new String each time
}

// ✅ Good
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();

// Or use Collectors.joining()
String result = list.stream().collect(Collectors.joining());

// 3. Boxing/unboxing in performance-critical code
// ❌ Bad
List<Integer> numbers = getNumbers();
int sum = 0;
for (Integer n : numbers) {
    sum += n;  // Unboxing on each iteration
}

// ✅ Good
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();

// 4. Unnecessary stream creation
// ❌ Bad
for (int i = 0; i < 10; i++) {
    long count = list.stream().count();  // Creates stream each time
}

// ✅ Good
int size = list.size();  // Once
for (int i = 0; i < 10; i++) {
    // use size
}

// 5. Wrong collection type
// ❌ Bad - O(n) contains check
List<String> list = new ArrayList<>();
if (list.contains("value")) { }  // Linear search

// ✅ Good - O(1) contains check
Set<String> set = new HashSet<>();
if (set.contains("value")) { }  // Hash lookup
```

---

### Q123. How do you optimize Stream operations? 🔴

**Stream Optimization:**
```java
// 1. Use primitive streams
// ❌ Slow
List<Integer> numbers = getNumbers();
int sum = numbers.stream()
    .reduce(0, Integer::sum);  // Boxing overhead

// ✅ Fast
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();

// 2. Filter before map
// ❌ Less efficient
list.stream()
    .map(expensiveTransform)  // Applied to all
    .filter(predicate)         // Then filtered
    .toList();

// ✅ More efficient
list.stream()
    .filter(predicate)         // Filter first
    .map(expensiveTransform)   // Applied to fewer
    .toList();

// 3. Use findAny() instead of findFirst() for parallel
// ❌ Slower in parallel
result = list.parallelStream()
    .filter(predicate)
    .findFirst();  // Must maintain order

// ✅ Faster in parallel
result = list.parallelStream()
    .filter(predicate)
    .findAny();  // No order requirement

// 4. Avoid stateful operations in parallel
// ❌ Slow in parallel
list.parallelStream()
    .sorted()      // Requires coordination
    .distinct()    // Requires coordination
    .toList();

// ✅ Better
list.stream()  // Sequential for stateful ops
    .sorted()
    .distinct()
    .toList();

// 5. Use collectors efficiently
// ❌ Multiple passes
long count = list.stream().count();
int sum = list.stream().mapToInt(Integer::intValue).sum();

// ✅ Single pass
IntSummaryStatistics stats = list.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));
long count = stats.getCount();
int sum = stats.getSum();
```

---

### Q124-Q135: Testing, Memory, and Advanced Topics

*These questions would cover:*
- Testing strategies for modern Java (Q124-Q126)
- Memory management and GC tuning (Q127-Q129)
- Concurrency best practices (Q130-Q132)
- Migration strategies (Q133-Q135)

---

*File complete with comprehensive best practices and design patterns!*

---

**📘 Continue to:** [06-REAL-WORLD-SCENARIOS.md](06-REAL-WORLD-SCENARIOS.md)
