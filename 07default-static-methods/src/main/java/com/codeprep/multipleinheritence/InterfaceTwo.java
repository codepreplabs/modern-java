package com.codeprep.multipleinheritence;

public interface InterfaceTwo extends InterfaceOne{

    default void methodB(){
        System.out.println("Inside method A");
    }

    @Override
    default void methodA(){
        System.out.println("Inside method A " + this.getClass().getName());
    }
}
