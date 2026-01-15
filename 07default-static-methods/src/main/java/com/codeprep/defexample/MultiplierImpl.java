package com.codeprep.defexample;

import java.util.List;

public class MultiplierImpl implements Multiplier {

    @Override
    public int size(List<Integer> integerList){
        System.out.println("The overridden default method");
        return integerList.size();
    }

    @Override
    public int multiply(List<Integer> integerList) {
        return integerList.stream().reduce(1, (x, y) -> x * y);
    }
}
