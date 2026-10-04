package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableCompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            // 1. Create files
            List<CompletableFuture<Void>> writeFutures = new ArrayList<>();

            for(int i = 0; i < 5; i++) {
                String filename = "test" + i + ".txt";
                Path file = Paths.get("files", filename);
                int finalI = i;
                CompletableFuture<Void> task =
                        CompletableFuture.runAsync(() -> {
                            try {
                                Files.createDirectories(file.getParent());
                                Files.writeString(
                                        file,
                                        "line" + finalI + "\n"
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }, executor);
                writeFutures.add(task);
            }

            // 2. Make sure all files have been written
            CompletableFuture.allOf(writeFutures.toArray(CompletableFuture[]::new)).join();

            // 3. Read files asynchronously
            List<CompletableFuture<String>> readTasks = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                String filename = "test" + i + ".txt";
                CompletableFuture<String> task =
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                Path file = Paths.get("files", filename);
                                return Files.readString(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }, executor);

                readTasks.add(task);
            }
            // 4. Wait for all reads and collect results
            CompletableFuture.allOf(
                    readTasks.toArray(CompletableFuture[]::new)
            ).join();

            for (CompletableFuture<String> task : readTasks) {
                System.out.println("File content: " + task.join());
            }

        } finally {
            executor.shutdown();
        }
    }
}
