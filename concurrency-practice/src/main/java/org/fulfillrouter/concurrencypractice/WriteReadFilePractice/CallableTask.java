package org.fulfillrouter.concurrencypractice.WriteReadFilePractice;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class CallableTask implements Callable<String> {
    String filename;
    String content = "";

    public CallableTask(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("filename must not be null or empty");
        }
        this.filename = filename;
    }

// to read file from disk and print the content to console
    @Override
    public String call() throws Exception {
        Path path = Paths.get("files", filename);
        File file = path.toFile();
        System.out.println("Reading file path: " + path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            // remove trailing line separator if present
            int sepLen = System.lineSeparator().length();
            if (sb.length() >= sepLen) {
                sb.setLength(sb.length() - sepLen);
            }
            content = sb.toString();
            return content;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + file.getPath(), e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
