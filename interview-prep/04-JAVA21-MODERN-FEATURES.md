# Java 21 Modern Features - Interview Questions 🚀

> **Part 4 of Comprehensive Java Interview Guide**
>
> Covers Java 21 LTS features: Virtual Threads, Sequenced Collections, Pattern Matching for Switch, and other modern features.

---

## 📑 Table of Contents
- Virtual Threads (Q86-Q92)
- Sequenced Collections (Q93-Q98)
- Pattern Matching for Switch (Q99-Q104)
- Other Java 21+ Features (Q105-Q110)

---

## Virtual Threads

### Q86. What are Virtual Threads and why were they introduced? 🟢

**Short Answer:**
Virtual threads are lightweight threads managed by the JVM (not the OS) that enable simple, scalable concurrency. They were introduced in Java 21 to make concurrent programming easier and more efficient for high-throughput applications.

**Detailed Explanation:**
```java
// Traditional Platform Thread
Thread platformThread = new Thread(() -> {
    System.out.println("Platform thread: " + Thread.currentThread());
});
platformThread.start();

// Virtual Thread (Java 21+)
Thread virtualThread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual thread: " + Thread.currentThread());
});

// Check if virtual
System.out.println("Is virtual? " + virtualThread.isVirtual());  // true
System.out.println("Is virtual? " + platformThread.isVirtual()); // false
```

**Why Virtual Threads?**
```java
// Problem: Platform threads are expensive
// Can't create millions of platform threads
for (int i = 0; i < 1_000_000; i++) {
    new Thread(() -> {
        // Do work
    }).start();  // OutOfMemoryError!
}

// Solution: Virtual threads are cheap
// Can easily create millions of virtual threads
for (int i = 0; i < 1_000_000; i++) {
    Thread.ofVirtual().start(() -> {
        // Do work
    });  // Works fine!
}
```

**Key Characteristics:**
```java
// 1. Lightweight - ~1KB per thread vs ~1MB for platform threads
// 2. Cheap to create - Can create millions
// 3. Cheap to block - Blocking doesn't waste OS threads
// 4. Scheduled by JVM - Not by OS
// 5. No thread pools needed - Create on demand

// Virtual thread with name
Thread vt = Thread.ofVirtual()
    .name("worker-", 0)
    .start(() -> {
        System.out.println("Named virtual thread");
    });

// Virtual thread factory
ThreadFactory factory = Thread.ofVirtual().factory();
Thread t1 = factory.newThread(() -> task1());
Thread t2 = factory.newThread(() -> task2());
```

**Platform Thread vs Virtual Thread:**
```java
// Platform Thread
// - Managed by OS
// - ~1MB stack memory
// - ~thousands max
// - Expensive to create/destroy
// - Context switching expensive
// - Blocking wastes OS thread

// Virtual Thread  
// - Managed by JVM
// - ~1KB stack memory
// - Millions possible
// - Cheap to create/destroy
// - Context switching cheap
// - Blocking doesn't waste carrier thread
```

---

### Q87. How do Virtual Threads work internally? 🔴

**Short Answer:**
Virtual threads run on a pool of carrier threads (platform threads). When a virtual thread blocks, it's unmounted from its carrier thread, allowing the carrier to run other virtual threads.

**Detailed Explanation:**

**1. Carrier Threads:**
```java
// Virtual threads run on carrier threads
// Carrier threads are platform threads from ForkJoinPool

// Check carrier thread
Thread.ofVirtual().start(() -> {
    System.out.println("Virtual thread: " + Thread.currentThread());
    System.out.println("Running on carrier: " + 
        Thread.currentThread().toString());
});

// Output might show:
// VirtualThread[#21]/runnable@ForkJoinPool-1-worker-1
```

**2. Mounting and Unmounting:**
```java
// When virtual thread runs: MOUNTED to carrier thread
// When virtual thread blocks: UNMOUNTED from carrier thread

Thread.ofVirtual().start(() -> {
    System.out.println("Running on: " + Thread.currentThread());
    // Virtual thread is MOUNTED
    
    try {
        Thread.sleep(1000);  // Blocking operation
        // Virtual thread is UNMOUNTED during sleep
        // Carrier thread can run other virtual threads
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    // After sleep, virtual thread is MOUNTED again
    System.out.println("Running on: " + Thread.currentThread());
    // Might be different carrier thread!
});
```

**3. Thread Pinning (What to Avoid):**
```java
// ❌ BAD: synchronized blocks can PIN carrier thread
Thread.ofVirtual().start(() -> {
    synchronized (lock) {  // PINS carrier thread!
        // Virtual thread cannot be unmounted
        // Carrier thread blocked while this runs
        Thread.sleep(1000);  // Wastes carrier thread
    }
});

// ✅ GOOD: Use ReentrantLock instead
ReentrantLock lock = new ReentrantLock();
Thread.ofVirtual().start(() -> {
    lock.lock();
    try {
        // Virtual thread CAN be unmounted
        Thread.sleep(1000);  // Carrier thread free
    } finally {
        lock.unlock();
    }
});

// Other pinning scenarios:
// - Native method calls
// - File I/O operations (in some cases)
// These are being improved in future Java versions
```

**4. Scheduler:**
```java
// Virtual threads are scheduled by ForkJoinPool
// Default pool size = number of CPU cores

// Check ForkJoinPool
ForkJoinPool pool = ForkJoinPool.commonPool();
System.out.println("Parallelism: " + pool.getParallelism());

// Virtual threads automatically balanced across carrier threads
// No manual thread pool management needed!
```

**5. Stack Management:**
```java
// Virtual thread stacks are stored in heap memory
// Grows/shrinks as needed
// Platform thread stacks are fixed size in native memory

// This enables millions of virtual threads:
List<Thread> threads = new ArrayList<>();
for (int i = 0; i < 100_000; i++) {
    Thread vt = Thread.ofVirtual().start(() -> {
        try {
            Thread.sleep(10000);  // All blocked at once
        } catch (InterruptedException e) {}
    });
    threads.add(vt);
}
// Only uses CPU cores worth of carrier threads!
```

---

### Q88. When should you use Virtual Threads vs Platform Threads? 🟡

**Use Virtual Threads For:**

**1. High-Throughput Servers:**
```java
// Server handling many concurrent requests
class WebServer {
    public void start() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (true) {
                Socket client = serverSocket.accept();
                executor.submit(() -> handleRequest(client));
                // Can handle millions of concurrent connections!
            }
        }
    }
    
    private void handleRequest(Socket client) {
        // IO-bound: reading, database calls, external APIs
        String request = readRequest(client);
        String data = database.query(request);  // Blocks, but OK
        String response = processData(data);
        writeResponse(client, response);
    }
}
```

**2. IO-Bound Operations:**
```java
// Reading files
Thread.ofVirtual().start(() -> {
    String content = Files.readString(Path.of("large-file.txt"));
    // Blocking IO is fine with virtual threads
    processContent(content);
});

// Database queries
Thread.ofVirtual().start(() -> {
    List<User> users = database.query("SELECT * FROM users");
    // Blocking database call is fine
    processUsers(users);
});

// HTTP requests
Thread.ofVirtual().start(() -> {
    HttpResponse<String> response = httpClient.send(request, 
        HttpResponse.BodyHandlers.ofString());
    // Blocking HTTP call is fine
    processResponse(response);
});
```

**3. Structured Concurrency:**
```java
// Multiple concurrent tasks with structured lifecycle
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Supplier<User> userTask = scope.fork(() -> fetchUser(userId));
    Supplier<List<Order>> ordersTask = scope.fork(() -> fetchOrders(userId));
    Supplier<Profile> profileTask = scope.fork(() -> fetchProfile(userId));
    
    scope.join();           // Wait for all
    scope.throwIfFailed();  // Propagate failures
    
    // All tasks succeeded
    User user = userTask.get();
    List<Order> orders = ordersTask.get();
    Profile profile = profileTask.get();
    
    return new UserData(user, orders, profile);
}
```

**Use Platform Threads For:**

**1. CPU-Intensive Operations:**
```java
// Computational work with no blocking
ExecutorService cpuPool = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
);

cpuPool.submit(() -> {
    // CPU-intensive: calculations, algorithms, processing
    BigInteger result = calculatePrime(1_000_000);
    // No blocking - constantly using CPU
    return result;
});

// Virtual threads add overhead here with no benefit
```

**2. Thread-Local Heavy Code:**
```java
// Code that heavily uses ThreadLocal
// Virtual threads can have many ThreadLocals in memory
ThreadLocal<DatabaseConnection> connectionThreadLocal = new ThreadLocal<>();

// With platform threads: fixed number of ThreadLocals
ExecutorService platformPool = Executors.newFixedThreadPool(10);
// Only 10 ThreadLocal instances

// With virtual threads: could be millions of ThreadLocals
// Memory usage can be significant
```

**3. Native Code / JNI:**
```java
// Native method calls can pin carrier threads
Thread platformThread = new Thread(() -> {
    nativeMethod();  // JNI call
    // Use platform thread to avoid pinning carrier
});
```

**Comparison Table:**

| Scenario | Virtual Thread | Platform Thread | Why |
|----------|---------------|-----------------|-----|
| Web server (1M requests) | ✅ Perfect | ❌ Can't scale | Virtual threads designed for this |
| Database queries | ✅ Great | ⚠️ Limited | Blocking IO is fine with virtual |
| HTTP client calls | ✅ Great | ⚠️ Limited | Blocking network IO |
| File operations | ✅ Good | ⚠️ OK | IO-bound operations |
| CPU-intensive math | ⚠️ OK | ✅ Better | No blocking, overhead not worth it |
| Image processing | ⚠️ OK | ✅ Better | CPU-bound work |
| Parallel streams | N/A | ✅ Yes | Uses ForkJoinPool optimally |
| Cryptography | ⚠️ OK | ✅ Better | CPU-intensive |
| Thread pools | ❌ Don't use | ✅ Traditional | Virtual threads = no pools needed |

---

### Q89. How do you use Virtual Threads with Executors? 🟢

**Short Answer:**
Use `Executors.newVirtualThreadPerTaskExecutor()` which creates a new virtual thread for each task without pooling.

**Detailed Explanation:**
```java
// 1. Virtual Thread Executor (Per-Task)
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

executor.submit(() -> {
    System.out.println("Task 1 in: " + Thread.currentThread());
});

executor.submit(() -> {
    System.out.println("Task 2 in: " + Thread.currentThread());
});

// Each submit() creates a NEW virtual thread
// No thread pooling/reuse!

executor.close();  // Java 19+: Executors are AutoCloseable
```

**2. Try-With-Resources Pattern:**
```java
// Automatically closes and waits for all tasks
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> task1());
    executor.submit(() -> task2());
    executor.submit(() -> task3());
    // Waits for all tasks before closing
}
// All tasks completed here
```

**3. Processing Multiple Items:**
```java
List<String> urls = List.of(
    "https://api.example.com/user/1",
    "https://api.example.com/user/2",
    "https://api.example.com/user/3"
    // ... thousands more
);

try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<String>> futures = urls.stream()
        .map(url -> executor.submit(() -> fetchUrl(url)))
        .toList();
    
    // Collect results
    List<String> results = futures.stream()
        .map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        })
        .toList();
}
```

**4. Comparison with Traditional Executors:**
```java
// Traditional: Fixed thread pool
ExecutorService traditional = Executors.newFixedThreadPool(10);
// Only 10 threads - tasks queue up

for (int i = 0; i < 1000; i++) {
    traditional.submit(() -> {
        Thread.sleep(1000);  // 10 at a time
    });
}
// Takes 100 seconds (1000 tasks / 10 threads)

// Virtual threads: Per-task
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 1000; i++) {
        executor.submit(() -> {
            Thread.sleep(1000);  // All at once!
        });
    }
}
// Takes 1 second (all 1000 tasks concurrently)
```

**5. Real-World Example: Batch Processing:**
```java
public class BatchProcessor {
    public List<Result> processBatch(List<Task> tasks) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Result>> futures = tasks.stream()
                .map(task -> executor.submit(() -> processTask(task)))
                .toList();
            
            return futures.stream()
                .map(this::getFutureResult)
                .toList();
        }
    }
    
    private Result processTask(Task task) {
        // IO-bound: database, API calls, file operations
        Data data = database.fetch(task.getId());
        ExternalData external = api.call(task.getEndpoint());
        return combineResults(data, external);
    }
    
    private Result getFutureResult(Future<Result> future) {
        try {
            return future.get();
        } catch (Exception e) {
            return Result.error(e);
        }
    }
}
```

**6. Custom Thread Factory:**
```java
ThreadFactory factory = Thread.ofVirtual()
    .name("worker-", 1)  // worker-1, worker-2, etc.
    .factory();

ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);

executor.submit(() -> {
    System.out.println(Thread.currentThread().getName());
    // Output: worker-1
});
```

**7. Structured Concurrency (Preview):**
```java
// Better than ExecutorService for structured tasks
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    // Fork tasks
    Future<String> user = scope.fork(() -> fetchUser());
    Future<List<Order>> orders = scope.fork(() -> fetchOrders());
    
    // Join all
    scope.join();
    scope.throwIfFailed();
    
    // Use results
    processData(user.resultNow(), orders.resultNow());
}
```

---

### Q90. What is Structured Concurrency? 🔴

**Short Answer:**
Structured Concurrency (preview in Java 21) treats multiple concurrent tasks as a single unit of work with a clear lifecycle, making concurrent code more reliable and maintainable.

**Detailed Explanation:**

**1. The Problem with Unstructured Concurrency:**
```java
// Unstructured: Tasks can outlive their scope
ExecutorService executor = Executors.newCachedThreadPool();

try {
    Future<String> future1 = executor.submit(() -> task1());
    Future<String> future2 = executor.submit(() -> task2());
    
    String result1 = future1.get();
    // If exception here, task2 keeps running!
    String result2 = future2.get();
    
    return combine(result1, result2);
} finally {
    executor.shutdown();  // Tasks might still be running
}

// Problems:
// - Task2 continues even if task1 fails
// - Resource leaks if exception thrown
// - No clear parent-child relationship
// - Hard to cancel all tasks
```

**2. Structured Concurrency Solution:**
```java
// Structured: All tasks bound to scope
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Supplier<String> task1 = scope.fork(() -> fetchUser());
    Supplier<String> task2 = scope.fork(() -> fetchOrders());
    
    scope.join();           // Wait for all tasks
    scope.throwIfFailed();  // Propagate any exception
    
    // Both succeeded
    return combine(task1.get(), task2.get());
}
// Scope closes: all tasks guaranteed to complete or be cancelled

// Benefits:
// - If one fails, all are cancelled
// - No resource leaks
// - Clear lifecycle
// - Exception handling built-in
```

**3. ShutdownOnFailure - Short-Circuit on Error:**
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Supplier<User> user = scope.fork(() -> fetchUser(userId));
    Supplier<Profile> profile = scope.fork(() -> fetchProfile(userId));
    Supplier<Orders> orders = scope.fork(() -> fetchOrders(userId));
    
    scope.join();  // Wait for all or first failure
    scope.throwIfFailed();  // Throws if any task failed
    
    // All succeeded - safe to get results
    return new UserData(
        user.get(),
        profile.get(),
        orders.get()
    );
}

// If fetchUser() fails:
// 1. Other tasks are immediately cancelled
// 2. scope.join() completes quickly
// 3. scope.throwIfFailed() throws the exception
// 4. Resources cleaned up automatically
```

**4. ShutdownOnSuccess - First Success Wins:**
```java
// Race multiple services, use first successful result
try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
    scope.fork(() -> fetchFromService1());
    scope.fork(() -> fetchFromService2());
    scope.fork(() -> fetchFromService3());
    
    scope.join();  // Wait for first success
    
    String result = scope.result();  // Get winning result
    // Other tasks automatically cancelled
    return result;
}

// Use case: Multiple API endpoints for redundancy
// Whichever responds first, wins
```

**5. Custom Scope Policies:**
```java
// Custom: Collect all results, even with some failures
class CollectAll<T> extends StructuredTaskScope<T> {
    private final List<T> results = new ArrayList<>();
    private final List<Throwable> exceptions = new ArrayList<>();
    
    @Override
    protected void handleComplete(Subtask<? extends T> subtask) {
        if (subtask.state() == Subtask.State.SUCCESS) {
            results.add(subtask.get());
        } else {
            exceptions.add(subtask.exception());
        }
    }
    
    public List<T> results() { return List.copyOf(results); }
    public List<Throwable> exceptions() { return List.copyOf(exceptions); }
}

// Usage
try (var scope = new CollectAll<String>()) {
    scope.fork(() -> task1());
    scope.fork(() -> task2());
    scope.fork(() -> task3());
    
    scope.join();
    
    List<String> successful = scope.results();
    List<Throwable> failed = scope.exceptions();
    
    System.out.println("Succeeded: " + successful.size());
    System.out.println("Failed: " + failed.size());
}
```

**6. Real-World Example: Aggregating Data:**
```java
public record UserDashboard(
    User user,
    List<Order> recentOrders,
    Statistics stats,
    List<Notification> notifications
) {}

public UserDashboard loadDashboard(Long userId) {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        Supplier<User> userTask = 
            scope.fork(() -> userService.getUser(userId));
        
        Supplier<List<Order>> ordersTask = 
            scope.fork(() -> orderService.getRecentOrders(userId));
        
        Supplier<Statistics> statsTask = 
            scope.fork(() -> statsService.getStats(userId));
        
        Supplier<List<Notification>> notificationsTask = 
            scope.fork(() -> notificationService.getUnread(userId));
        
        scope.join();
        scope.throwIfFailed();
        
        return new UserDashboard(
            userTask.get(),
            ordersTask.get(),
            statsTask.get(),
            notificationsTask.get()
        );
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted", e);
    } catch (ExecutionException e) {
        throw new RuntimeException("Failed to load dashboard", e);
    }
}

// All services called concurrently
// If any fails, all are cancelled
// Results combined only if all succeed
```

**7. Comparison:**
```java
// Traditional ExecutorService
ExecutorService executor = Executors.newCachedThreadPool();
try {
    Future<A> fa = executor.submit(() -> taskA());
    Future<B> fb = executor.submit(() -> taskB());
    A a = fa.get();  // Blocks
    B b = fb.get();  // Blocks
    return combine(a, b);
} finally {
    executor.shutdown();  // Hope tasks finish
}

// Structured Concurrency
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var ta = scope.fork(() -> taskA());
    var tb = scope.fork(() -> taskB());
    scope.join();
    scope.throwIfFailed();
    return combine(ta.get(), tb.get());
}
// Tasks guaranteed to complete or be cancelled
```

---

### Q91. What are common pitfalls with Virtual Threads? 🟡

**1. Using Thread Pools:**
```java
// ❌ WRONG: Pooling virtual threads
ExecutorService pool = Executors.newFixedThreadPool(100, 
    Thread.ofVirtual().factory());
// Defeats the purpose! Virtual threads are cheap, don't pool them

// ✅ RIGHT: Create on demand
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Creates new virtual thread for each task
}
```

**2. Synchronized Blocks (Pinning):**
```java
// ❌ WRONG: synchronized pins carrier thread
Object lock = new Object();
Thread.ofVirtual().start(() -> {
    synchronized (lock) {  // PINS!
        Thread.sleep(1000);  // Wastes carrier thread
    }
});

// ✅ RIGHT: Use ReentrantLock
ReentrantLock lock = new ReentrantLock();
Thread.ofVirtual().start(() -> {
    lock.lock();
    try {
        Thread.sleep(1000);  // Can unmount
    } finally {
        lock.unlock();
    }
});
```

**3. CPU-Intensive Work:**
```java
// ❌ WRONG: CPU-intensive work in virtual threads
Thread.ofVirtual().start(() -> {
    // Complex calculation, no blocking
    BigInteger prime = calculateLargePrime();
    // No benefit, just overhead
});

// ✅ RIGHT: Use platform thread pool
ExecutorService cpuPool = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
);
cpuPool.submit(() -> calculateLargePrime());
```

**4. ThreadLocal Overuse:**
```java
// ⚠️ CAREFUL: ThreadLocals in virtual threads
ThreadLocal<ExpensiveObject> threadLocal = ThreadLocal.withInitial(
    ExpensiveObject::new
);

// With 1 million virtual threads = 1 million ExpensiveObjects!
for (int i = 0; i < 1_000_000; i++) {
    Thread.ofVirtual().start(() -> {
        ExpensiveObject obj = threadLocal.get();  // Memory intensive
        // use obj
    });
}

// ✅ BETTER: Use method parameters or ScopedValue (Java 20+)
```

**5. Blocking File I/O (Current Limitation):**
```java
// ⚠️ Currently pins carrier thread (being improved)
Thread.ofVirtual().start(() -> {
    FileInputStream fis = new FileInputStream("file.txt");
    // File I/O currently pins in some cases
});

// Workaround: Use NIO for non-blocking I/O
Thread.ofVirtual().start(() -> {
    Path path = Path.of("file.txt");
    String content = Files.readString(path);  // Better
});
```

**6. Expecting Thread Identity:**
```java
// ❌ WRONG: Relying on thread identity
Map<Thread, Data> threadData = new ConcurrentHashMap<>();

Thread.ofVirtual().start(() -> {
    threadData.put(Thread.currentThread(), data);
    // Thread object might be garbage collected!
});

// ✅ RIGHT: Use proper data structures
Map<TaskId, Data> taskData = new ConcurrentHashMap<>();
```

**7. Not Using Try-With-Resources:**
```java
// ❌ WRONG: Forgetting to close executor
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
executor.submit(() -> task());
// Might not complete!

// ✅ RIGHT: Use try-with-resources
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> task());
}  // Guaranteed to complete
```

---

### Q92. How do you monitor and debug Virtual Threads? 🔴

**1. JFR (Java Flight Recorder):**
```java
// Enable JFR events for virtual threads
// -XX:StartFlightRecording=filename=recording.jfr

// Events captured:
// - Virtual thread creation
// - Mounting/unmounting
// - Pinning events
// - Carrier thread usage

// Analyze with JDK Mission Control
```

**2. Thread Dumps:**
```java
// Get thread dump
jstack <pid>

// Virtual threads appear as:
// "VirtualThread-1234" #1234 virtual
//   java.base@21/java.lang.VirtualThread.park(...)

// Pinned threads marked with "pinned"
```

**3. Programmatic Monitoring:**
```java
// Monitor virtual thread creation
Thread.Builder.OfVirtual builder = Thread.ofVirtual()
    .name("worker-", 0);

AtomicInteger counter = new AtomicInteger();

for (int i = 0; i < 1000; i++) {
    builder.start(() -> {
        counter.incrementAndGet();
        // task
        counter.decrementAndGet();
    });
}

System.out.println("Active virtual threads: " + counter.get());
```

**4. Debugging Pinning:**
```java
// Enable pinning detection
// -Djdk.tracePinnedThreads=full

// Output shows where pinning occurs:
// Thread[#123,worker-1,5,CarrierThreads]
//   at java.base/java.lang.VirtualThread.parkOnCarrierThread(...)
//   at MyClass.synchronizedMethod(MyClass.java:42)  // Shows pinning location
```

**5. Custom Monitoring:**
```java
public class VirtualThreadMonitor {
    private final AtomicInteger created = new AtomicInteger();
    private final AtomicInteger active = new AtomicInteger();
    private final AtomicInteger completed = new AtomicInteger();
    
    public Thread createMonitored(Runnable task) {
        created.incrementAndGet();
        return Thread.ofVirtual().start(() -> {
            active.incrementAndGet();
            try {
                task.run();
            } finally {
                active.decrementAndGet();
                completed.incrementAndGet();
            }
        });
    }
    
    public void printStats() {
        System.out.println("Created: " + created.get());
        System.out.println("Active: " + active.get());
        System.out.println("Completed: " + completed.get());
    }
}
```

---

## Sequenced Collections

### Q93. What are Sequenced Collections and why were they added? 🟢

**Short Answer:**
Sequenced Collections (Java 21) provide a uniform API for collections with a defined encounter order, adding methods like `getFirst()`, `getLast()`, `addFirst()`, `addLast()`, and `reversed()`.

**Detailed Explanation:**

**The Problem:**
```java
// Before Java 21: Inconsistent APIs

List<String> list = new ArrayList<>();
String first = list.get(0);           // List way
String last = list.get(list.size() - 1);

Deque<String> deque = new ArrayDeque<>();
String first = deque.getFirst();      // Deque way
String last = deque.getLast();

SortedSet<String> set = new TreeSet<>();
String first = set.first();           // SortedSet way
String last = set.last();

// No common interface!
```

**The Solution:**
```java
// Java 21: Unified interface

// SequencedCollection interface
public interface SequencedCollection<E> extends Collection<E> {
    SequencedCollection<E> reversed();
    
    void addFirst(E e);
    void addLast(E e);
    
    E getFirst();
    E getLast();
    
    E removeFirst();
    E removeLast();
}

// Now all have common API
List<String> list = new ArrayList<>();
list.addFirst("first");
list.addLast("last");
String first = list.getFirst();
String last = list.getLast();
```

**New Interface Hierarchy:**
```java
Collection<E>
└── SequencedCollection<E>
    ├── List<E>
    └── SequencedSet<E>
        └── SortedSet<E>

Map<K,V>
└── SequencedMap<K,V>
    └── SortedMap<K,V>
```

---

### Q94. How do you use Sequenced Collections? 🟢

**1. List Operations:**
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));

// Access first/last
String first = list.getFirst();  // "a"
String last = list.getLast();    // "c"

// Add first/last
list.addFirst("z");  // ["z", "a", "b", "c"]
list.addLast("d");   // ["z", "a", "b", "c", "d"]

// Remove first/last
list.removeFirst();  // Remove "z"
list.removeLast();   // Remove "d"

// Reversed view (not a copy!)
SequencedCollection<String> reversed = list.reversed();
System.out.println(reversed);  // [c, b, a]

// Modifications to reversed affect original
reversed.addFirst("x");  // Adds to end of original
System.out.println(list);  // [a, b, c, x]
```

**2. SequencedSet Operations:**
```java
SequencedSet<Integer> set = new LinkedHashSet<>();
set.add(1);
set.add(2);
set.add(3);

// First/last in insertion order
int first = set.getFirst();  // 1
int last = set.getLast();    // 3

// Add at ends
set.addFirst(0);  // [0, 1, 2, 3]
set.addLast(4);   // [0, 1, 2, 3, 4]

// Reversed iteration
for (Integer num : set.reversed()) {
    System.out.println(num);  // 4, 3, 2, 1, 0
}
```

**3. SequencedMap Operations:**
```java
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.put("a", 1);
map.put("b", 2);
map.put("c", 3);

// First/last entries
Map.Entry<String, Integer> first = map.firstEntry();  // a=1
Map.Entry<String, Integer> last = map.lastEntry();    // c=3

// Poll (remove and return)
Map.Entry<String, Integer> removed = map.pollFirstEntry();  // Removes a=1
removed = map.pollLastEntry();  // Removes c=3

// Add at ends
map.putFirst("z", 26);  // Adds at beginning
map.putLast("d", 4);    // Adds at end

// Reversed view
SequencedMap<String, Integer> reversed = map.reversed();
reversed.forEach((k, v) -> System.out.println(k + "=" + v));
// Prints in reverse order
```

**4. Practical Examples:**
```java
// Example 1: LRU Cache behavior
public class SimpleCache<K, V> {
    private final SequencedMap<K, V> cache = new LinkedHashMap<>();
    private final int maxSize;
    
    public SimpleCache(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public void put(K key, V value) {
        cache.remove(key);  // Remove if exists
        cache.putLast(key, value);  // Add at end (most recent)
        
        if (cache.size() > maxSize) {
            cache.pollFirstEntry();  // Remove oldest
        }
    }
    
    public V get(K key) {
        V value = cache.remove(key);
        if (value != null) {
            cache.putLast(key, value);  // Move to end (most recent)
        }
        return value;
    }
}

// Example 2: Task queue
public class TaskQueue {
    private final SequencedCollection<Task> tasks = new ArrayDeque<>();
    
    public void addUrgent(Task task) {
        tasks.addFirst(task);  // High priority
    }
    
    public void addNormal(Task task) {
        tasks.addLast(task);  // Normal priority
    }
    
    public Task getNext() {
        return tasks.removeFirst();
    }
    
    public Task peekNext() {
        return tasks.getFirst();
    }
}

// Example 3: Reverse iteration
List<String> logs = getRecentLogs();
// Process in reverse chronological order
for (String log : logs.reversed()) {
    processLog(log);
}
```

---

### Q95. What's the difference between reversed() and Collections.reverse()? 🟡

**Short Answer:**
`reversed()` returns a **view** (no copying), while `Collections.reverse()` **modifies** the original list in place.

**Detailed Comparison:**
```java
List<Integer> original = new ArrayList<>(List.of(1, 2, 3, 4, 5));

// 1. reversed() - Returns view
SequencedCollection<Integer> view = original.reversed();
System.out.println(view);  // [5, 4, 3, 2, 1]
System.out.println(original);  // [1, 2, 3, 4, 5] - unchanged

// Modifications to view affect original
view.addFirst(6);  // Adds to end of original
System.out.println(original);  // [1, 2, 3, 4, 5, 6]
System.out.println(view);  // [6, 5, 4, 3, 2, 1]

// 2. Collections.reverse() - Modifies in place
Collections.reverse(original);
System.out.println(original);  // [6, 5, 4, 3, 2, 1] - reversed!

// Key differences:
// reversed() - O(1) time, creates view
// Collections.reverse() - O(n) time, mutates list
```

**When to Use Each:**
```java
// Use reversed() when:
// - You need to iterate in reverse without copying
// - You want a read-only reverse view
// - You need to modify through the view

List<String> list = List.of("a", "b", "c");
for (String s : list.reversed()) {  // O(1), no copy
    System.out.println(s);
}

// Use Collections.reverse() when:
// - You actually want to reverse the list permanently
// - You need in-place reversal for algorithms

List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3));
Collections.reverse(numbers);  // [3, 2, 1]
// numbers is now reversed
```

**Performance:**
```java
List<Integer> large = new ArrayList<>();
for (int i = 0; i < 1_000_000; i++) {
    large.add(i);
}

// reversed() - instant, no copying
long start = System.nanoTime();
SequencedCollection<Integer> view = large.reversed();
long time = System.nanoTime() - start;
System.out.println("reversed(): " + time + "ns");  // ~100ns

// Collections.reverse() - linear time
start = System.nanoTime();
Collections.reverse(large);
time = System.nanoTime() - start;
System.out.println("reverse(): " + time + "ns");  // ~millions ns
```

---

## Pattern Matching for Switch

### Q96. What are the enhancements to switch in Java 21? 🟢

**Key Enhancements:**
```java
// 1. Pattern matching with type patterns
Object obj = "Hello";
String result = switch (obj) {
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    case Double d -> "Double: " + d;
    case null -> "Null value";
    default -> "Unknown type";
};

// 2. Record patterns in switch
record Point(int x, int y) {}

String location = switch (new Point(3, 4)) {
    case Point(0, 0) -> "Origin";
    case Point(int x, 0) -> "On X-axis at " + x;
    case Point(0, int y) -> "On Y-axis at " + y;
    case Point(int x, int y) -> "At (" + x + ", " + y + ")";
};

// 3. Guarded patterns
int classify(Integer num) {
    return switch (num) {
        case null -> 0;
        case Integer i when i > 100 -> 3;
        case Integer i when i > 10 -> 2;
        case Integer i when i > 0 -> 1;
        default -> -1;
    };
}

// 4. Null handling built-in
String handleNull(String s) {
    return switch (s) {
        case null -> "Was null";
        case "" -> "Was empty";
        default -> "Was: " + s;
    };
}
```

---

### Q97. How do you use when clauses effectively? 🟡

**When Clause Patterns:**
```java
// 1. Complex conditions
String categorize(String input) {
    return switch (input) {
        case String s when s.isEmpty() -> "Empty";
        case String s when s.length() < 5 -> "Short";
        case String s when s.length() < 10 -> "Medium";
        case String s when s.matches("[0-9]+") -> "Numeric";
        case String s when s.matches("[a-zA-Z]+") -> "Alphabetic";
        default -> "Other";
    };
}

// 2. Multiple conditions
record Person(String name, int age, boolean premium) {}

String getPricing(Person person) {
    return switch (person) {
        case Person p when p.age() < 18 -> "Youth discount";
        case Person p when p.premium() && p.age() >= 65 -> "Premium senior";
        case Person p when p.premium() -> "Premium member";
        case Person p when p.age() >= 65 -> "Senior discount";
        default -> "Standard pricing";
    };
}

// 3. Range checking
int categorizeScore(int score) {
    return switch (score) {
        case int s when s >= 90 && s <= 100 -> 'A';
        case int s when s >= 80 && s < 90 -> 'B';
        case int s when s >= 70 && s < 80 -> 'C';
        case int s when s >= 60 && s < 70 -> 'D';
        case int s when s >= 0 && s < 60 -> 'F';
        default -> throw new IllegalArgumentException("Invalid score");
    };
}

// 4. Method calls in when
record Product(String name, double price, String category) {}

String evaluate(Product product) {
    return switch (product) {
        case Product p when isLuxuryBrand(p.name()) -> "Luxury item";
        case Product p when p.price() > 1000 && isInStock(p) -> "Premium available";
        case Product p when p.price() > 1000 -> "Premium out of stock";
        default -> "Standard item";
    };
}
```

---

### Q98. What are the performance implications of pattern matching switch? 🔴

**Performance Considerations:**
```java
// 1. Compiler optimization
// Switch with patterns is optimized by compiler
String result = switch (obj) {
    case String s -> handleString(s);
    case Integer i -> handleInteger(i);
    case Double d -> handleDouble(d);
    default -> handleOther(obj);
};
// Compiler may use hash tables, jump tables, or binary search

// 2. Guard evaluation order matters
// Guards evaluated sequentially until match
switch (value) {
    case int i when expensiveCheck1(i) -> "Match1";  // Called first
    case int i when expensiveCheck2(i) -> "Match2";  // Only if first fails
    case int i when cheapCheck(i) -> "Match3";       // Put cheap checks first!
    default -> "No match";
}

// 3. Pattern complexity affects performance
// Simple type patterns are fast
switch (obj) {
    case String s -> ...;   // Fast
    case Integer i -> ...;  // Fast
}

// Complex nested patterns are slower
switch (obj) {
    case Optional<List<String>> opt when opt.isPresent() -> ...;  // Slower
}

// 4. Sealed types enable optimizations
sealed interface Shape permits Circle, Square {}
// Compiler knows all possible types - better optimization

double area(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Square s -> s.side() * s.side();
    };
}
// No default needed, compiler optimizes better
```

---

### Q99. How do you handle null safely in pattern matching switch? 🟡

**Null Handling:**
```java
// 1. Explicit null case
String handleNull(String input) {
    return switch (input) {
        case null -> "Input was null";
        case "" -> "Input was empty";
        default -> "Input: " + input;
    };
}

// 2. Null with type patterns
Object process(Object obj) {
    return switch (obj) {
        case null -> "Null";
        case String s -> "String: " + s;
        case Integer i -> "Integer: " + i;
        default -> "Other: " + obj;
    };
}

// 3. Combined null and empty check
String validate(String input) {
    return switch (input) {
        case null, "" -> "Invalid input";
        case String s when s.isBlank() -> "Blank input";
        default -> "Valid: " + input;
    };
}

// 4. Null in record patterns
record Point(Integer x, Integer y) {}

String describePoint(Point point) {
    return switch (point) {
        case null -> "No point";
        case Point(null, null) -> "Both null";
        case Point(Integer x, null) -> "Y is null";
        case Point(null, Integer y) -> "X is null";
        case Point(Integer x, Integer y) -> "Point at (" + x + ", " + y + ")";
    };
}

// 5. Optional handling
Optional<String> opt = Optional.ofNullable(value);
String result = switch (opt) {
    case Optional o when o.isEmpty() -> "Empty";
    case Optional o -> "Value: " + o.get();
};
```

---

### Q100. Can you combine pattern matching with other Java features? 🔴

**Combining Features:**
```java
// 1. Pattern Matching + Sealed Classes + Records
sealed interface Message permits TextMessage, ImageMessage, VideoMessage {}
record TextMessage(String content) implements Message {}
record ImageMessage(String url, int width, int height) implements Message {}
record VideoMessage(String url, int duration) implements Message {}

String process(Message msg) {
    return switch (msg) {
        case TextMessage(String content) when content.length() < 100 ->
            "Short text: " + content;
        case TextMessage(String content) ->
            "Long text: " + content.substring(0, 100) + "...";
        case ImageMessage(String url, int w, int h) when w > 1000 ->
            "High-res image: " + url;
        case ImageMessage(String url, int w, int h) ->
            "Image: " + url;
        case VideoMessage(String url, int duration) when duration > 300 ->
            "Long video: " + url;
        case VideoMessage(String url, int duration) ->
            "Video: " + url;
    };
}

// 2. Pattern Matching + Streams
List<Message> messages = getMessages();
List<String> imageUrls = messages.stream()
    .mapMulti((msg, consumer) -> {
        if (msg instanceof ImageMessage(String url, int w, int h)) {
            consumer.accept(url);
        }
    })
    .toList();

// 3. Pattern Matching + Optional
Optional<Object> opt = Optional.of("Hello");
String result = opt.map(obj -> switch (obj) {
    case String s -> s.toUpperCase();
    case Integer i -> String.valueOf(i);
    default -> obj.toString();
}).orElse("Empty");

// 4. Pattern Matching + Virtual Threads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    messages.forEach(msg -> executor.submit(() -> {
        String result = switch (msg) {
            case TextMessage tm -> processText(tm);
            case ImageMessage im -> processImage(im);
            case VideoMessage vm -> processVideo(vm);
        };
        saveResult(result);
    }));
}
```

---

## Other Java 21+ Features

### Q101. What are Unnamed Patterns and Variables (Java 21)? 🟡

**Unnamed Patterns:**
```java
// 1. Underscore for unused variables
// Before: need to name unused variable
if (obj instanceof Point(int x, int y)) {
    System.out.println("Y coordinate: " + y);
    // x is not used
}

// After: Use _ for unused
if (obj instanceof Point(int _, int y)) {
    System.out.println("Y coordinate: " + y);
}

// 2. In record patterns
record RGB(int red, int green, int blue) {}

RGB color = new RGB(255, 128, 64);
switch (color) {
    case RGB(int r, int _, int _) -> 
        System.out.println("Red component: " + r);
}

// 3. In catch blocks
try {
    riskyOperation();
} catch (Exception _) {  // Don't need exception details
    System.out.println("Operation failed");
}

// 4. In lambda parameters
list.forEach(_ -> System.out.println("Item processed"));
// When you don't need the parameter

// 5. Multiple underscores for multiple unused
record Point3D(int x, int y, int z) {}

switch (point) {
    case Point3D(int x, int _, int _) -> 
        System.out.println("X: " + x);
}
```

---

### Q102. What is String Templates (Preview)? 🟡

**String Templates (Preview in Java 21):**
```java
// Note: Preview feature, syntax may change

// Traditional string formatting
String name = "Alice";
int age = 30;
String msg = String.format("Name: %s, Age: %d", name, age);

// String templates (preview)
String msg = STR."Name: \{name}, Age: \{age}";

// With expressions
String msg = STR."Name: \{name.toUpperCase()}, Age: \{age + 1}";

// Multi-line
String html = STR."""
    <html>
        <body>
            <h1>Hello \{name}</h1>
            <p>Age: \{age}</p>
        </body>
    </html>
    """;

// FMT for formatted output
double value = 123.456;
String formatted = FMT."Value: %.2f\{value}";  // "Value: 123.46"

// Custom templates
static StringTemplate.Processor<String, RuntimeException> SAFE = 
    (StringTemplate st) -> {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < st.fragments().size(); i++) {
            sb.append(st.fragments().get(i));
            if (i < st.values().size()) {
                sb.append(escapeHtml(st.values().get(i).toString()));
            }
        }
        return sb.toString();
    };

String safe = SAFE."<div>\{userInput}</div>";  // HTML-escaped
```

---

### Q103. What improvements were made to the Collections API? 🟢

**Collections Enhancements:**
```java
// 1. Sequenced Collections (Java 21)
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
String first = list.getFirst();  // "a"
String last = list.getLast();    // "c"

// 2. List.of() improvements
List<String> list = List.of("a", "b", "c");  // Immutable

// 3. Map.of() and Map.ofEntries()
Map<String, Integer> map = Map.of(
    "one", 1,
    "two", 2,
    "three", 3
);

// More than 10 entries
Map<String, Integer> largeMap = Map.ofEntries(
    Map.entry("key1", 1),
    Map.entry("key2", 2),
    // ... many more
    Map.entry("key20", 20)
);

// 4. Collection factories maintain iteration order
Set<String> set = Set.of("a", "b", "c");  // Iteration order undefined
// For ordered: use LinkedHashSet

// 5. toList() convenience (Java 16+)
List<String> result = stream.toList();  // Immutable list
// vs
List<String> result = stream.collect(Collectors.toList());  // Mutable
```

---

### Q104. What are the important JEPs in Java 21? 🟡

**Key JEPs:**
```java
// JEP 431: Sequenced Collections
SequencedCollection<String> seq = new ArrayList<>();
seq.addFirst("first");
seq.addLast("last");

// JEP 430: String Templates (Preview)
String name = "Alice";
String msg = STR."Hello \{name}";

// JEP 442: Foreign Function & Memory API (Third Preview)
// Access native libraries without JNI

// JEP 443: Unnamed Patterns and Variables (Preview)
if (obj instanceof Point(int x, int _)) {
    // Don't care about y
}

// JEP 444: Virtual Threads
Thread.ofVirtual().start(() -> {
    // Lightweight thread
});

// JEP 445: Unnamed Classes and Instance Main Methods (Preview)
// Simplified Hello World
void main() {
    System.out.println("Hello, World!");
}

// JEP 446: Scoped Values (Preview)
// Alternative to ThreadLocal
```

---

### Q105. How do you use the modern Java features together in real applications? 🔴

**Complete Example:**
```java
// Modern Java application combining multiple features

// 1. Sealed interfaces for domain model
public sealed interface Order permits NewOrder, ProcessingOrder, CompletedOrder, CancelledOrder {}

public record NewOrder(
    String orderId,
    List<OrderItem> items,
    LocalDateTime createdAt
) implements Order {
    public NewOrder {
        items = List.copyOf(items);  // Defensive copy
    }
}

public record ProcessingOrder(
    String orderId,
    List<OrderItem> items,
    LocalDateTime createdAt,
    LocalDateTime processingStartedAt
) implements Order {}

public record CompletedOrder(
    String orderId,
    List<OrderItem> items,
    LocalDateTime createdAt,
    LocalDateTime completedAt,
    String trackingNumber
) implements Order {}

public record CancelledOrder(
    String orderId,
    String reason,
    LocalDateTime cancelledAt
) implements Order {}

// 2. Record for items
public record OrderItem(String productId, int quantity, BigDecimal price) {
    public OrderItem {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be positive");
    }
    
    public BigDecimal total() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

// 3. Service using Virtual Threads
@Service
public class OrderService {
    private final OrderRepository repository;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    
    public CompletableFuture<Order> processOrder(String orderId) {
        return CompletableFuture.supplyAsync(() -> {
            // Runs in virtual thread
            Order order = repository.findById(orderId);
            
            return switch (order) {
                case NewOrder no -> processNewOrder(no);
                case ProcessingOrder po -> continueProcessing(po);
                case CompletedOrder co -> co;  // Already done
                case CancelledOrder co -> throw new OrderCancelledException(co.reason());
            };
        }, Executors.newVirtualThreadPerTaskExecutor());
    }
    
    private Order processNewOrder(NewOrder order) {
        // Pattern matching with records
        var processingOrder = new ProcessingOrder(
            order.orderId(),
            order.items(),
            order.createdAt(),
            LocalDateTime.now()
        );
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Parallel processing with structured concurrency
            var paymentTask = scope.fork(() -> 
                paymentService.processPayment(order.orderId(), calculateTotal(order.items())));
            var inventoryTask = scope.fork(() -> 
                inventoryService.reserveItems(order.items()));
            
            scope.join();
            scope.throwIfFailed();
            
            String trackingNumber = shippingService.createShipment(order);
            
            return new CompletedOrder(
                order.orderId(),
                order.items(),
                order.createdAt(),
                LocalDateTime.now(),
                trackingNumber
            );
        } catch (InterruptedException | ExecutionException e) {
            return new CancelledOrder(
                order.orderId(),
                "Processing failed: " + e.getMessage(),
                LocalDateTime.now()
            );
        }
    }
    
    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
            .map(OrderItem::total)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

// 4. Controller with virtual threads (Spring Boot 3.2+)
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderId) {
        // Runs in virtual thread automatically
        Order order = orderService.getOrder(orderId);
        
        // Pattern matching for response
        return switch (order) {
            case NewOrder no -> ResponseEntity.ok(new OrderDTO(no, "NEW"));
            case ProcessingOrder po -> ResponseEntity.ok(new OrderDTO(po, "PROCESSING"));
            case CompletedOrder co -> ResponseEntity.ok(new OrderDTO(co, "COMPLETED"));
            case CancelledOrder co -> ResponseEntity.ok(new OrderDTO(co, "CANCELLED"));
        };
    }
}

// This example demonstrates:
// - Sealed interfaces for type safety
// - Records for immutable data
// - Pattern matching for exhaustive handling
// - Virtual threads for high concurrency
// - Structured concurrency for parallel tasks
// - Modern Java best practices
```

---

### Q106-Q110: Performance and Best Practices

### Q106. What are the best practices for using Java 21+ features? 🔴

**Best Practices:**
```java
// 1. Use Virtual Threads for IO-bound operations
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<CompletableFuture<String>> futures = urls.stream()
        .map(url -> CompletableFuture.supplyAsync(
            () -> fetchUrl(url), executor))
        .toList();
}

// 2. Use Sequenced Collections for ordered access
SequencedMap<String, User> users = new LinkedHashMap<>();
User newest = users.lastEntry().getValue();
User oldest = users.firstEntry().getValue();

// 3. Combine Sealed Classes + Records + Pattern Matching
sealed interface Result<T> permits Success, Failure {}
record Success<T>(T value) implements Result<T> {}
record Failure<T>(Exception error) implements Result<T> {}

public <T> T unwrapOrThrow(Result<T> result) {
    return switch (result) {
        case Success<T>(T value) -> value;
        case Failure<T>(Exception e) -> throw new RuntimeException(e);
    };
}

// 4. Use Structured Concurrency for related tasks
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var task1 = scope.fork(() -> operation1());
    var task2 = scope.fork(() -> operation2());
    scope.join();
    scope.throwIfFailed();
    return combine(task1.get(), task2.get());
}

// 5. Prefer pattern matching over instanceof chains
// ❌ Old way
if (obj instanceof String) {
    String s = (String) obj;
    process(s);
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    process(i);
}

// ✅ New way
switch (obj) {
    case String s -> process(s);
    case Integer i -> process(i);
    default -> handleOther(obj);
}
```

---

*File complete with comprehensive Java 21+ features!*

---

**📘 Continue to:** [05-ADVANCED-TOPICS.md](05-ADVANCED-TOPICS.md)
