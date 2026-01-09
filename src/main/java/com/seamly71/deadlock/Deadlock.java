package com.seamly71.deadlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {

    private static final Map<String, Lock> lockByDirection = Map.of(
            "north", new ReentrantLock(),
            "south", new ReentrantLock(),
            "east", new ReentrantLock(),
            "west", new ReentrantLock()
            );
    private static final Map<String, String> blockingMap = Map.of(
            "north", "east",
            "south", "west",
            "east", "south",
            "west", "north"
    );


    public static void main(String[] args) {
        Map<String, Thread> threadByDirection = new HashMap<>();

        for (String direction : lockByDirection.keySet()) {
            threadByDirection.put(
                    direction,
                    new Thread(() -> crossIntersection(direction))
            );
        }

        for (Thread thread : threadByDirection.values()) {
            thread.start();
        }

        for (Thread thread : threadByDirection.values()) {
            loopJoin(thread);
        }
    }

    private static void crossIntersection(String direction) {
        String blockingDirection = blockingMap.get(direction);
        Lock tangentialLock = lockByDirection.get(blockingDirection);

        tangentialLock.lock();
        System.out.printf(
                "Выехал на перекресток в направлении %s, блокирую %s%n",
                direction,
                blockingDirection
        );

        drive();
        System.out.printf(
                "Хочу ехать в направлении %s%n", direction
        );

        Lock straightLock = lockByDirection.get(direction);
        straightLock.lock();
        tangentialLock.unlock();
        System.out.printf("Освободил %s%n", blockingDirection);

        straightLock.unlock();
        System.out.printf(
                "Проехал перекресток в направлении %s%n", direction
        );
    }

    private static void drive() {
        while (true) {
            try {
                Thread.sleep(3000);
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
