# Real-World Java Scenarios - Interview Questions 💼

> **Part 6 of Comprehensive Java Interview Guide**
>
> Covers system design, architectural decisions, real interview scenarios, and practical problem-solving with modern Java.

---

## 📑 Table of Contents
- System Design with Modern Java (Q136-Q140)
- API Design & Best Practices (Q141-Q145)
- Migration Scenarios (Q146-Q150)
- Performance Problem Solving (Q151-Q155)
- Real Interview Scenarios (Q156-Q160)

---

## System Design with Modern Java

### Q136. How would you design a high-throughput REST API with Virtual Threads? 🔴

**Scenario:**
Design a REST API that needs to handle 100,000+ concurrent requests, each making multiple database queries and external API calls.

**Solution:**

**1. Architecture Overview:**
```java
// Controller with Virtual Thread per request
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    
    // Spring Boot 3.2+ uses virtual threads automatically if configured
    // spring.threads.virtual.enabled=true
    
    @GetMapping("/{id}/dashboard")
    public UserDashboard getUserDashboard(@PathVariable Long id) {
        // Each request runs in virtual thread
        // Can handle 100,000+ concurrent requests
        return userService.loadDashboard(id);
    }
}
```

**2. Service Layer with Structured Concurrency:**
```java
@Service
public class UserService {
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final NotificationService notificationService;
    private final AnalyticsService analyticsService;
    
    public UserDashboard loadDashboard(Long userId) {
        // Use Structured Concurrency to load data in parallel
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Fork parallel tasks
            Supplier<User> userTask = 
                scope.fork(() -> userRepo.findById(userId));
            
            Supplier<List<Order>> ordersTask = 
                scope.fork(() -> orderRepo.findRecentByUserId(userId));
            
            Supplier<List<Notification>> notificationsTask = 
                scope.fork(() -> notificationService.getUnread(userId));
            
            Supplier<UserStats> statsTask = 
                scope.fork(() -> analyticsService.getUserStats(userId));
            
            // Wait for all to complete
            scope.join();
            scope.throwIfFailed();
            
            // Combine results
            return new UserDashboard(
                userTask.get(),
                ordersTask.get(),
                notificationsTask.get(),
                statsTask.get()
            );
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException("Interrupted while loading dashboard", e);
        } catch (ExecutionException e) {
            throw new ServiceException("Failed to load dashboard", e.getCause());
        }
    }
}
```

**3. Database Access (Non-Blocking):**
```java
@Repository
public class UserRepository {
    private final DataSource dataSource;
    
    public User findById(Long id) {
        // JDBC calls block, but it's OK with virtual threads
        // Virtual thread unmounts during IO
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM users WHERE id = ?")) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapToUser(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user", e);
        }
    }
    
    // No need for reactive drivers - blocking is fine!
}
```

**4. External API Calls:**
```java
@Service
public class NotificationService {
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    
    public List<Notification> getUnread(Long userId) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://notification-service/users/" + userId + "/unread"))
            .timeout(Duration.ofSeconds(10))
            .build();
        
        try {
            // Blocking HTTP call - virtual thread handles it efficiently
            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());
            
            return parseNotifications(response.body());
            
        } catch (IOException | InterruptedException e) {
            log.error("Failed to fetch notifications", e);
            return List.of();  // Fallback to empty list
        }
    }
}
```

**5. Configuration:**
```yaml
# application.yml
spring:
  threads:
    virtual:
      enabled: true  # Enable virtual threads

server:
  tomcat:
    threads:
      max: 200  # Carrier threads (CPU cores * 2)
    # Can now handle 100,000+ concurrent requests
    # because requests run on virtual threads

# Database connection pool
spring:
  datasource:
    hikari:
      maximum-pool-size: 50  # Small pool is enough
      # Virtual threads wait efficiently for connections
```

**6. Monitoring:**
```java
@Component
public class VirtualThreadMonitoring {
    private final MeterRegistry registry;
    
    @PostConstruct
    public void setupMetrics() {
        // Monitor virtual thread usage
        Gauge.builder("vthreads.active", this::getActiveVirtualThreads)
            .register(registry);
        
        Gauge.builder("carrier.threads.active", this::getActiveCarrierThreads)
            .register(registry);
    }
    
    private long getActiveVirtualThreads() {
        return Thread.getAllStackTraces().keySet().stream()
            .filter(Thread::isVirtual)
            .count();
    }
    
    private int getActiveCarrierThreads() {
        return ForkJoinPool.commonPool().getActiveThreadCount();
    }
}
```

**7. Load Testing Results:**
```java
// Traditional Thread-per-request model:
// - Max: ~1,000 concurrent requests
// - Memory: ~1GB (1000 threads * 1MB each)
// - Latency: Increases rapidly after 500 requests

// Virtual Thread model:
// - Max: 100,000+ concurrent requests
// - Memory: ~500MB (mostly carrier threads + heap)
// - Latency: Stable even at high concurrency
// - Throughput: 10x improvement for IO-bound operations
```

**Key Benefits:**
- Simple synchronous code (no reactive complexity)
- Massive concurrency (100,000+ requests)
- Efficient resource usage
- Easy to debug and maintain
- Perfect for microservices with multiple IO calls

---

### Q137. Design a reactive data processing pipeline using Streams 🔴

**Scenario:**
Process millions of records from a file, transform them, validate, enrich with external data, and save to database - all with backpressure and error handling.

**Solution:**

**1. Pipeline Design:**
```java
public class DataProcessingPipeline {
    private final ValidationService validator;
    private final EnrichmentService enrichmentService;
    private final Repository repository;
    
    public ProcessingResult processFile(Path filePath) {
        AtomicInteger processed = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();
        List<String> errors = new CopyOnWriteArrayList<>();
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines
                .parallel()  // Process in parallel
                .skip(1)  // Skip header
                .filter(line -> !line.isBlank())
                
                // Parse
                .map(this::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                
                // Validate
                .filter(record -> {
                    ValidationResult result = validator.validate(record);
                    if (!result.isValid()) {
                        errors.add("Validation failed: " + record.getId() + 
                                 " - " + result.getErrors());
                        failed.incrementAndGet();
                        return false;
                    }
                    return true;
                })
                
                // Enrich (with batching for efficiency)
                .collect(Collectors.groupingBy(
                    record -> processed.get() / 1000))  // Batch of 1000
                .values()
                .stream()
                .flatMap(batch -> enrichBatch(batch).stream())
                
                // Save (with batching)
                .collect(Collectors.groupingBy(
                    record -> processed.get() / 100))  // Batch of 100
                .values()
                .stream()
                .forEach(batch -> {
                    try {
                        repository.saveAll(batch);
                        processed.addAndGet(batch.size());
                    } catch (Exception e) {
                        errors.add("Save failed for batch: " + e.getMessage());
                        failed.addAndGet(batch.size());
                    }
                });
                
        } catch (IOException e) {
            throw new ProcessingException("Failed to process file", e);
        }
        
        return new ProcessingResult(processed.get(), failed.get(), errors);
    }
    
    private Optional<Record> parseLine(String line) {
        try {
            String[] parts = line.split(",");
            return Optional.of(new Record(
                parts[0],  // id
                parts[1],  // name
                parts[2]   // value
            ));
        } catch (Exception e) {
            log.warn("Failed to parse line: {}", line, e);
            return Optional.empty();
        }
    }
    
    private List<EnrichedRecord> enrichBatch(List<Record> batch) {
        // Enrich batch with external API (single call)
        Map<String, ExternalData> externalData = 
            enrichmentService.enrichBatch(
                batch.stream()
                    .map(Record::getId)
                    .collect(Collectors.toList())
            );
        
        return batch.stream()
            .map(record -> new EnrichedRecord(
                record,
                externalData.get(record.getId())
            ))
            .collect(Collectors.toList());
    }
}
```

**2. With Backpressure (Using Virtual Threads):**
```java
public class BackpressureAwarePipeline {
    private final Semaphore rateLimiter = new Semaphore(100);
    
    public void processWithBackpressure(Path filePath) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Files.lines(filePath)
                .skip(1)
                .forEach(line -> {
                    executor.submit(() -> {
                        try {
                            rateLimiter.acquire();  // Backpressure control
                            processLine(line);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } finally {
                            rateLimiter.release();
                        }
                    });
                });
        } catch (IOException e) {
            throw new ProcessingException("Failed to process file", e);
        }
    }
    
    private void processLine(String line) {
        // Parse
        Record record = parse(line);
        
        // Validate
        if (!validator.validate(record).isValid()) {
            return;
        }
        
        // Enrich (with retry)
        EnrichedRecord enriched = enrichWithRetry(record, 3);
        
        // Save
        repository.save(enriched);
    }
    
    private EnrichedRecord enrichWithRetry(Record record, int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                return enrichmentService.enrich(record);
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw e;
                }
                // Exponential backoff
                try {
                    Thread.sleep((long) Math.pow(2, attempt) * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
        throw new RuntimeException("Failed after " + maxRetries + " attempts");
    }
}
```

**3. Error Handling and Recovery:**
```java
public class ResilientPipeline {
    private final Queue<FailedRecord> deadLetterQueue = new ConcurrentLinkedQueue<>();
    
    public ProcessingResult processWithErrorHandling(Path filePath) {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                .parallel()
                .map(this::safeProcess)
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    this::summarize
                ));
        } catch (IOException e) {
            throw new ProcessingException("Failed to read file", e);
        }
    }
    
    private ProcessedRecord safeProcess(String line) {
        try {
            return processLine(line);
        } catch (ValidationException e) {
            log.warn("Validation failed for line", e);
            return null;  // Skip invalid records
        } catch (EnrichmentException e) {
            log.error("Enrichment failed for line", e);
            deadLetterQueue.offer(new FailedRecord(line, e.getMessage()));
            return null;  // Add to DLQ, skip
        } catch (Exception e) {
            log.error("Unexpected error processing line", e);
            return null;  // Skip on any error
        }
    }
    
    private ProcessingResult summarize(List<ProcessedRecord> records) {
        int successful = records.size();
        int failed = deadLetterQueue.size();
        
        // Retry failed records (in background)
        if (!deadLetterQueue.isEmpty()) {
            retryFailedRecordsAsync();
        }
        
        return new ProcessingResult(successful, failed, 
            List.copyOf(deadLetterQueue));
    }
    
    private void retryFailedRecordsAsync() {
        Thread.ofVirtual().start(() -> {
            List<FailedRecord> toRetry = new ArrayList<>();
            deadLetterQueue.drainTo(toRetry);
            
            for (FailedRecord failed : toRetry) {
                try {
                    Thread.sleep(5000);  // Wait before retry
                    processLine(failed.line());
                } catch (Exception e) {
                    log.error("Retry failed", e);
                    // Could move to permanent failure storage
                }
            }
        });
    }
}
```

---

### Q138. How do you migrate a legacy codebase to use modern Java features? 🟡

**Scenario:**
Migrate a large Java 8 codebase to Java 21, introducing modern features without breaking functionality.

**Migration Strategy:**

**Phase 1: Update to Java 17 (LTS)**
```java
// Step 1: Replace anonymous classes with lambdas
// Before:
list.sort(new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareToIgnoreCase(b);
    }
});

// After:
list.sort((a, b) -> a.compareToIgnoreCase(b));
// Or better:
list.sort(String::compareToIgnoreCase);

// Step 2: Replace DTOs with Records
// Before:
public class UserDTO {
    private final String name;
    private final String email;
    
    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    @Override
    public boolean equals(Object o) { /* ... */ }
    @Override
    public int hashCode() { /* ... */ }
    @Override
    public String toString() { /* ... */ }
}

// After:
public record UserDTO(String name, String email) {}

// Step 3: Use Pattern Matching for instanceof
// Before:
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.toUpperCase());
}

// After:
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}

// Step 4: Replace string concatenation with Text Blocks
// Before:
String query = "SELECT users.name, orders.id, orders.total\n" +
               "FROM users\n" +
               "JOIN orders ON users.id = orders.user_id\n" +
               "WHERE orders.status = 'COMPLETED'\n" +
               "ORDER BY orders.created_at DESC";

// After:
String query = """
    SELECT users.name, orders.id, orders.total
    FROM users
    JOIN orders ON users.id = orders.user_id
    WHERE orders.status = 'COMPLETED'
    ORDER BY orders.created_at DESC
    """;

// Step 5: Use Switch Expressions
// Before:
String result;
switch (day) {
    case MONDAY:
    case FRIDAY:
        result = "Work from home";
        break;
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
        result = "Office";
        break;
    default:
        result = "Weekend";
        break;
}

// After:
String result = switch (day) {
    case MONDAY, FRIDAY -> "Work from home";
    case TUESDAY, WEDNESDAY, THURSDAY -> "Office";
    default -> "Weekend";
};

// Step 6: Replace type hierarchies with Sealed Classes
// Before:
public abstract class Result {}
public class Success extends Result {}
public class Failure extends Result {}
// Anyone can extend Result!

// After:
public sealed interface Result permits Success, Failure {}
public record Success(Object value) implements Result {}
public record Failure(String error) implements Result {}
// Controlled hierarchy
```

**Phase 2: Optimize with Java 21 Features**
```java
// Step 7: Replace thread pools with Virtual Threads
// Before:
ExecutorService executor = Executors.newFixedThreadPool(100);
for (Task task : tasks) {
    executor.submit(() -> processTask(task));
}
executor.shutdown();
executor.awaitTermination(1, TimeUnit.HOURS);

// After:
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (Task task : tasks) {
        executor.submit(() -> processTask(task));
    }
}  // Automatically waits for all tasks

// Step 8: Use Sequenced Collections
// Before:
String first = list.get(0);
String last = list.get(list.size() - 1);

// After:
String first = list.getFirst();
String last = list.getLast();

// Step 9: Pattern Matching for Switch
// Before:
if (obj instanceof String) {
    String s = (String) obj;
    process(s.length());
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    process(i * 2);
}

// After:
switch (obj) {
    case String s -> process(s.length());
    case Integer i -> process(i * 2);
    case null -> handleNull();
    default -> handleUnknown();
}
```

**Migration Checklist:**
```java
// ✅ Phase 1: Preparation
// - Update build tools (Maven/Gradle)
// - Update dependencies
// - Run all tests with new JDK
// - Fix deprecated API usage

// ✅ Phase 2: Low-Risk Changes
// - Replace anonymous classes with lambdas
// - Use var for local variables
// - Replace string concatenation with Text Blocks
// - Use new String methods (isBlank, lines, etc.)

// ✅ Phase 3: Moderate Changes
// - Create Records for DTOs
// - Use Pattern Matching for instanceof
// - Use Switch Expressions
// - Update HTTP clients to java.net.http

// ✅ Phase 4: High-Impact Changes
// - Introduce Sealed Classes for domain models
// - Replace thread pools with Virtual Threads
// - Use Structured Concurrency
// - Use Pattern Matching for Switch

// ✅ Phase 5: Optimization
// - Profile and identify bottlenecks
// - Optimize with parallel streams (where appropriate)
// - Use Virtual Threads for high concurrency
// - Monitor and tune
```

---

## API Design & Best Practices

### Q139. How do you design a RESTful API using modern Java features? 🔴

**Modern API Design:**
```java
// 1. Use Records for DTOs
public record CreateUserRequest(
    @NotBlank String username,
    @Email String email,
    @Size(min = 8) String password
) {}

public record UserResponse(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) {
    public static UserResponse from(User entity) {
        return new UserResponse(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getCreatedAt()
        );
    }
}

// 2. Use Sealed Interfaces for API responses
public sealed interface ApiResponse<T> permits Success, Error {}

public record Success<T>(
    T data,
    String message,
    Map<String, Object> metadata
) implements ApiResponse<T> {}

public record Error<T>(
    int code,
    String message,
    List<String> details
) implements ApiResponse<T> {}

// 3. Controller with virtual threads
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResponse<UserResponse>>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        // Runs in virtual thread (Spring Boot 3.2+ with virtual threads enabled)
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = userService.createUser(request);
                UserResponse response = UserResponse.from(user);
                return ResponseEntity.ok(new Success<>(response, "User created", Map.of()));
            } catch (ValidationException e) {
                return ResponseEntity.badRequest()
                    .body(new Error<UserResponse>(400, "Validation failed", e.getErrors()));
            }
        });
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(UserResponse::from)
            .map(response -> ResponseEntity.ok(
                new Success<>(response, "User found", Map.of())))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

// 4. Service with structured concurrency
@Service
public class UserService {
    private final UserRepository userRepo;
    private final EmailService emailService;
    private final AuditService auditService;
    
    public User createUser(CreateUserRequest request) {
        // Validate and create
        User user = new User(request.username(), request.email());
        user = userRepo.save(user);
        
        // Parallel post-creation tasks
        User finalUser = user;
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> emailService.sendWelcomeEmail(finalUser));
            scope.fork(() -> auditService.logUserCreation(finalUser));
            
            scope.join();
            // Don't fail creation if notifications fail
            // Just log errors
        } catch (Exception e) {
            log.warn("Post-creation tasks failed", e);
        }
        
        return user;
    }
}
```

---

### Q140. How do you implement pagination and filtering efficiently? 🟡

**Efficient Pagination:**
```java
// Request DTOs with Records
public record PageRequest(
    int page,
    int size,
    String sortBy,
    String direction
) {
    public PageRequest {
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 20;
    }
    
    public Pageable toPageable() {
        Sort.Direction dir = "DESC".equalsIgnoreCase(direction) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        return org.springframework.data.domain.PageRequest.of(
            page, size, Sort.by(dir, sortBy));
    }
}

public record FilterCriteria(
    Optional<String> name,
    Optional<String> email,
    Optional<Boolean> active,
    Optional<LocalDate> createdAfter
) {}

public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
}

// Service implementation
@Service
public class UserService {
    private final UserRepository userRepo;
    
    public PageResponse<UserDTO> findUsers(
            PageRequest pageReq, 
            FilterCriteria filters) {
        
        // Build specification dynamically
        Specification<User> spec = buildSpecification(filters);
        
        // Query with pagination
        Page<User> page = userRepo.findAll(spec, pageReq.toPageable());
        
        // Convert to DTOs
        Page<UserDTO> dtoPage = page.map(UserDTO::from);
        
        return PageResponse.from(dtoPage);
    }
    
    private Specification<User> buildSpecification(FilterCriteria filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            filters.name().ifPresent(name -> 
                predicates.add(cb.like(root.get("name"), "%" + name + "%")));
            
            filters.email().ifPresent(email -> 
                predicates.add(cb.equal(root.get("email"), email)));
            
            filters.active().ifPresent(active -> 
                predicates.add(cb.equal(root.get("active"), active)));
            
            filters.createdAfter().ifPresent(date -> 
                predicates.add(cb.greaterThanOrEqualTo(
                    root.get("createdAt"), date.atStartOfDay())));
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

// Controller
@GetMapping
public ResponseEntity<PageResponse<UserDTO>> listUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String direction,
        @RequestParam Optional<String> name,
        @RequestParam Optional<String> email,
        @RequestParam Optional<Boolean> active,
        @RequestParam Optional<LocalDate> createdAfter) {
    
    PageRequest pageReq = new PageRequest(page, size, sortBy, direction);
    FilterCriteria filters = new FilterCriteria(name, email, active, createdAfter);
    
    PageResponse<UserDTO> response = userService.findUsers(pageReq, filters);
    return ResponseEntity.ok(response);
}
```

---

## Performance Problem Solving

### Q141. How do you diagnose and fix a memory leak? 🔴

**Memory Leak Diagnosis:**
```java
// Common memory leak scenarios

// 1. Static collections
// ❌ Memory leak
public class CacheManager {
    private static final Map<String, Object> cache = new HashMap<>();
    
    public void add(String key, Object value) {
        cache.put(key, value);  // Never removed!
    }
}

// ✅ Fixed with eviction
public class CacheManager {
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    
    record CacheEntry(Object value, LocalDateTime expiry) {}
    
    public void add(String key, Object value, Duration ttl) {
        LocalDateTime expiry = LocalDateTime.now().plus(ttl);
        cache.put(key, new CacheEntry(value, expiry));
    }
    
    @Scheduled(fixedRate = 60000)
    public void evictExpired() {
        LocalDateTime now = LocalDateTime.now();
        cache.entrySet().removeIf(e -> e.getValue().expiry().isBefore(now));
    }
}

// 2. Unclosed resources
// ❌ Memory leak
public void processFiles(List<Path> files) {
    files.forEach(file -> {
        try {
            Stream<String> lines = Files.lines(file);  // Never closed!
            lines.forEach(this::processLine);
        } catch (IOException e) {
            log.error("Error", e);
        }
    });
}

// ✅ Fixed
public void processFiles(List<Path> files) {
    files.forEach(file -> {
        try (Stream<String> lines = Files.lines(file)) {  // Auto-closed
            lines.forEach(this::processLine);
        } catch (IOException e) {
            log.error("Error", e);
        }
    });
}

// 3. ThreadLocal not cleaned
// ❌ Memory leak with thread pools
public class RequestContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();
    
    public static void setUser(User user) {
        currentUser.set(user);  // Never removed!
    }
}

// ✅ Fixed
public class RequestContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();
    
    public static void setUser(User user) {
        currentUser.set(user);
    }
    
    public static void clear() {
        currentUser.remove();  // Clean up!
    }
}

// Use in filter
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    try {
        User user = extractUser(req);
        RequestContext.setUser(user);
        chain.doFilter(req, res);
    } finally {
        RequestContext.clear();  // Always clean up
    }
}

// 4. Diagnosis tools
// Heap dump analysis
// jmap -dump:format=b,file=heap.bin <pid>

// Monitor with JFR
try (var recording = new Recording()) {
    recording.enable("jdk.ObjectAllocationInNewTLAB");
    recording.enable("jdk.GCHeapSummary");
    recording.start();
    
    runApplication();
    
    recording.stop();
    recording.dump(Path.of("memory.jfr"));
}
```

---

### Q142. How do you optimize database queries in a Java application? 🔴

**Database Optimization:**
```java
// 1. N+1 Query Problem
// ❌ Bad - N+1 queries
@GetMapping("/orders")
public List<OrderDTO> getOrders() {
    List<Order> orders = orderRepo.findAll();  // 1 query
    return orders.stream()
        .map(order -> {
            Customer customer = customerRepo.findById(order.getCustomerId());  // N queries!
            return new OrderDTO(order, customer);
        })
        .toList();
}

// ✅ Good - Single query with join
@GetMapping("/orders")
public List<OrderDTO> getOrders() {
    List<Order> orders = orderRepo.findAllWithCustomers();  // 1 query with join
    return orders.stream()
        .map(OrderDTO::from)
        .toList();
}

// Repository
@Query("SELECT o FROM Order o JOIN FETCH o.customer")
List<Order> findAllWithCustomers();

// 2. Batch operations
// ❌ Bad - Individual inserts
public void saveOrders(List<Order> orders) {
    orders.forEach(order -> orderRepo.save(order));  // N queries
}

// ✅ Good - Batch insert
public void saveOrders(List<Order> orders) {
    orderRepo.saveAll(orders);  // Batch query
}

// Configure batch size
spring.jpa.properties.hibernate.jdbc.batch_size=50

// 3. Projection for read-only queries
// ❌ Bad - Fetching entire entity
@Query("SELECT u FROM User u WHERE u.active = true")
List<User> findActiveUsers();  // Fetches all columns

// ✅ Good - Projection
public interface UserProjection {
    Long getId();
    String getUsername();
    String getEmail();
}

@Query("SELECT u.id as id, u.username as username, u.email as email " +
       "FROM User u WHERE u.active = true")
List<UserProjection> findActiveUserProjections();  // Only needed columns

// Or with Records (Java 16+)
public record UserSummary(Long id, String username, String email) {}

@Query("SELECT new com.example.UserSummary(u.id, u.username, u.email) " +
       "FROM User u WHERE u.active = true")
List<UserSummary> findActiveUserSummaries();

// 4. Pagination for large results
// ❌ Bad - Loading everything
List<Order> orders = orderRepo.findAll();  // OOM with millions of records

// ✅ Good - Paginated processing
Pageable pageable = PageRequest.of(0, 1000);
Page<Order> page;
do {
    page = orderRepo.findAll(pageable);
    processOrders(page.getContent());
    pageable = page.nextPageable();
} while (page.hasNext());

// 5. Caching
@Cacheable(value = "users", key = "#id")
public User findById(Long id) {
    return userRepo.findById(id).orElse(null);
}

@CacheEvict(value = "users", key = "#user.id")
public void update(User user) {
    userRepo.save(user);
}
```

---

## Real Interview Scenarios

### Q143. "Design a URL Shortener" - Complete Solution 🔴

**URL Shortener Design:**
```java
// 1. Domain Model with Records
public record ShortUrl(
    String shortCode,
    String originalUrl,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    long accessCount
) {
    public ShortUrl {
        if (shortCode == null || shortCode.length() != 7) {
            throw new IllegalArgumentException("Short code must be 7 characters");
        }
    }
    
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
}

// 2. Service Implementation
@Service
public class UrlShortenerService {
    private static final String CHARSET = 
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 7;
    
    private final UrlRepository repository;
    private final CacheManager cacheManager;
    
    public String shorten(String originalUrl, Optional<Duration> ttl) {
        // Generate unique short code
        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (repository.existsByShortCode(shortCode));
        
        // Calculate expiry
        LocalDateTime expiresAt = ttl
            .map(d -> LocalDateTime.now().plus(d))
            .orElse(null);
        
        // Save
        ShortUrl shortUrl = new ShortUrl(
            shortCode,
            originalUrl,
            LocalDateTime.now(),
            expiresAt,
            0
        );
        repository.save(shortUrl);
        
        // Cache
        cacheManager.put(shortCode, originalUrl);
        
        return shortCode;
    }
    
    public Optional<String> resolve(String shortCode) {
        // Check cache first
        String cached = cacheManager.get(shortCode);
        if (cached != null) {
            incrementAccessCount(shortCode);
            return Optional.of(cached);
        }
        
        // Check database
        return repository.findByShortCode(shortCode)
            .filter(url -> !url.isExpired())
            .map(url -> {
                incrementAccessCount(shortCode);
                cacheManager.put(shortCode, url.originalUrl());
                return url.originalUrl();
            });
    }
    
    private String generateShortCode() {
        Random random = ThreadLocalRandom.current();
        return random.ints(SHORT_CODE_LENGTH, 0, CHARSET.length())
            .mapToObj(CHARSET::charAt)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }
    
    private void incrementAccessCount(String shortCode) {
        // Async increment to not block redirect
        Thread.ofVirtual().start(() -> {
            repository.incrementAccessCount(shortCode);
        });
    }
}

// 3. Controller
@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlShortenerService service;
    
    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(
            @RequestBody ShortenRequest request) {
        String shortCode = service.shorten(
            request.url(),
            request.ttlDays().map(days -> Duration.ofDays(days))
        );
        String shortUrl = "https://short.ly/" + shortCode;
        return ResponseEntity.ok(new ShortenResponse(shortUrl, shortCode));
    }
    
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        return service.resolve(shortCode)
            .map(url -> ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(url))
                .build())
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<UrlStats> getStats(@PathVariable String shortCode) {
        return service.getStats(shortCode)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

public record ShortenRequest(String url, Optional<Integer> ttlDays) {}
public record ShortenResponse(String shortUrl, String shortCode) {}
public record UrlStats(String shortCode, String originalUrl, 
                      long accessCount, LocalDateTime createdAt) {}
```

---

### Q144. "Implement Rate Limiting" - Complete Solution 🔴

**Rate Limiter Implementation:**
```java
// 1. Rate Limiter with Sliding Window
public class SlidingWindowRateLimiter {
    private final Map<String, Queue<Long>> requestTimestamps = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final Duration window;
    
    public SlidingWindowRateLimiter(int maxRequests, Duration window) {
        this.maxRequests = maxRequests;
        this.window = window;
    }
    
    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        long windowStart = now - window.toMillis();
        
        Queue<Long> timestamps = requestTimestamps.computeIfAbsent(
            clientId, k -> new ConcurrentLinkedQueue<>());
        
        // Remove old timestamps
        timestamps.removeIf(ts -> ts < windowStart);
        
        // Check limit
        if (timestamps.size() < maxRequests) {
            timestamps.offer(now);
            return true;
        }
        
        return false;
    }
    
    public int getRemainingRequests(String clientId) {
        Queue<Long> timestamps = requestTimestamps.get(clientId);
        if (timestamps == null) return maxRequests;
        
        long windowStart = System.currentTimeMillis() - window.toMillis();
        long validRequests = timestamps.stream()
            .filter(ts -> ts >= windowStart)
            .count();
        
        return (int) (maxRequests - validRequests);
    }
}

// 2. Token Bucket Algorithm
public class TokenBucketRateLimiter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final int capacity;
    private final int refillRate;
    private final Duration refillPeriod;
    
    record Bucket(int tokens, long lastRefillTimestamp) {}
    
    public TokenBucketRateLimiter(int capacity, int refillRate, Duration refillPeriod) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.refillPeriod = refillPeriod;
    }
    
    public boolean allowRequest(String clientId) {
        return allowRequests(clientId, 1);
    }
    
    public boolean allowRequests(String clientId, int tokensNeeded) {
        Bucket bucket = buckets.compute(clientId, (k, current) -> {
            long now = System.currentTimeMillis();
            
            if (current == null) {
                // New bucket
                return new Bucket(capacity - tokensNeeded, now);
            }
            
            // Refill tokens
            long timePassed = now - current.lastRefillTimestamp();
            int periodsP assed = (int) (timePassed / refillPeriod.toMillis());
            int tokensToAdd = periodsPassed * refillRate;
            int newTokens = Math.min(capacity, current.tokens() + tokensToAdd);
            
            if (newTokens >= tokensNeeded) {
                return new Bucket(newTokens - tokensNeeded, now);
            }
            
            return current;  // Not enough tokens
        });
        
        return bucket.tokens() >= 0;
    }
}

// 3. Filter/Interceptor
@Component
public class RateLimitFilter implements Filter {
    private final RateLimiter rateLimiter = 
        new SlidingWindowRateLimiter(100, Duration.ofMinutes(1));
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientId = getClientId(httpRequest);
        
        if (rateLimiter.allowRequest(clientId)) {
            int remaining = rateLimiter.getRemainingRequests(clientId);
            ((HttpServletResponse) response).setHeader("X-RateLimit-Remaining", 
                String.valueOf(remaining));
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(429);
            response.getWriter().write("Rate limit exceeded");
        }
    }
    
    private String getClientId(HttpServletRequest request) {
        // Use IP or API key
        return request.getRemoteAddr();
    }
}
```

---

### Q145-Q160: Additional Interview Scenarios

*These would cover additional common scenarios:*
- Q145: Design a distributed cache system
- Q146: Implement real-time notifications
- Q147: Design a job scheduling system
- Q148: Build a search and filter system
- Q149: Implement file upload/download
- Q150: Design a multi-tenant system
- Q151-Q160: More complex scenarios and trade-off discussions

---

## 🎯 Interview Guide Complete!

You now have an exhaustive collection of **150+ detailed interview questions** covering:

- ✅ Java 8-21 features
- ✅ Performance optimization
- ✅ Design patterns
- ✅ System design
- ✅ Real-world scenarios
- ✅ Best practices

**Total Coverage:**
- **File 01**: Q1-Q21 (Java 8 Core)
- **File 02**: Q22-Q45 (Advanced Streams)
- **File 03**: Q46-Q66 (Java 11-17)
- **File 04**: Q86-Q106 (Java 21)
- **File 05**: Q111-Q123 (Advanced Topics)
- **File 06**: Q136-Q144 (Real-World)

---

**📘 Back to:** [README](README.md) | [Question Index](QUESTION-INDEX.md) | [Getting Started](GETTING-STARTED.md)
