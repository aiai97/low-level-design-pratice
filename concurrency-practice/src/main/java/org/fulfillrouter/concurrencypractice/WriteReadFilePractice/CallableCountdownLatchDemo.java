package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
// 1, won't use completable future join grammer, and will use countdownlatch to replace it
public class CallableCountdownLatchDemo {

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                );

        try {
            // 1. Create files
            CountDownLatch writeLatch = new CountDownLatch(5);

            for (int i = 0; i < 5; i++) {
                String filename = "test" + i + ".txt";
                Path file = Paths.get("files", filename);
                int finalI = i;

                executor.submit(() -> {
                    try {
                        Files.createDirectories(file.getParent());

                        Files.writeString(
                                file,
                                "line" + finalI + "\n"
                        );

                    } catch (IOException e) {
                        throw new RuntimeException(e);

                    } finally {
                        writeLatch.countDown();
                    }
                });
            }

            // 2. Wait until all files have been written
            writeLatch.await();

            // 3. Read files asynchronously
            CountDownLatch readLatch = new CountDownLatch(5);

            List<Future<String>> readTasks = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                String filename = "test" + i + ".txt";

                Future<String> future = executor.submit(() -> {
                    try {
                        Path file = Paths.get("files", filename);
                        return Files.readString(file);
                    } finally {
                        readLatch.countDown();
                    }
                });

                readTasks.add(future);
            }

            // 4. Wait until all files have been read
            readLatch.await();

            // 5. Collect results
            for (Future<String> future : readTasks) {
                System.out.println(
                        "File content: " + future.get()
                );
            }

        } finally {
            executor.shutdown();
        }
    }
}