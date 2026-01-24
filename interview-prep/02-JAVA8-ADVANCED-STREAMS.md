# Java 8 Advanced Streams - Interview Questions 🌊

> **Part 2 of Comprehensive Java Interview Guide**
>
> Focuses on advanced Stream API topics, parallel processing, and performance optimization.

---

## 📑 Table of Contents
- Stream Performance & Optimization (Q22-Q30)
- Parallel Streams Deep Dive (Q31-Q35)
- Custom Collectors (Q36-Q40)
- Stream Gotchas & Edge Cases (Q41-Q45)

---

## Stream Performance & Optimization

### Q22. What are stateful vs stateless operations in Streams? 🟡

**Short Answer:**
- **Stateless operations**: Don't retain state from previously seen elements (filter, map, flatMap)
- **Stateful operations**: May need to process entire stream before producing result (sorted, distinct, limit, skip)

**Detailed Explanation:**
```java
// Stateless operations - process elements independently
// Can be parallelized efficiently

// filter() - Stateless
list.stream()
    .filter(s -> s.length() > 5)  // Each element checked independently
    .toList();

// map() - Stateless
list.stream()
    .map(String::toUpperCase)  // Each element transformed independently
    .toList();

// flatMap() - Stateless
list.stream()
    .flatMap(s -> Arrays.stream(s.split("")))  // Independent
    .toList();

// Stateful operations - may need to see all/many elements
// Harder to parallelize, may buffer elements

// distinct() - Stateful (must remember all seen elements)
list.stream()
    .distinct()  // Must track all unique elements
    .toList();

// sorted() - Stateful (must see all elements to sort)
list.stream()
    .sorted()  // Requires all elements to determine order
    .toList();

// limit() - Stateful (must count elements)
list.stream()
    .limit(10)  // Must track count
    .toList();

// skip() - Stateful (must count skipped elements)
list.stream()
    .skip(5)  // Must track position
    .toList();
```

**Performance Implications:**
```java
// Good: Stateless operations first (cheap)
list.stream()
    .filter(s -> s.length() > 5)  // Stateless - reduces elements
    .map(String::toUpperCase)      // Stateless
    .distinct()                    // Stateful - fewer elements to process
    .toList();

// Less efficient: Stateful operations too early
list.stream()
    .distinct()                    // Stateful - processes all elements
    .filter(s -> s.length() > 5)  // Could have reduced before distinct
    .toList();

// Optimization: Place cheap filters before expensive operations
list.stream()
    .filter(s -> !s.isEmpty())           // Cheap filter
    .filter(s -> expensiveCheck(s))      // Expensive filter
    .sorted()                             // Expensive stateful operation
    .toList();
```

**Parallel Stream Considerations:**
```java
// Stateless operations parallelize well
list.parallelStream()
    .filter(predicate)
    .map(function)
    .toList();  // Efficient parallel execution

// Stateful operations require coordination in parallel
list.parallelStream()
    .distinct()  // Requires merging distinct sets from each thread
    .sorted()    // Requires merging sorted segments
    .toList();   // More overhead in parallel

// Order-dependent operations are expensive in parallel
list.parallelStream()
    .limit(10)   // Must coordinate across threads to get first 10
    .toList();

// Unordered can improve performance
list.parallelStream()
    .unordered()   // Removes ordering constraint
    .distinct()    // Can be more efficient without ordering
    .toList();
```

**Bounded vs Unbounded Stateful Operations:**
```java
// Bounded stateful - limited memory usage
stream.limit(100)  // At most 100 elements buffered
stream.skip(10)    // Count up to 10

// Unbounded stateful - may buffer entire stream
stream.distinct()  // Must remember all seen elements
stream.sorted()    // Must buffer all elements to sort

// For large streams, unbounded operations can cause OutOfMemoryError
Stream.iterate(0, n -> n + 1)  // Infinite stream
    .distinct()  // Would try to buffer infinite elements - BAD
    .limit(100)  // Must come before unbounded operations
    .toList();

// Correct order
Stream.iterate(0, n -> n + 1)
    .limit(100)   // Bounded first
    .distinct()   // Now processes only 100 elements
    .toList();
```

---

### Q23. How do you optimize Stream performance? 🔴

**Short Answer:**
Optimize by choosing appropriate operations, ordering them efficiently, avoiding unnecessary boxing, using primitive streams, and knowing when to use parallel streams.

**Detailed Explanation:**

**1. Operation Ordering:**
```java
// ❌ Bad: Expensive operations on large dataset
list.stream()
    .map(s -> expensiveTransformation(s))  // Applied to all elements
    .filter(s -> s.length() > 5)           // Then filtered
    .toList();

// ✅ Good: Filter first to reduce dataset
list.stream()
    .filter(s -> s.length() > 5)           // Filter early
    .map(s -> expensiveTransformation(s))  // Applied to fewer elements
    .toList();

// ❌ Bad: Multiple passes over data
long count = list.stream().count();
int sum = list.stream().mapToInt(String::length).sum();
double avg = list.stream().mapToInt(String::length).average().orElse(0);

// ✅ Good: Single pass with collector
IntSummaryStatistics stats = list.stream()
    .collect(Collectors.summarizingInt(String::length));
stats.getCount();
stats.getSum();
stats.getAverage();
```

**2. Avoid Boxing/Unboxing:**
```java
// ❌ Bad: Boxing overhead
List<Integer> numbers = getNumbers();
int sum = numbers.stream()
    .reduce(0, Integer::sum);  // Boxing/unboxing on each operation

// ✅ Good: Use primitive streams
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();

// Or if starting with primitives
int[] array = getIntArray();
int sum = Arrays.stream(array).sum();  // IntStream - no boxing

// Primitive stream operations
IntStream.range(0, 1000)
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .sum();  // All operations on primitives
```

**3. Short-Circuit When Possible:**
```java
// ✅ Use short-circuiting terminal operations
boolean hasLongString = list.stream()
    .anyMatch(s -> s.length() > 100);  // Stops at first match

Optional<String> first = list.stream()
    .filter(predicate)
    .findFirst();  // Stops after finding first

// ✅ Use limit() to cap processing
List<String> first10 = list.stream()
    .filter(predicate)
    .limit(10)  // Stops after 10 matches
    .toList();

// ❌ Don't process entire stream if not needed
boolean hasMatch = list.stream()
    .filter(predicate)
    .collect(Collectors.toList())
    .size() > 0;  // Processes everything!

// ✅ Better
boolean hasMatch = list.stream().anyMatch(predicate);
```

**4. Reuse Expensive Operations:**
```java
// ❌ Bad: Repeated expensive operations
Predicate<String> expensive = s -> expensiveCheck(s);

list.stream().filter(expensive).count();
list.stream().filter(expensive).toList();  // Recomputes everything

// ✅ Good: Compute once, reuse
List<String> filtered = list.stream()
    .filter(expensive)
    .toList();

long count = filtered.size();
// Use filtered list for other operations
```

**5. Choose Right Data Structures:**
```java
// If you need frequent lookups, use appropriate structure
Set<String> validIds = getValidIds();  // HashSet for O(1) lookup

list.stream()
    .filter(item -> validIds.contains(item.getId()))  // Fast lookup
    .toList();

// vs using List for validation (O(n) lookup)
List<String> validIdsList = getValidIds();
list.stream()
    .filter(item -> validIdsList.contains(item.getId()))  // Slow!
    .toList();
```

**6. Parallel Streams (Use Wisely):**
```java
// ✅ Good candidates for parallel:
// - Large datasets (10,000+ elements)
// - CPU-intensive operations
// - Independent operations

List<ComplexObject> results = largeList.parallelStream()
    .filter(predicate)
    .map(obj -> cpuIntensiveTransformation(obj))
    .toList();

// ❌ Bad candidates for parallel:
// - Small datasets (overhead > benefit)
// - IO-bound operations (threads waiting)
// - Operations with side effects

// Don't use parallel for small lists
List<Integer> small = List.of(1, 2, 3, 4, 5);
small.parallelStream()  // Overhead > benefit
    .map(n -> n * 2)
    .toList();

// Don't use parallel for IO operations
files.parallelStream()
    .map(file -> readFile(file))  // IO-bound - use virtual threads instead
    .toList();
```

**7. Avoid Unnecessary Intermediate Collections:**
```java
// ❌ Bad: Creating unnecessary collections
List<String> temp = list.stream()
    .filter(predicate1)
    .toList();

List<String> result = temp.stream()
    .filter(predicate2)
    .toList();

// ✅ Good: Single pipeline
List<String> result = list.stream()
    .filter(predicate1)
    .filter(predicate2)
    .toList();
```

**8. Use Method References:**
```java
// Method references are often optimized better by JVM

// ❌ Lambda
list.stream()
    .map(s -> s.toUpperCase())
    .forEach(s -> System.out.println(s));

// ✅ Method reference (slightly better performance)
list.stream()
    .map(String::toUpperCase)
    .forEach(System.out::println);
```

**9. Consider Non-Stream Alternatives:**
```java
// Sometimes traditional loops are faster for simple operations

// For very simple operations on small lists
List<String> result = new ArrayList<>();
for (String s : list) {
    if (s.length() > 5) {
        result.add(s.toUpperCase());
    }
}

// vs Stream (has overhead)
List<String> result = list.stream()
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase)
    .toList();

// Stream is better for:
// - Complex pipelines
// - Readability
// - Parallel processing
// - Large datasets
```

**10. Benchmarking:**
```java
// Always measure before optimizing
long start = System.nanoTime();

// Your stream operation
List<String> result = list.stream()
    .filter(predicate)
    .map(function)
    .toList();

long duration = System.nanoTime() - start;
System.out.println("Time: " + duration / 1_000_000 + "ms");

// Use JMH for accurate benchmarking
// @Benchmark
// public void testStreamPerformance() {
//     list.stream().filter(predicate).toList();
// }
```

---

### Q24. What's the difference between Collection.stream() and Collection.parallelStream()? 🟡

**Short Answer:**
`stream()` processes elements sequentially in a single thread, while `parallelStream()` splits work across multiple threads using ForkJoinPool.

**Detailed Explanation:**
```java
List<Integer> numbers = IntStream.range(0, 1000).boxed().toList();

// Sequential stream - single thread
List<Integer> sequential = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .toList();

// Parallel stream - multiple threads
List<Integer> parallel = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .toList();

// Can convert between them
Stream<Integer> seq = numbers.stream();
Stream<Integer> par = seq.parallel();  // Now parallel

Stream<Integer> par2 = numbers.parallelStream();
Stream<Integer> seq2 = par2.sequential();  // Now sequential
```

**How Parallel Streams Work:**
```java
// Parallel streams use ForkJoinPool.commonPool()
// Default parallelism = number of CPU cores

// Check available parallelism
int parallelism = ForkJoinPool.commonPool().getParallelism();
System.out.println("Parallelism: " + parallelism);  // Usually CPU cores - 1

// Process:
// 1. Stream splits data into chunks
// 2. Each chunk processed by different thread
// 3. Results combined (reduced) back together

// Example: How work is divided
List<Integer> numbers = IntStream.range(0, 100).boxed().toList();

numbers.parallelStream()
    .map(n -> {
        System.out.println(Thread.currentThread().getName() + ": " + n);
        return n * 2;
    })
    .toList();

// Output shows multiple threads:
// ForkJoinPool.commonPool-worker-1: 25
// ForkJoinPool.commonPool-worker-2: 50
// main: 0
// ForkJoinPool.commonPool-worker-3: 75
```

**When Parallel is Faster:**
```java
// Large dataset + CPU-intensive operation
List<BigInteger> numbers = generateLargeList(100_000);

// Parallel is much faster here
long start = System.currentTimeMillis();
List<BigInteger> result = numbers.parallelStream()
    .map(n -> n.pow(1000))  // CPU-intensive
    .toList();
long duration = System.currentTimeMillis() - start;
System.out.println("Parallel: " + duration + "ms");

// vs Sequential (slower)
start = System.currentTimeMillis();
result = numbers.stream()
    .map(n -> n.pow(1000))
    .toList();
duration = System.currentTimeMillis() - start;
System.out.println("Sequential: " + duration + "ms");
```

**When Parallel is Slower:**
```java
// Small dataset - overhead > benefit
List<Integer> small = List.of(1, 2, 3, 4, 5);

// Parallel is slower due to thread overhead
small.parallelStream()
    .map(n -> n * 2)
    .toList();

// IO-bound operations - threads just wait
List<String> urls = getUrls();
urls.parallelStream()
    .map(url -> httpClient.get(url))  // Threads blocked on IO
    .toList();  // Use virtual threads instead!

// Stateful operations - require coordination
list.parallelStream()
    .sorted()  // Needs to merge sorted segments
    .toList(); // Coordination overhead
```

**Thread Safety Concerns:**
```java
// ❌ WRONG: Shared mutable state
List<String> results = new ArrayList<>();  // Not thread-safe!
list.parallelStream()
    .forEach(item -> results.add(item));  // Race condition!

// ✅ CORRECT: Use collector
List<String> results = list.parallelStream()
    .collect(Collectors.toList());  // Thread-safe

// ❌ WRONG: External mutation
int[] counter = {0};
list.parallelStream()
    .forEach(item -> counter[0]++);  // Race condition!

// ✅ CORRECT: Use reduce or count
long count = list.parallelStream().count();
```

**Controlling Parallelism:**
```java
// Custom ForkJoinPool with specific parallelism
ForkJoinPool customPool = new ForkJoinPool(4);  // 4 threads

List<Integer> result = customPool.submit(() ->
    numbers.parallelStream()
        .map(n -> n * 2)
        .toList()
).get();

customPool.shutdown();

// Or set system property (affects all parallel streams)
// System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4");
```

**Order Preservation:**
```java
// Parallel streams try to maintain encounter order if source is ordered
List<Integer> ordered = List.of(1, 2, 3, 4, 5);

List<Integer> result = ordered.parallelStream()
    .map(n -> n * 2)
    .toList();
// Result maintains order: [2, 4, 6, 8, 10]

// But maintaining order adds overhead
// If order doesn't matter, use unordered()
Set<Integer> unordered = Set.of(1, 2, 3, 4, 5);
List<Integer> result = unordered.parallelStream()
    .unordered()  // Removes ordering constraint
    .map(n -> n * 2)
    .toList();  // Faster - order not guaranteed
```

**Comparison Table:**
| Aspect | stream() | parallelStream() |
|--------|----------|------------------|
| **Threads** | Single thread | Multiple threads (ForkJoinPool) |
| **Performance** | Predictable | Varies (overhead for small datasets) |
| **Order** | Always maintains | Maintains if source is ordered |
| **Thread-safety** | No concerns | Must be careful |
| **Best for** | Small datasets, IO operations | Large datasets, CPU-intensive |
| **Overhead** | Minimal | Thread management overhead |

---

### Q25. What are the common pitfalls when using Streams? 🔴

**1. Reusing Streams:**
```java
// ❌ WRONG: Streams can only be used once
Stream<String> stream = list.stream();
stream.forEach(System.out::println);
stream.forEach(System.out::println);  // IllegalStateException: stream has already been operated upon

// ✅ CORRECT: Create new stream for each operation
list.stream().forEach(System.out::println);
list.stream().forEach(System.out::println);
```

**2. Modifying Source While Streaming:**
```java
// ❌ WRONG: ConcurrentModificationException
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
list.stream()
    .forEach(item -> list.remove(item));  // Exception!

// ✅ CORRECT: Use removeIf or collect to new list
list.removeIf(item -> someCondition(item));

// Or collect to new list
List<String> newList = list.stream()
    .filter(item -> !someCondition(item))
    .toList();
```

**3. Ignoring Lazy Evaluation:**
```java
// ❌ WRONG: Thinking this does something
list.stream()
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase);  // No terminal operation - nothing happens!

// ✅ CORRECT: Add terminal operation
list.stream()
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase)
    .toList();  // Now it executes

// ❌ WRONG: Expecting side effects in intermediate operations
list.stream()
    .map(s -> {
        saveToDatabase(s);  // May not execute if stream not consumed!
        return s;
    });

// ✅ CORRECT: Use forEach (terminal operation)
list.stream().forEach(s -> saveToDatabase(s));
```

**4. Optional.get() Without Checking:**
```java
// ❌ WRONG: Can throw NoSuchElementException
Optional<String> opt = list.stream().findFirst();
String value = opt.get();  // Throws if empty!

// ✅ CORRECT: Use safe methods
String value = opt.orElse("default");
String value = opt.orElseGet(() -> computeDefault());
String value = opt.orElseThrow(() -> new CustomException());
opt.ifPresent(v -> doSomething(v));
```

**5. Parallel Stream Pitfalls:**
```java
// ❌ WRONG: Shared mutable state
int sum = 0;
numbers.parallelStream()
    .forEach(n -> sum += n);  // Race condition!

// ✅ CORRECT: Use reduce or sum
int sum = numbers.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();

// ❌ WRONG: Order-dependent operations in parallel
List<String> result = list.parallelStream()
    .sorted()
    .limit(10)  // Expensive in parallel
    .toList();

// ✅ BETTER: Use sequential for order-dependent ops
List<String> result = list.stream()
    .sorted()
    .limit(10)
    .toList();
```

**6. Incorrect flatMap Usage:**
```java
// ❌ WRONG: Using map instead of flatMap
List<List<String>> nested = getNested();
List<String> flat = nested.stream()
    .map(List::stream)  // Wrong: Stream<Stream<String>>
    .toList();  // Compile error!

// ✅ CORRECT: Use flatMap
List<String> flat = nested.stream()
    .flatMap(List::stream)  // Correct: Stream<String>
    .toList();
```

**7. Boxing in Primitive Operations:**
```java
// ❌ WRONG: Unnecessary boxing
List<Integer> numbers = getNumbers();
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);  // Boxing/unboxing overhead

// ✅ CORRECT: Use primitive stream
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();
```

**8. Collecting to Wrong Type:**
```java
// ❌ WRONG: Losing type information
List list = stream.collect(Collectors.toList());  // Raw type

// ✅ CORRECT: Maintain generics
List<String> list = stream.collect(Collectors.toList());

// ❌ WRONG: Duplicates in Set not considered
Set<String> set = list.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toSet());  // May lose elements if duplicates after mapping

// Be aware of this behavior
```

**9. Infinite Streams Without Limit:**
```java
// ❌ WRONG: Infinite stream without limit
Stream.iterate(0, n -> n + 1)
    .forEach(System.out::println);  // Never stops!

// ✅ CORRECT: Use limit
Stream.iterate(0, n -> n + 1)
    .limit(100)
    .forEach(System.out::println);

// ❌ WRONG: Stateful operations on infinite streams
Stream.iterate(0, n -> n + 1)
    .distinct()  // Tries to buffer infinite elements!
    .limit(100)
    .toList();

// ✅ CORRECT: Limit before stateful operations
Stream.iterate(0, n -> n + 1)
    .limit(100)
    .distinct()
    .toList();
```

**10. Null Elements:**
```java
// ❌ WRONG: Nulls can cause NPE
List<String> list = Arrays.asList("a", null, "b");
list.stream()
    .map(String::toUpperCase)  // NullPointerException!
    .toList();

// ✅ CORRECT: Filter nulls first
list.stream()
    .filter(Objects::nonNull)
    .map(String::toUpperCase)
    .toList();

// Or use Optional
list.stream()
    .map(Optional::ofNullable)
    .filter(Optional::isPresent)
    .map(Optional::get)
    .map(String::toUpperCase)
    .toList();
```

**11. Performance Misconceptions:**
```java
// ❌ WRONG: Assuming parallel is always faster
smallList.parallelStream()  // Overhead > benefit for small lists
    .map(simpleOperation)
    .toList();

// ❌ WRONG: Multiple terminal operations
long count = list.stream().count();
List<String> collected = list.stream().toList();  // Processes twice!

// ✅ CORRECT: Collect once, then query
List<String> collected = list.stream().toList();
long count = collected.size();
```

**12. Incorrect Collectors:**
```java
// ❌ WRONG: Collectors.toMap with duplicate keys
List<Person> people = getPeople();
Map<String, Person> map = people.stream()
    .collect(Collectors.toMap(
        Person::getName,  // Duplicate names cause IllegalStateException
        Function.identity()
    ));

// ✅ CORRECT: Handle duplicates
Map<String, Person> map = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Function.identity(),
        (existing, replacement) -> existing  // Keep first
    ));
```

---

### Q26. How do you handle exceptions in Streams? 🟡

**Short Answer:**
Streams don't handle checked exceptions well. You need to wrap operations in try-catch, create wrapper methods, or use libraries like Vavr.

**Detailed Explanation:**

**1. Problem with Checked Exceptions:**
```java
// This doesn't compile - parseInt throws checked exception
List<String> strings = List.of("1", "2", "3");
List<Integer> numbers = strings.stream()
    .map(Integer::parseInt)  // This actually works (unchecked)
    .toList();

// But this doesn't work:
List<String> urls = List.of("http://example.com");
List<String> contents = urls.stream()
    .map(url -> fetchUrl(url))  // Compile error if fetchUrl throws IOException
    .toList();
```

**2. Solution 1: Wrapper Method:**
```java
// Create wrapper that handles exception
private String fetchUrlSafe(String url) {
    try {
        return fetchUrl(url);
    } catch (IOException e) {
        return null;  // or default value
        // or throw unchecked exception
    }
}

// Use in stream
List<String> contents = urls.stream()
    .map(this::fetchUrlSafe)
    .filter(Objects::nonNull)
    .toList();
```

**3. Solution 2: Functional Interface Wrapper:**
```java
@FunctionalInterface
interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}

// Generic wrapper
static <T, R> Function<T, R> wrap(CheckedFunction<T, R> function) {
    return t -> {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}

// Usage
List<String> contents = urls.stream()
    .map(wrap(url -> fetchUrl(url)))  // Checked exception wrapped
    .toList();
```

**4. Solution 3: Try-Catch Inside Lambda:**
```java
List<String> contents = urls.stream()
    .map(url -> {
        try {
            return fetchUrl(url);
        } catch (IOException e) {
            log.error("Failed to fetch: " + url, e);
            return null;
        }
    })
    .filter(Objects::nonNull)
    .toList();
```

**5. Solution 4: Collecting Errors:**
```java
// Collect both results and errors
record Result<T>(T value, Exception error) {
    boolean isSuccess() { return error == null; }
}

List<Result<String>> results = urls.stream()
    .map(url -> {
        try {
            return new Result<>(fetchUrl(url), null);
        } catch (Exception e) {
            return new Result<>(null, e);
        }
    })
    .toList();

// Separate successes and failures
List<String> successes = results.stream()
    .filter(Result::isSuccess)
    .map(Result::value)
    .toList();

List<Exception> errors = results.stream()
    .filter(r -> !r.isSuccess())
    .map(Result::error)
    .toList();
```

**6. Solution 5: Using Optional for Errors:**
```java
List<Optional<String>> results = urls.stream()
    .map(url -> {
        try {
            return Optional.of(fetchUrl(url));
        } catch (Exception e) {
            log.error("Error", e);
            return Optional.empty();
        }
    })
    .toList();

// Extract successful results
List<String> successful = results.stream()
    .flatMap(Optional::stream)  // Java 9+
    .toList();
```

**7. Solution 6: CompletableFuture for Async Exception Handling:**
```java
List<CompletableFuture<String>> futures = urls.stream()
    .map(url -> CompletableFuture.supplyAsync(() -> {
        try {
            return fetchUrl(url);
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }))
    .toList();

// Wait for all and handle exceptions
List<String> results = futures.stream()
    .map(future -> {
        try {
            return future.join();
        } catch (CompletionException e) {
            log.error("Error", e.getCause());
            return null;
        }
    })
    .filter(Objects::nonNull)
    .toList();
```

**8. Solution 7: Sneaky Throws (Not Recommended):**
```java
@SuppressWarnings("unchecked")
static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
    throw (E) e;
}

List<String> contents = urls.stream()
    .map(url -> {
        try {
            return fetchUrl(url);
        } catch (Exception e) {
            sneakyThrow(e);
            return null;  // Never reached
        }
    })
    .toList();
```

**9. Best Practice: Design for Streams:**
```java
// Design methods to return Optional instead of throwing
public Optional<String> fetchUrlSafe(String url) {
    try {
        return Optional.of(fetchUrl(url));
    } catch (IOException e) {
        log.error("Failed to fetch: " + url, e);
        return Optional.empty();
    }
}

// Clean stream usage
List<String> contents = urls.stream()
    .map(this::fetchUrlSafe)
    .flatMap(Optional::stream)
    .toList();
```

**10. Real-World Example:**
```java
// Processing files with error handling
public record FileResult(Path file, String content, Exception error) {
    public boolean isSuccess() { return error == null; }
}

public List<FileResult> processFiles(List<Path> files) {
    return files.stream()
        .map(file -> {
            try {
                String content = Files.readString(file);
                return new FileResult(file, content, null);
            } catch (IOException e) {
                return new FileResult(file, null, e);
            }
        })
        .toList();
}

// Use results
List<FileResult> results = processFiles(files);

// Log errors
results.stream()
    .filter(r -> !r.isSuccess())
    .forEach(r -> log.error("Failed: " + r.file(), r.error()));

// Process successes
List<String> contents = results.stream()
    .filter(FileResult::isSuccess)
    .map(FileResult::content)
    .toList();
```

---

## Parallel Streams Deep Dive

### Q27. What is the ForkJoinPool and how does it relate to parallel streams? 🔴

**Short Answer:**
ForkJoinPool is a thread pool designed for divide-and-conquer tasks. Parallel streams use `ForkJoinPool.commonPool()` by default to execute operations across multiple threads.

**Detailed Explanation:**

**1. How ForkJoinPool Works:**
```java
// ForkJoinPool uses work-stealing algorithm
// Tasks are split (forked) and then results are combined (joined)

// Default common pool
ForkJoinPool commonPool = ForkJoinPool.commonPool();
int parallelism = commonPool.getParallelism();
System.out.println("Parallelism: " + parallelism);  // Usually CPU cores - 1

// Example: Parallel stream uses common pool
List<Integer> numbers = IntStream.range(0, 1000).boxed().toList();
numbers.parallelStream()
    .map(n -> {
        System.out.println(Thread.currentThread().getName());
        return n * 2;
    })
    .toList();

// Output shows ForkJoinPool threads:
// ForkJoinPool.commonPool-worker-1
// ForkJoinPool.commonPool-worker-2
// main (also participates)
// ForkJoinPool.commonPool-worker-3
```

**2. Work-Stealing Algorithm:**
```java
// How ForkJoinPool distributes work:
// 1. Task queue for each thread
// 2. If thread finishes its work, it "steals" from other threads
// 3. Maximizes CPU utilization

// Visualization:
// Thread 1: [Task1, Task2, Task3] -> Processing Task1
// Thread 2: [Task4, Task5] -> Done, steals Task3 from Thread 1
// Thread 3: [Task6] -> Done, steals Task5 from Thread 2

// This is automatic with parallel streams
List<Integer> result = largeList.parallelStream()
    .map(expensiveOperation)  // Work automatically distributed and stolen as needed
    .toList();
```

**3. Custom ForkJoinPool:**
```java
// Create custom pool with specific parallelism
ForkJoinPool customPool = new ForkJoinPool(4);

try {
    List<Integer> result = customPool.submit(() ->
        numbers.parallelStream()
            .map(n -> n * 2)
            .toList()
    ).get();
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    customPool.shutdown();
}

// Why use custom pool?
// 1. Isolate expensive operations
// 2. Control parallelism level
// 3. Avoid blocking common pool
```

**4. Common Pool Configuration:**
```java
// Set parallelism for common pool (must be set before first use)
System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

// Check current parallelism
int parallelism = ForkJoinPool.getCommonPoolParallelism();

// NOTE: This affects ALL parallel streams in the JVM!
```

**5. Blocking Operations and Common Pool:**
```java
// ❌ Problem: Blocking common pool threads
List<String> urls = getUrls();
List<String> contents = urls.parallelStream()
    .map(url -> {
        // Blocking IO - all ForkJoinPool threads blocked!
        return httpClient.get(url);  // Bad!
    })
    .toList();

// ✅ Solution 1: Use virtual threads (Java 21+)
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
List<Future<String>> futures = urls.stream()
    .map(url -> executor.submit(() -> httpClient.get(url)))
    .toList();

// ✅ Solution 2: Use separate thread pool
ExecutorService ioPool = Executors.newFixedThreadPool(100);
List<CompletableFuture<String>> futures = urls.stream()
    .map(url -> CompletableFuture.supplyAsync(() -> httpClient.get(url), ioPool))
    .toList();

ioPool.shutdown();

// ✅ Solution 3: Use ManagedBlocker for blocking operations
class UrlFetcher implements ForkJoinPool.ManagedBlocker {
    private String result;
    private final String url;
    
    UrlFetcher(String url) {
        this.url = url;
    }
    
    @Override
    public boolean block() throws InterruptedException {
        result = fetchUrl(url);
        return true;
    }
    
    @Override
    public boolean isReleasable() {
        return result != null;
    }
    
    public String getResult() {
        return result;
    }
}

// Usage
List<String> contents = urls.parallelStream()
    .map(url -> {
        UrlFetcher fetcher = new UrlFetcher(url);
        try {
            ForkJoinPool.managedBlock(fetcher);
            return fetcher.getResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    })
    .toList();
```

**6. Recursive Tasks with ForkJoinPool:**
```java
// Direct use of ForkJoinPool for recursive tasks
class SumTask extends RecursiveTask<Long> {
    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 1000;
    
    SumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // Base case: compute directly
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            // Recursive case: split task
            int mid = start + (end - start) / 2;
            SumTask leftTask = new SumTask(array, start, mid);
            SumTask rightTask = new SumTask(array, mid, end);
            
            leftTask.fork();  // Async execute
            long rightResult = rightTask.compute();  // Execute in this thread
            long leftResult = leftTask.join();  // Wait for async result
            
            return leftResult + rightResult;
        }
    }
}

// Usage
int[] array = new int[100_000];
ForkJoinPool pool = new ForkJoinPool();
long sum = pool.invoke(new SumTask(array, 0, array.length));
```

**7. Monitoring ForkJoinPool:**
```java
ForkJoinPool pool = ForkJoinPool.commonPool();

// Pool statistics
System.out.println("Parallelism: " + pool.getParallelism());
System.out.println("Active threads: " + pool.getActiveThreadCount());
System.out.println("Running threads: " + pool.getRunningThreadCount());
System.out.println("Queued submissions: " + pool.getQueuedSubmissionCount());
System.out.println("Queued tasks: " + pool.getQueuedTaskCount());
System.out.println("Steal count: " + pool.getStealCount());
```

**8. ForkJoinPool vs ThreadPoolExecutor:**
```java
// ForkJoinPool - Best for:
// - Divide-and-conquer tasks
// - Recursive algorithms
// - Parallel streams
// - Tasks that spawn subtasks

ForkJoinPool fjp = new ForkJoinPool();
fjp.submit(() -> parallelStreamOperation());

// ThreadPoolExecutor - Best for:
// - Independent tasks
// - IO-bound operations
// - Fixed number of tasks
// - Traditional async operations

ExecutorService executor = Executors.newFixedThreadPool(10);
executor.submit(() -> ioOperation());
```

---

### Q28. How does Stream.parallel() actually split the work? 🔴

**Detailed Explanation:**

**1. Spliterator - The Key to Parallel Splitting:**
```java
// Every collection has a Spliterator that defines how to split
List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8);
Spliterator<Integer> spliterator = list.spliterator();

// Spliterator characteristics determine split behavior
System.out.println("Size: " + spliterator.estimateSize());
System.out.println("Characteristics: " + spliterator.characteristics());

// Try to split
Spliterator<Integer> split1 = spliterator.trySplit();
if (split1 != null) {
    System.out.println("Split successful");
    // Now we have two spliterators covering different parts of the list
}
```

**2. How Different Collections Split:**
```java
// ArrayList - Excellent splitting (random access)
List<Integer> arrayList = new ArrayList<>(List.of(1,2,3,4,5,6,7,8));
// Splits in half efficiently: [1,2,3,4] [5,6,7,8]
// Then recursively: [1,2] [3,4] [5,6] [7,8]

arrayList.parallelStream()
    .forEach(n -> System.out.println(Thread.currentThread().getName() + ": " + n));

// LinkedList - Poor splitting (sequential access)
List<Integer> linkedList = new LinkedList<>(List.of(1,2,3,4,5,6,7,8));
// Cannot split efficiently - each split requires traversal
// Parallel performance is worse than sequential!

linkedList.parallelStream()  // Don't do this!
    .forEach(n -> System.out.println(Thread.currentThread().getName() + ": " + n));

// HashSet - Good splitting
Set<Integer> hashSet = new HashSet<>(List.of(1,2,3,4,5,6,7,8));
// Splits by hash buckets

// TreeSet - Good splitting
TreeSet<Integer> treeSet = new TreeSet<>(List.of(1,2,3,4,5,6,7,8));
// Splits by tree structure
```

**3. Spliterator Characteristics:**
```java
// Characteristics affect how parallel streams work

// ORDERED - maintains encounter order
List<Integer> ordered = List.of(1,2,3,4);
// Has ORDERED characteristic
// parallel().forEachOrdered() respects order

// SIZED - knows exact size
List<Integer> sized = List.of(1,2,3,4);
// Can split evenly

// DISTINCT - no duplicates
Set<Integer> distinct = Set.of(1,2,3,4);
// Can optimize distinct() operation

// SORTED - elements in sorted order
TreeSet<Integer> sorted = new TreeSet<>(List.of(1,2,3,4));
// Can optimize sorted() operation

// IMMUTABLE - cannot be modified
List<Integer> immutable = List.of(1,2,3,4);
// Thread-safe for parallel operations

// Check characteristics
Spliterator<Integer> sp = list.spliterator();
boolean isOrdered = sp.hasCharacteristics(Spliterator.ORDERED);
boolean isSized = sp.hasCharacteristics(Spliterator.SIZED);
```

**4. Splitting Strategy:**
```java
// Parallel streams split recursively until threshold reached

List<Integer> numbers = IntStream.range(0, 16).boxed().toList();

numbers.parallelStream()
    .map(n -> {
        String thread = Thread.currentThread().getName();
        System.out.println(thread + " processing: " + n);
        return n * 2;
    })
    .toList();

// Typical output shows splitting:
// Initial: [0-15]
// Split 1: [0-7] [8-15]
// Split 2: [0-3] [4-7] [8-11] [12-15]
// Split 3: [0-1] [2-3] [4-5] [6-7] [8-9] [10-11] [12-13] [14-15]
// Each chunk processed by different thread
```

**5. Unbalanced Splitting:**
```java
// Some operations create unbalanced splits

// filter() - unpredictable
List<Integer> numbers = IntStream.range(0, 1000).boxed().toList();
List<Integer> result = numbers.parallelStream()
    .filter(n -> n > 500)  // Second half passes, first half doesn't
    .toList();
// Threads processing first half do less work!

// Solution: Use unordered() if order doesn't matter
numbers.parallelStream()
    .unordered()
    .filter(n -> n > 500)
    .toList();
```

**6. Custom Spliterator:**
```java
// Create custom spliterator for custom data structure
class CustomSpliterator<T> implements Spliterator<T> {
    private final List<T> data;
    private int start;
    private int end;
    
    CustomSpliterator(List<T> data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }
    
    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (start < end) {
            action.accept(data.get(start++));
            return true;
        }
        return false;
    }
    
    @Override
    public Spliterator<T> trySplit() {
        int currentSize = end - start;
        if (currentSize < 10) {
            return null;  // Too small to split
        }
        int mid = start + currentSize / 2;
        int oldStart = start;
        start = mid;
        return new CustomSpliterator<>(data, oldStart, mid);
    }
    
    @Override
    public long estimateSize() {
        return end - start;
    }
    
    @Override
    public int characteristics() {
        return ORDERED | SIZED | SUBSIZED | IMMUTABLE;
    }
}

// Usage
List<Integer> data = IntStream.range(0, 100).boxed().toList();
StreamSupport.stream(
    new CustomSpliterator<>(data, 0, data.size()),
    true  // Parallel
).forEach(System.out::println);
```

**7. Splitting Cost:**
```java
// Different data structures have different splitting costs

// ArrayList - O(1) split cost
List<Integer> arrayList = new ArrayList<>(List.of(1,2,3,4,5,6,7,8));
// Fast split: just divide indices

// LinkedList - O(n) split cost
List<Integer> linkedList = new LinkedList<>(List.of(1,2,3,4,5,6,7,8));
// Slow split: must traverse to midpoint

// Array - O(1) split cost
int[] array = {1,2,3,4,5,6,7,8};
Arrays.stream(array).parallel();
// Fast split: array slicing

// Stream.iterate() - Cannot split efficiently
Stream.iterate(0, n -> n + 1)
    .limit(1000)
    .parallel();  // Poor parallel performance
// Must generate sequentially

// IntStream.range() - Excellent splitting
IntStream.range(0, 1000).parallel();
// Knows size, can split evenly
```

---

### Q29. What are Collectors and how do you create custom ones? 🔴

**Short Answer:**
Collectors are implementations of the `Collector` interface that accumulate Stream elements into a result container. You can create custom collectors using `Collector.of()`.

**Creating Custom Collectors:**
```java
// Custom collector to concatenate strings with delimiter
Collector<String, StringBuilder, String> joining = Collector.of(
    StringBuilder::new,                              // Supplier
    (sb, s) -> sb.append(s).append(","),           // Accumulator
    (sb1, sb2) -> sb1.append(sb2),                 // Combiner
    StringBuilder::toString,                        // Finisher
    Collector.Characteristics.CONCURRENT            // Characteristics
);

List<String> names = List.of("Alice", "Bob", "Charlie");
String result = names.stream().collect(joining);  // "Alice,Bob,Charlie,"
```

**Real-World Custom Collector:**
```java
// Collector to build immutable result
public class ImmutableListCollector {
    public static <T> Collector<T, ?, ImmutableList<T>> toImmutableList() {
        return Collector.of(
            ImmutableList.Builder<T>::new,
            ImmutableList.Builder::add,
            (b1, b2) -> b1.addAll(b2.build()),
            ImmutableList.Builder::build
        );
    }
}

// Usage
ImmutableList<String> result = stream.collect(toImmutableList());
```

---

### Q30. Explain Stream short-circuiting operations 🟡

**Short Answer:**
Short-circuiting operations can complete without processing all stream elements. Terminal operations like `findFirst()`, `findAny()`, `anyMatch()`, `allMatch()`, `noneMatch()`, and intermediate operation `limit()` are short-circuiting.

**Examples:**
```java
// anyMatch - stops at first match
boolean hasLongName = names.stream()
    .peek(s -> System.out.println("Checking: " + s))
    .anyMatch(s -> s.length() > 10);
// Stops as soon as it finds one

// allMatch - stops at first non-match
boolean allLong = names.stream()
    .allMatch(s -> s.length() > 3);
// Stops at first string <= 3 characters

// findFirst - stops after finding first element
Optional<String> first = Stream.iterate(1, n -> n + 1)
    .map(n -> "Number: " + n)
    .filter(s -> s.contains("5"))
    .findFirst();  // Stops at "Number: 5"

// limit - limits processing
List<Integer> first10 = Stream.iterate(0, n -> n + 1)
    .limit(10)  // Only processes 10 elements
    .toList();
```

---

## Custom Collectors

### Q31. How do you implement a thread-safe custom collector? 🔴

**Implementation:**
```java
public class ConcurrentSetCollector<T> {
    public static <T> Collector<T, ?, Set<T>> toConcurrentSet() {
        return Collector.of(
            ConcurrentHashMap::<T>newKeySet,  // Thread-safe set
            Set::add,
            (set1, set2) -> {
                set1.addAll(set2);
                return set1;
            },
            Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED
        );
    }
}

// Usage with parallel stream
Set<String> result = largeList.parallelStream()
    .map(String::toUpperCase)
    .collect(ConcurrentSetCollector.toConcurrentSet());
```

---

### Q32. What are downstream collectors? 🟡

**Short Answer:**
Downstream collectors are used within other collectors (like `groupingBy()` or `partitioningBy()`) to process grouped elements.

**Examples:**
```java
// Count by group
Map<String, Long> countByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::getCategory,
        Collectors.counting()  // Downstream collector
    ));

// Sum by group
Map<String, Integer> totalByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::getCategory,
        Collectors.summingInt(Product::getPrice)
    ));

// Multiple downstream collectors
Map<String, Stats> statsByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::getCategory,
        Collectors.collectingAndThen(
            Collectors.toList(),
            list -> new Stats(list.size(), calculateAverage(list))
        )
    ));
```

---

## Stream Gotchas & Edge Cases

### Q33. What happens with null elements in Streams? 🟡

**Handling Nulls:**
```java
// Problem: NPE with null elements
List<String> withNulls = Arrays.asList("a", null, "b", null, "c");

// ❌ This throws NullPointerException
withNulls.stream()
    .map(String::toUpperCase)  // NPE here!
    .toList();

// ✅ Filter nulls first
List<String> result = withNulls.stream()
    .filter(Objects::nonNull)
    .map(String::toUpperCase)
    .toList();

// ✅ Use map with null check
List<String> result = withNulls.stream()
    .map(s -> s == null ? "NULL" : s.toUpperCase())
    .toList();

// ✅ Convert to Optional
List<String> result = withNulls.stream()
    .map(Optional::ofNullable)
    .filter(Optional::isPresent)
    .map(Optional::get)
    .map(String::toUpperCase)
    .toList();
```

---

### Q34. How do you handle checked exceptions in lambda expressions? 🟡

**Problem and Solutions:**
```java
// Problem: Checked exceptions in lambdas
List<String> files = List.of("file1.txt", "file2.txt");

// ❌ Doesn't compile
files.stream()
    .map(Files::readString)  // Checked IOException
    .toList();

// ✅ Solution 1: Wrap in unchecked exception
files.stream()
    .map(file -> {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    })
    .toList();

// ✅ Solution 2: Return Optional
files.stream()
    .map(file -> {
        try {
            return Optional.of(Files.readString(Path.of(file)));
        } catch (IOException e) {
            return Optional.<String>empty();
        }
    })
    .flatMap(Optional::stream)
    .toList();

// ✅ Solution 3: Generic wrapper
@FunctionalInterface
interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}

static <T, R> Function<T, R> unchecked(CheckedFunction<T, R> f) {
    return t -> {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}

// Usage
files.stream()
    .map(unchecked(path -> Files.readString(Path.of(path))))
    .toList();
```

---

### Q35. What are the performance implications of boxing/unboxing in Streams? 🔴

**Problem:**
```java
// ❌ Boxing overhead
List<Integer> numbers = getIntegers();
int sum = numbers.stream()
    .reduce(0, Integer::sum);  // Boxing on each operation
// Each integer boxed/unboxed multiple times

// ✅ Use primitive streams
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();
// No boxing overhead

// Performance comparison
// Boxed: ~500ms for 10M elements
// Primitive: ~50ms for 10M elements (10x faster)
```

**When to Use Primitive Streams:**
```java
// IntStream, LongStream, DoubleStream
IntStream.range(0, 1000)
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .sum();

// Specialized operations
IntSummaryStatistics stats = IntStream.range(0, 100)
    .summaryStatistics();

stats.getCount();
stats.getSum();
stats.getMin();
stats.getMax();
stats.getAverage();
```

---

### Q36. How do infinite Streams work and when should you use them? 🟡

**Creating Infinite Streams:**
```java
// 1. Stream.generate()
Stream<Double> randoms = Stream.generate(Math::random);
Stream<String> constants = Stream.generate(() -> "Hello");

// Must use limit()
List<Double> randomList = randoms.limit(10).toList();

// 2. Stream.iterate()
Stream<Integer> numbers = Stream.iterate(0, n -> n + 1);
Stream<Integer> evens = Stream.iterate(0, n -> n + 2);

// Java 9+: iterate with condition
Stream<Integer> numbersUpTo100 = Stream.iterate(0, n -> n < 100, n -> n + 1);

// 3. Fibonacci sequence
Stream<Long> fibonacci = Stream.iterate(
    new long[]{0, 1},
    f -> new long[]{f[1], f[0] + f[1]}
).map(f -> f[0]);

List<Long> first10 = fibonacci.limit(10).toList();
```

**Use Cases:**
```java
// Generate test data
Stream.generate(() -> UUID.randomUUID().toString())
    .limit(1000)
    .forEach(id -> database.insert(id));

// Continuous data processing
Stream.generate(sensor::readData)
    .filter(data -> data.isValid())
    .peek(this::processData)
    .limit(1000000)
    .count();

// ⚠️ Warning: Always use limit() or short-circuit operation
// ❌ This never terminates!
Stream.iterate(0, n -> n + 1)
    .forEach(System.out::println);
```

---

### Q37. What's the difference between sorted() with Comparator and using TreeSet? 🟡

**Comparison:**
```java
List<String> names = List.of("Charlie", "Alice", "Bob");

// 1. Stream sorted() - Temporary sorting
List<String> sorted = names.stream()
    .sorted()
    .toList();
// Original list unchanged
// O(n log n) for each sort operation

// 2. TreeSet - Maintains sorted order
Set<String> sortedSet = new TreeSet<>(names);
// Always sorted
// O(log n) for add operations
// O(1) for getting first/last

// When to use sorted():
// - One-time sorting
// - Don't need to maintain order
// - Working with existing collection

// When to use TreeSet:
// - Need constant sorted access
// - Adding elements over time
// - Need unique elements
```

---

### Q38. How do you debug complex Stream pipelines? 🟡

**Debugging Techniques:**
```java
// 1. Use peek() to inspect intermediate values
List<String> result = names.stream()
    .peek(name -> System.out.println("Original: " + name))
    .filter(name -> name.length() > 3)
    .peek(name -> System.out.println("After filter: " + name))
    .map(String::toUpperCase)
    .peek(name -> System.out.println("After map: " + name))
    .toList();

// 2. Break into steps
Stream<String> filtered = names.stream()
    .filter(name -> name.length() > 3);
System.out.println("Filtered count: " + filtered.count());

Stream<String> mapped = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase);
System.out.println("Mapped: " + mapped.toList());

// 3. Extract predicates/functions for testing
Predicate<String> longName = name -> name.length() > 3;
Function<String, String> upper = String::toUpperCase;

// Test separately
assert longName.test("Alice") == true;
assert upper.apply("hello").equals("HELLO");

// Use in stream
names.stream()
    .filter(longName)
    .map(upper)
    .toList();

// 4. IDE Debugger Stream support
// Most IDEs can debug streams step-by-step
// Set breakpoint and use "Step into" to see each operation
```

---

### Q39. What are the memory implications of Streams? 🔴

**Memory Considerations:**
```java
// 1. Streams don't store elements
Stream<String> stream = list.stream();  // No extra memory

// 2. Intermediate operations are lazy (no memory)
Stream<String> filtered = stream
    .filter(predicate)   // Lazy, no memory
    .map(function);      // Lazy, no memory

// 3. Terminal operations may store results
List<String> result = filtered.toList();  // Stores result

// 4. Stateful operations buffer elements
Stream<Integer> sorted = numbers.stream()
    .sorted();  // Must buffer all elements to sort
// Memory: O(n)

Stream<Integer> distinct = numbers.stream()
    .distinct();  // Must remember seen elements
// Memory: O(unique elements)

// 5. Parallel streams use more memory
// Each thread may have its own buffers

// 6. Infinite streams with stateful operations
Stream.iterate(0, n -> n + 1)
    .distinct()  // Tries to store all numbers - OutOfMemoryError!
    .limit(100)
    .toList();

// ✅ Correct order
Stream.iterate(0, n -> n + 1)
    .limit(100)   // Limit first
    .distinct()   // Then distinct
    .toList();
```

---

### Q40. How do you chain multiple filters efficiently? 🟡

**Filter Ordering:**
```java
List<User> users = getUsers();

// ❌ Inefficient: Expensive filters first
List<User> result = users.stream()
    .filter(user -> expensiveDatabaseCheck(user))  // Slow
    .filter(user -> user.getAge() > 18)             // Fast
    .filter(user -> user.getName() != null)         // Fast
    .toList();

// ✅ Efficient: Cheap filters first
List<User> result = users.stream()
    .filter(user -> user.getName() != null)         // Fast
    .filter(user -> user.getAge() > 18)             // Fast
    .filter(user -> expensiveDatabaseCheck(user))  // Slow (fewer elements)
    .toList();

// Principle: Order filters from:
// 1. Cheapest to most expensive
// 2. Most selective to least selective

// Example with predicates
Predicate<User> notNull = user -> user != null;
Predicate<User> adult = user -> user.getAge() >= 18;
Predicate<User> active = user -> user.isActive();
Predicate<User> verified = user -> verifyUser(user);  // Expensive

// Optimal order
users.stream()
    .filter(notNull)    // Eliminate nulls first
    .filter(adult)      // Simple field check
    .filter(active)     // Another field check
    .filter(verified)   // Expensive check last
    .toList();
```

---

### Q41. What's the difference between forEach() and forEachOrdered()? 🟡

**Key Differences:**
```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// forEach() - No order guarantee in parallel
numbers.parallelStream()
    .forEach(n -> System.out.println(n));
// Output: 3, 1, 5, 2, 4 (random order)

// forEachOrdered() - Maintains encounter order even in parallel
numbers.parallelStream()
    .forEachOrdered(n -> System.out.println(n));
// Output: 1, 2, 3, 4, 5 (always in order)

// Performance impact
// forEach() - Fast in parallel
// forEachOrdered() - Slower in parallel (must coordinate)

// When to use forEach():
// - Order doesn't matter
// - Parallel processing
// - Maximum performance

// When to use forEachOrdered():
// - Order is important
// - Sequential-like behavior needed
```

---

### Q42. How do you convert between Stream types? 🟢

**Stream Conversions:**
```java
// 1. Object Stream to Primitive Stream
Stream<Integer> integerStream = Stream.of(1, 2, 3);
IntStream intStream = integerStream.mapToInt(Integer::intValue);

// 2. Primitive Stream to Object Stream
IntStream primitiveStream = IntStream.range(0, 10);
Stream<Integer> boxedStream = primitiveStream.boxed();

// 3. Between primitive types
IntStream intStream = IntStream.range(0, 10);
LongStream longStream = intStream.asLongStream();
DoubleStream doubleStream = intStream.asDoubleStream();

// 4. Stream to Collection
List<String> list = stream.toList();
Set<String> set = stream.collect(Collectors.toSet());
Map<String, Integer> map = stream.collect(
    Collectors.toMap(String::toLowerCase, String::length));

// 5. Collection to Stream
List<String> list = List.of("a", "b", "c");
Stream<String> stream = list.stream();
Stream<String> parallelStream = list.parallelStream();

// 6. Array to Stream
String[] array = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(array);
IntStream intStream = Arrays.stream(new int[]{1, 2, 3});
```

---

### Q43. What are the best practices for Stream resource management? 🟡

**Resource Management:**
```java
// 1. Streams from IO resources need closing
try (Stream<String> lines = Files.lines(Path.of("file.txt"))) {
    lines.filter(line -> !line.isEmpty())
         .forEach(System.out::println);
}  // Auto-closes

// 2. Don't store IO streams
// ❌ Bad
Stream<String> lines = Files.lines(Path.of("file.txt"));
lines.forEach(System.out::println);
// File not closed!

// ✅ Good
try (Stream<String> lines = Files.lines(Path.of("file.txt"))) {
    lines.forEach(System.out::println);
}

// 3. BufferedReader streams
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    reader.lines()
          .forEach(System.out::println);
}

// 4. Parallel streams with IO
// ❌ Don't use parallel for IO operations
try (Stream<String> lines = Files.lines(Path.of("file.txt"))) {
    lines.parallel()  // Bad for IO
         .forEach(this::processLine);
}

// ✅ Process in parallel after reading
List<String> allLines = Files.readAllLines(Path.of("file.txt"));
allLines.parallelStream()
        .forEach(this::processLine);
```

---

### Q44. How do you handle Stream pipeline errors gracefully? 🔴

**Error Handling Strategies:**
```java
// Strategy 1: Collect errors separately
public record Result<T>(T value, Exception error) {
    public boolean isSuccess() { return error == null; }
}

List<Result<String>> results = urls.stream()
    .map(url -> {
        try {
            return new Result<>(fetchUrl(url), null);
        } catch (Exception e) {
            return new Result<>(null, e);
        }
    })
    .toList();

// Separate success and failures
List<String> successes = results.stream()
    .filter(Result::isSuccess)
    .map(Result::value)
    .toList();

List<Exception> failures = results.stream()
    .filter(r -> !r.isSuccess())
    .map(Result::error)
    .toList();

// Strategy 2: Log and continue
List<String> results = urls.stream()
    .map(url -> {
        try {
            return fetchUrl(url);
        } catch (Exception e) {
            log.error("Failed to fetch: " + url, e);
            return null;
        }
    })
    .filter(Objects::nonNull)
    .toList();

// Strategy 3: Fail fast
try {
    List<String> results = urls.stream()
        .map(url -> {
            try {
                return fetchUrl(url);
            } catch (Exception e) {
                throw new RuntimeException("Failed at: " + url, e);
            }
        })
        .toList();
} catch (RuntimeException e) {
    // Handle failure
}

// Strategy 4: Optional for nullable results
List<String> results = urls.stream()
    .map(url -> {
        try {
            return Optional.of(fetchUrl(url));
        } catch (Exception e) {
            return Optional.<String>empty();
        }
    })
    .flatMap(Optional::stream)
    .toList();
```

---

### Q45. What are Stream.Builder and when should you use it? 🟡

**Stream.Builder Usage:**
```java
// Creating streams dynamically
Stream.Builder<String> builder = Stream.builder();

// Add elements conditionally
if (condition1) builder.add("value1");
if (condition2) builder.add("value2");
builder.add("value3");

// Build and use
Stream<String> stream = builder.build();
List<String> result = stream.toList();

// Real-world example
public Stream<LogEntry> buildLogStream(LocalDate date, Level minLevel) {
    Stream.Builder<LogEntry> builder = Stream.builder();
    
    // Add entries based on conditions
    for (LogEntry entry : getAllLogs()) {
        if (entry.getDate().equals(date) && 
            entry.getLevel().compareTo(minLevel) >= 0) {
            builder.add(entry);
        }
    }
    
    return builder.build();
}

// Note: Usually better to use stream operations
// Builder is mainly for building streams incrementally
// when you can't use stream() or of()
```

---

**📘 Continue to:** [03-JAVA11-17-FEATURES.md](03-JAVA11-17-FEATURES.md)
