package com.seamly71.consolespammer;

import java.util.concurrent.Semaphore;

public class ConsoleSpammer {

    public static void main(String[] args) {
        Semaphore oneSemaphore = new Semaphore(1);
        Semaphore twoSemaphore = new Semaphore(1);

        twoSemaphore.acquireUninterruptibly();
        new Thread(
                () -> spam("1", oneSemaphore, twoSemaphore)
        ).start();
        new Thread(
                () -> spam("2", twoSemaphore, oneSemaphore)
        ).start();
    }

    private static void spam(String str, Semaphore thisSemaphore, Semaphore anotherSemaphore) {
        while (true) {
            thisSemaphore.acquireUninterruptibly();
            System.out.println(str);
            anotherSemaphore.release();
        }
    }
}
