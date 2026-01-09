package com.seamly71.livelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLock {

    public static void main(String[] args) {
        Lock juiceLock = new ReentrantLock();
        Lock cupLock = new ReentrantLock();
        Thread aliceThread = new Thread(() -> getJuice(
                juiceLock,
                "графин с соком",
                700,
                cupLock,
                "Алиса"
        ));
        Thread bobThread = new Thread(() -> getJuice(
                cupLock,
                "стаканы",
                1300,
                juiceLock,
                "Боб"
        ));

        aliceThread.start();
        bobThread.start();
        loopJoin(aliceThread);
        loopJoin(bobThread);
    }

    private static void getJuice(
            Lock firstItemLock,
            String firstItemName,
            int delay,
            Lock secondItemLock,
            String person
    ) {
        while (true) {
            if (firstItemLock.tryLock()) {
                System.out.printf("%s взял(а) %s.%n", person, firstItemName);
                delay(delay);

                if (secondItemLock.tryLock()) {
                    System.out.printf("Успех! %s налил(а) себе сок.%n", person);
                    firstItemLock.unlock();
                    secondItemLock.unlock();
                    return;
                }

                firstItemLock.unlock();
                System.out.printf("%s вернул(а) %s.%n", person, firstItemName);
            }
        }
    }

    private static void delay(int mills) {
        while (true) {
            try {
                Thread.sleep(mills);
            } catch (InterruptedException exception) {
                continue;
            }
            break;
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
