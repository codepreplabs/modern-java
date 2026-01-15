package com.codeprep.multipleinheritence;

public interface InterfaceOne {

    default void methodA(){
        System.out.println("Inside method A");
    }
}
