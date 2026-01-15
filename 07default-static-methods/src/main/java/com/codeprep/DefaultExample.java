package com.codeprep;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultExample {

    public static void main(String[] args) {

        /*
        * Sort a list of names alphabetically*/

        List<String> names = Arrays.asList("Alice", "Charlie", "Bob", "Diana", "Eve");

        // before java 8
        Collections.sort(names);
        System.out.println(names);

        // jave 8
        // sort is a default method present in the interface
        names.sort(String::compareTo);
        System.out.println(names);

        // reverseOrder is a static utility method in the Comparator interface
        names.sort(Comparator.reverseOrder());
        System.out.println(names);
    }
}
