package com.codeprep.methodreference;

/*
* Lambdas helped us to move away from creating anonymous inner classes.
* Yes, you are right, but this one is going to simplify the lambda expression itself.
* And also call method reference as a shortcut for the lambda expressions from the name itself.
* It is clear that this one is used to refer a method in a class.
*
* Syntax:
*
* className::instance-methodName
* className::static-methodName
* Instance::methodName
* */

import java.util.function.Function;

public class MethodReference {

    private final static Function<String, String> toUpperCaseUsingMethodReference = String::toUpperCase;
    private final static Function<String, String> toUpperCaseLambda = (s) -> s.toUpperCase();

    public static void main(String[] args) {
        System.out.println(toUpperCaseLambda.apply("Java"));
        System.out.println(toUpperCaseUsingMethodReference.apply("Java"));
    }
}