package com.seamly71.consolespammer;

import java.util.concurrent.Semaphore;

public class ConsoleSpammer {

    public static void main(String[] args) {
        Semaphore oneSemaphore = new Semaphore(1);
        Semaphore twoSemaphore = new Semaphore(1);

        Thread oneThread = new Thread(
                () -> spam("1", oneSemaphore, twoSemaphore)
        );
        Thread twoThread = new Thread(
                () -> spam("2", twoSemaphore, oneSemaphore)
        );

        twoSemaphore.acquireUninterruptibly();
        oneThread.start();
        twoThread.start();

        loopJoin(oneThread);
        loopJoin(twoThread);
    }

    private static void spam(String str, Semaphore thisSemaphore, Semaphore anotherSemaphore) {
        while (true) {
            thisSemaphore.acquireUninterruptibly();
            System.out.println(str);
            anotherSemaphore.release();
        }
    }

    private static void loopJoin(Thread thread) {
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                continue;
            }
            break;
        }
    }
}
