package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CyclicBarrierCallableDemo {

    static class FileTask implements Callable<String> {

        private final String filename;
        private final CyclicBarrier barrier;

        public FileTask(String filename, CyclicBarrier barrier) {
            this.filename = filename;
            this.barrier = barrier;
        }

        @Override
        public String call() throws Exception {

            Path file = Paths.get("files", filename);

            // Phase 1: write
            Files.createDirectories(file.getParent());

            Files.writeString(
                    file,
                    "content of " + filename + "\n"
            );

            System.out.println(
                    Thread.currentThread().getName()
                            + " finished writing " + filename
            );

            // Wait until all 5 files are written
            barrier.await();

            // Phase 2: read
            String content = Files.readString(file);

            System.out.println(
                    Thread.currentThread().getName()
                            + " read " + filename
            );

            return content;
        }
    }

    public static void main(String[] args) throws Exception {

        ExecutorService executor =
                Executors.newFixedThreadPool(5);

        CyclicBarrier barrier =
                new CyclicBarrier(5);

        List<Callable<String>> tasks = new ArrayList<>();

        // Create 5 tasks
        for (int i = 0; i < 5; i++) {
            tasks.add(
                    new FileTask(
                            "test" + i + ".txt",
                            barrier
                    )
            );
        }

        // Execute all tasks asynchronously
        List<Future<String>> futures =
                executor.invokeAll(tasks);

        // Collect results
        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executor.shutdown();
    }
}