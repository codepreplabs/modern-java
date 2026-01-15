package com.codeprep.lambda;

public class RunnableLambdaExample {

    public static void main(String[] args) {

        // prior to java 8

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable example prior to Java 8");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        // using lambda expression in java 8 and later
        Runnable lambdaRunnable = () -> System.out.println("Runnable example using Lambda in Java 8 and later");
        Thread lambdaThread = new Thread(lambdaRunnable);
        lambdaThread.start();

        // or even more concise
        Thread conciseLambdaThread = new Thread(() -> System.out.println("Concise Runnable example using Lambda"));
        conciseLambdaThread.start();
    }
}
