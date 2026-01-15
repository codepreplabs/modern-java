package com.codeprep.streams.numeric;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class NumericStreams {

    public static void main(String[] args) {

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println(integers.stream().reduce(0, Integer::sum));

        System.out.println(IntStream.range(1, 6).sum());

        // double stream
        IntStream.range(1, 50).asDoubleStream().forEach((value -> System.out.print(value + ",")));
        System.out.println();

        // sum
        System.out.println(IntStream.rangeClosed(1, 50).sum());

        // max
        System.out.println(IntStream.rangeClosed(1, 50).max());

        // min
        System.out.println(IntStream.rangeClosed(1, 50).min());

        // average
        OptionalDouble optionalDouble = IntStream.rangeClosed(1, 50).average();
        System.out.println(optionalDouble.isPresent() ? optionalDouble.getAsDouble() : 0);

        // Boxing
        List<Integer> integerList = IntStream.rangeClosed(1, 10)
                .boxed()
                .toList();
        System.out.println(integerList);

        // UnBoxing
        System.out.println(integerList.stream().mapToInt(Integer::valueOf).sum());

        // mapToObject
        List<Integer> integerList1 = IntStream.rangeClosed(1, 5)
                .mapToObj((i) -> i)
                .toList();

        // mapToLong
        long sum = IntStream.rangeClosed(1, 5)
                .mapToLong((i) -> i)
                .sum();
        System.out.println(sum);

        // mapToDouble
        double doubleSum = IntStream.rangeClosed(1, 5)
                .mapToDouble((i) -> i)
                .sum();
        System.out.println(doubleSum);
    }
}
