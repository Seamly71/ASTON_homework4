package com.seamly71.deadlock;

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
        for (String direction : lockByDirection.keySet()) {
            new Thread(() -> crossIntersection(direction)).start();
        }
        System.out.println("yay");
    }

    private static void crossIntersection(String direction) {
        String blockingDirection = blockingMap.get(direction);
        Lock tangentialLock = lockByDirection.get(blockingDirection);

        tangentialLock.lock();
        System.out.println(String.format(
                "Выехал на перекресток в направлении %s, блокирую %s", direction, blockingDirection
        ));

        drive();
        System.out.println(String.format(
                "Хочу ехать в направлении %s", direction
        ));

        Lock straightLock = lockByDirection.get(direction);
        straightLock.lock();
        tangentialLock.unlock();
        System.out.println(String.format("Освободил %s", blockingDirection));

        straightLock.unlock();
        System.out.println(String.format(
                "Проехал перекресток в направлении %s", direction
        ));
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

}
