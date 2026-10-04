package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VolatileFileDemo {

    static class FileMonitor {

        private final Path file =
                Paths.get("files", "test.txt");

        // Shared state
        private volatile boolean fileReady = false;

        // Producer
        public void writeFile() throws Exception {

            System.out.println("Writer: writing file...");

            Files.createDirectories(file.getParent());

            Files.writeString(
                    file,
                    "hello from writer"
            );

            // Tell the reader:
            // "The file is ready."
            fileReady = true;

            System.out.println("Writer: file is ready.");
        }

        // Consumer
        public void readFile() throws Exception {

            System.out.println("Reader: waiting for file...");

            // Keep checking the shared state
            while (!fileReady) {
                Thread.yield();
            }

            System.out.println("Reader: file is ready.");

            String content =
                    Files.readString(file);

            System.out.println(
                    "Reader: " + content
            );
        }
    }

    public static void main(String[] args)
            throws Exception {

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        FileMonitor monitor =
                new FileMonitor();

        // Writer thread
        executor.submit(() -> {
            try {
                Thread.sleep(2000);
                monitor.writeFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Reader thread
        executor.submit(() -> {
            try {
                monitor.readFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }
}