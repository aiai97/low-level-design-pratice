package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicReadOrderDemo {

    static class WriteTask implements Callable<Void> {

        private final String filename;
        private final int index;

        public WriteTask(String filename, int index) {
            this.filename = filename;
            this.index = index;
        }

        @Override
        public Void call() throws Exception {
            Path file = Paths.get("files", filename);

            Files.createDirectories(file.getParent());

            Files.writeString(
                    file,
                    "content of " + filename + "\n"
            );

            System.out.println(
                    Thread.currentThread().getName()
                            + " wrote " + filename
            );

            return null;
        }
    }

    static class ReadTask implements Callable<String> {

        private final String filename;
        private final int index;
        private final AtomicInteger nextIndex;

        public ReadTask(
                String filename,
                int index,
                AtomicInteger nextIndex) {

            this.filename = filename;
            this.index = index;
            this.nextIndex = nextIndex;
        }

        @Override
        public String call() throws Exception {

            // Wait until it is my turn
            while (nextIndex.get() != index) {
                Thread.yield();
            }

            Path file = Paths.get("files", filename);

            String content = Files.readString(file);

            System.out.println(
                    Thread.currentThread().getName()
                            + " read " + filename
            );

            // Allow the next task to read
            nextIndex.incrementAndGet();

            return content;
        }
    }

    public static void main(String[] args) throws Exception {

        // Fixed thread pool
        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        // -------------------------
        // Phase 1: Write
        // -------------------------

        List<Callable<Void>> writeTasks = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            writeTasks.add(
                    new WriteTask(
                            "test" + i + ".txt",
                            i
                    )
            );
        }

        // Writing can happen in any order
        List<Future<Void>> writeResults =
                executor.invokeAll(writeTasks);

        // Make sure all writes are finished
        for (Future<Void> future : writeResults) {
            future.get();
        }

        // -------------------------
        // Phase 2: Read
        // -------------------------

        AtomicInteger nextIndex =
                new AtomicInteger(0);

        List<Callable<String>> readTasks = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            readTasks.add(
                    new ReadTask(
                            "test" + i + ".txt",
                            i,
                            nextIndex
                    )
            );
        }

        // Read tasks can be submitted concurrently,
        // but AtomicInteger controls their execution order.
        List<Future<String>> readResults =
                executor.invokeAll(readTasks);

        // Collect results
        for (Future<String> future : readResults) {
            System.out.print(future.get());
        }

        executor.shutdown();
    }
}