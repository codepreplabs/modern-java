package com.codeprep;

import java.util.function.Supplier;
import java.util.stream.IntStream;

/*
 * Parallel Streams in Java:
 *
 * Parallel streams allow processing of stream elements concurrently using multiple threads.
 * They leverage the Fork/Join framework to split the work across available CPU cores.
 *
 * Key Points:
 * - Use .parallelStream() on collections or .parallel() on streams to enable parallel processing
 * - Best suited for CPU-intensive operations on large datasets
 * - Order of execution is non-deterministic
 * - May not always be faster than sequential streams due to thread overhead
 * - Avoid using parallel streams with stateful operations or shared mutable state
 */
public class ParallelStreamExample   {

    public static long checkPerformanceResult(Supplier<Integer> sum, int numberOfTimes){

        long start = System.currentTimeMillis();
        for(int i = 0; i < numberOfTimes; i++){
            sum.get();
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static int sumSequentialStream(){
        return IntStream.rangeClosed(1, 1000000)
                .sum();
    }

    public static int sumParallelStream(){
        return IntStream.rangeClosed(1, 1000000)
                .parallel()
                .sum();
    }

    public static void main(String[] args) {

        System.out.println("Available processes: "+ Runtime.getRuntime().availableProcessors());
        long sequentialDuration = checkPerformanceResult(ParallelStreamExample::sumSequentialStream, 10);
        long parallelDuration = checkPerformanceResult(ParallelStreamExample::sumParallelStream, 20);

        System.out.println("sequential processing in duration: "+ sequentialDuration);
        System.out.println("parallel processing in duration: "+ parallelDuration);
    }
}