package com.codeprep.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/*
 * Collections vs Streams:
 *
 * Collections:
 * - Data structure that stores and groups data in memory
 * - Eagerly constructed - all elements must be computed before use
 * - Can be traversed multiple times
 * - Allows element modification (add, remove, update)
 * - External iteration using loops (for, while, forEach)
 * - Focuses on efficient storage and access of data
 *
 * Streams:
 * - Sequence of elements supporting sequential and parallel operations
 * - Lazily constructed - computed on demand
 * - Can only be traversed once (consumed after terminal operation)
 * - Does not store data - operates on source data (Collection, array, etc.)
 * - Internal iteration - handled by the Stream API
 * - Focuses on computations and transformations using functional operations
 * - Supports operations like filter, map, reduce, collect
 * - Can be parallelized easily for performance
 *
 * Example: Collection is like a DVD with stored movies,
 *          Stream is like Netflix streaming movies on demand
 */
public class CollectionsVsStream {

    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        names.add("Jim");
        names.add("Jane");
        names.add("Jenny");

        // cannot iterate a stream twice
        Stream<String> nameStream = names.stream();
        nameStream.forEach(System.out::println);
        nameStream.forEach(System.out::println);
    }
}
