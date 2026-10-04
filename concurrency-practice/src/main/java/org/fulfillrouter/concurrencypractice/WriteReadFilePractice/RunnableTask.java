package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RunnableTask implements Runnable{
    String filename;
    String content = "";

    public RunnableTask(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("filename must not be null or empty");
        }
        this.filename = filename;
    }
    // to read file from disk and print the content to console
    @Override
    public void run() {
        Path path = Paths.get("files", filename);
        File file = path.toFile();
        System.out.println("Reading file path: " + path.toString());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + file.getPath(), e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
