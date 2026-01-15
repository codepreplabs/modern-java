package com.codeprep.streams.factorymethods;

/**
 * Streams Factory Methods in Java
 *
 * The Stream API provides several factory methods to create streams:
 *
 * 1. Stream.of(T... values) - Creates a stream from specified values
 *    Example: Stream.of(1, 2, 3, 4, 5)
 *
 * 2. Stream.empty() - Creates an empty stream
 *    Example: Stream.empty()
 *
 * 3. Stream.generate(Supplier<T>) - Creates an infinite stream using a supplier
 *    Example: Stream.generate(() -> Math.random())
 *
 * 4. Stream.iterate(T seed, UnaryOperator<T>) - Creates an infinite stream by iteratively applying a function
 *    Example: Stream.iterate(0, n -> n + 1)
 *
 * 5. Stream.iterate(T seed, Predicate<T>, UnaryOperator<T>) - Creates a finite stream with a condition (Java 9+)
 *    Example: Stream.iterate(0, n -> n < 10, n -> n + 1)
 *
 * 6. Stream.concat(Stream<T>, Stream<T>) - Concatenates two streams
 *    Example: Stream.concat(stream1, stream2)
 *
 * 7. Stream.builder() - Creates a stream builder for adding elements
 *    Example: Stream.builder().add(1).add(2).build()
 *
 * 8. Arrays.stream(T[]) - Creates a stream from an array
 *    Example: Arrays.stream(new int[]{1, 2, 3})
 *
 * 9. Collection.stream() - Creates a stream from a collection
 *    Example: list.stream()
 *
 * 10. IntStream.range(int, int) / IntStream.rangeClosed(int, int) - Creates numeric streams
 *     Example: IntStream.range(1, 10)
 */
public class StreamsFactoryMethods {

    public static void main(String[] args) {

        // 1. Stream.of() - Create stream from values
        System.out.println("1. Stream.of():");
        java.util.stream.Stream.of(1, 2, 3, 4, 5)
                .forEach(System.out::println);

        // 2. Stream.empty() - Create empty stream
        System.out.println("\n2. Stream.empty():");
        java.util.stream.Stream.empty()
                .forEach(System.out::println); // Prints nothing

        // 3. Stream.generate() - Infinite stream (limit to 5)
        System.out.println("\n3. Stream.generate():");
        java.util.stream.Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        // 4. Stream.iterate() - Infinite stream (limit to 5)
        System.out.println("\n4. Stream.iterate() - infinite:");
        java.util.stream.Stream.iterate(0, n -> n + 2)
                .limit(5)
                .forEach(System.out::println);

        // 5. Stream.iterate() with predicate - Finite stream
        System.out.println("\n5. Stream.iterate() - finite:");
        java.util.stream.Stream.iterate(0, n -> n < 10, n -> n + 2)
                .forEach(System.out::println);

        // 6. Stream.concat() - Concatenate streams
        System.out.println("\n6. Stream.concat():");
        java.util.stream.Stream<String> stream1 = java.util.stream.Stream.of("A", "B", "C");
        java.util.stream.Stream<String> stream2 = java.util.stream.Stream.of("D", "E", "F");
        java.util.stream.Stream.concat(stream1, stream2)
                .forEach(System.out::println);

        // 7. Stream.builder() - Build stream dynamically
        System.out.println("\n7. Stream.builder():");
        java.util.stream.Stream.builder()
                .add("Apple")
                .add("Banana")
                .add("Cherry")
                .build()
                .forEach(System.out::println);

        // 8. Arrays.stream() - Stream from array
        System.out.println("\n8. Arrays.stream():");
        int[] numbers = {10, 20, 30, 40, 50};
        java.util.Arrays.stream(numbers)
                .forEach(System.out::println);

        // 9. Collection.stream() - Stream from collection
        System.out.println("\n9. Collection.stream():");
        java.util.List<String> list = java.util.Arrays.asList("Java", "Python", "JavaScript");
        list.forEach(System.out::println);

        // 10. IntStream.range() and rangeClosed()
        System.out.println("\n10a. IntStream.range(1, 5):");
        java.util.stream.IntStream.range(1, 5)
                .forEach(System.out::println); // 1,2,3,4 (excludes 5)

        System.out.println("\n10b. IntStream.rangeClosed(1, 5):");
        java.util.stream.IntStream.rangeClosed(1, 5)
                .forEach(System.out::println); // 1,2,3,4,5 (includes 5)
    }
}
