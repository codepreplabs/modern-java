package com.codeprep.lambda;

public class ComparatorLambdaExample {

    public static void main(String[] args) {

        // prior to java 8

        java.util.List<String> names = java.util.Arrays.asList("John", "Alice", "Bob", "Diana");

        names.sort(new java.util.Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        System.out.println("Sorted names prior to Java 8: " + names);

        // using lambda expression in java 8 and later
        names.sort((s1, s2) -> s1.compareTo(s2));

        System.out.println("Sorted names using Lambda in Java 8 and later: " + names);

        // or even more concise using method reference
        names.sort(String::compareTo);

        System.out.println("Sorted names using Method Reference: " + names);
    }
}
