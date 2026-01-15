package com.codeprep.defexample;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiplierClient {

    public static void main(String[] args) {

        Multiplier multiplier = new MultiplierImpl();
        List<Integer> integerList = Arrays.asList(1, 3, 5);
        System.out.println(multiplier.multiply(integerList));

        System.out.println(multiplier.size(integerList));

        System.out.println(Multiplier.isEmpty(Collections.emptyList()));

    }
}
