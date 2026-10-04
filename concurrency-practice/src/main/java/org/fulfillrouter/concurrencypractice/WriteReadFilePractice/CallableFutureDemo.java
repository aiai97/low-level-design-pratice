package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            // 1. Create files
            List<Future<?>> writeFutures = new ArrayList<>();

            for(int i = 0; i < 5; i++) {
                String filename = "test" + i + ".txt";
                Path file = Paths.get("files", filename);
                int finalI = i;
                Runnable writer = () -> {
                    try {
                        Files.createDirectories(file.getParent());
                        Files.writeString(file, "line" + finalI + "\n");
                        System.out.println("Written to file: " + file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
                writeFutures.add(executor.submit(writer));
            }

            // 2. Make sure all files have been written
            for (Future<?> future : writeFutures) {
                future.get();
            }

            // 3. Create Callable tasks to read files
            List<Callable<String>> tasks = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                tasks.add(new CallableTask("test" + i + ".txt"));
            }


            // 4. Run all read tasks and wait for all results
            List<Future<String>> futures = executor.invokeAll(tasks);

            // 5. Collect results
            for (Future<String> future : futures) {
                try {
                    String content = future.get();
                    System.out.println("File content: " + content);
                } catch (java.util.concurrent.ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
