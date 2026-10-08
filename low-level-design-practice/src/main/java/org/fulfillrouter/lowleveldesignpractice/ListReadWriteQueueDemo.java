package org.fulfillrouter.lowleveldesignpractice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;


public class ListReadWriteQueueDemo {

    public static void main(String[] args) throws Exception {
        testSynchronizedList();
        testCopyOnWriteArrayList();
        testConcurrentLinkedQueue();
    }

    // 1. synchronizedList
    private static void testSynchronizedList() throws Exception {
        System.out.println("\n===== synchronizedList =====");

        List<Integer> list =
                Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        Thread reader = new Thread(() -> {
            synchronized (list) {
                for (Integer value : list) {
                    System.out.println("reader: " + value);
                    sleep(100);
                }
            }
        });

        Thread writer = new Thread(() -> {
            sleep(200);
            System.out.println("writer: add 100");
            list.add(100);
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("final: " + list);
    }

    // 2. CopyOnWriteArrayList
    private static void testCopyOnWriteArrayList() throws Exception {
        System.out.println("\n===== CopyOnWriteArrayList =====");

        List<Integer> list =
                new CopyOnWriteArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        Thread reader = new Thread(() -> {
            for (Integer value : list) {
                System.out.println("reader: " + value);
                sleep(100);
            }
        });

        Thread writer = new Thread(() -> {
            sleep(200);
            System.out.println("writer: add 100");
            list.add(100);
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("final: " + list);
    }

    // 3. ConcurrentLinkedQueue
    private static void testConcurrentLinkedQueue() throws Exception {
        System.out.println("\n===== ConcurrentLinkedQueue =====");

        Queue<Integer> queue =
                new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 10; i++) {
            queue.add(i);
        }

        Thread reader = new Thread(() -> {
            for (Integer value : queue) {
                System.out.println("reader: " + value);
                sleep(100);
            }
        });

        Thread writer = new Thread(() -> {
            sleep(200);
            System.out.println("writer: add 100");
            queue.add(100);
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("final: " + queue);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}