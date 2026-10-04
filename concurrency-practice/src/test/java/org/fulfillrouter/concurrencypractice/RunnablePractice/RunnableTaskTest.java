package org.fulfillrouter.concurrencypractice.RunnablePractice;

import org.fulfillrouter.concurrencypractice.WriteReadFilePractice.RunnableTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RunnableTaskTest {

    @BeforeEach
    void setup() throws IOException {
        Path filesDir = Paths.get("files");
        if (!Files.exists(filesDir)) {
            Files.createDirectories(filesDir);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Path filesDir = Paths.get("files");
        if (Files.exists(filesDir)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(filesDir)) {
                for (Path p : ds) {
                    Files.deleteIfExists(p);
                }
            }
            Files.deleteIfExists(filesDir);
        }
    }

    @Test
    void testReadExistingFile() throws Exception {
        Path file = Paths.get("files", "test.txt");
        Files.writeString(file, "line1\nline2\n");

        RunnableTask task = new RunnableTask("test.txt");
        task.run();

        // RunnableTask appends lines without newlines, so we expect concatenation
        assertEquals("line1line2", task.content);
    }

    @Test
    void testConstructorNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new RunnableTask(null));
        assertThrows(IllegalArgumentException.class, () -> new RunnableTask(""));
    }

    @Test
    void testRunFileNotFound() throws Exception {
        Path file = Paths.get("files", "doesnotexist.txt");
        Files.deleteIfExists(file);

        RunnableTask task = new RunnableTask("doesnotexist.txt");
        assertThrows(IllegalArgumentException.class, task::run);
    }
}

