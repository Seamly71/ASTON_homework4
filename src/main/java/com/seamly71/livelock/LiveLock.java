package com.seamly71.livelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLock {

    public static void main(String[] args) {
        Lock juiceLock = new ReentrantLock();
        Lock cupLock = new ReentrantLock();

        new Thread(() -> aliceGetJuice(juiceLock, cupLock)).start();
        new Thread(() -> bobGetJuice(juiceLock, cupLock)).start();
    }

    private static void aliceGetJuice(
            Lock juiceLock,
            Lock cupLock
    ) {
        while (true) {
            if (juiceLock.tryLock()) {
                System.out.println("Алиса взяла графин с соком");
                delay(700);

                if (cupLock.tryLock()) {
                    System.out.println("Успех! Алиса налила себе сок");
                    juiceLock.unlock();
                    cupLock.unlock();
                    return;
                }

                juiceLock.unlock();
                System.out.println("Алиса вернула графин.");
            }
        }
    }

    private static void bobGetJuice(
            Lock juiceLock,
            Lock cupLock
    ) {
        while (true) {
            if (cupLock.tryLock()) {
                System.out.println("Боб взял стаканы");
                delay(1300);

                if (juiceLock.tryLock()) {
                    System.out.println("Успех! Боб налил себе сок");
                    juiceLock.unlock();
                    cupLock.unlock();
                    return;
                }

                cupLock.unlock();
                System.out.println("Боб вернул стаканы.");
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
}
