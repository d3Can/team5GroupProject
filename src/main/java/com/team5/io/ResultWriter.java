package com.team5.io;

import com.team5.collection.MyList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Запись результатов в файл в режиме добавления.
 */
public class ResultWriter {

    public <T> void append(Path path, String title, MyList<T> list) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write("# " + title);
            writer.newLine();
            for (T element : list) {
                writer.write(String.valueOf(element));
                writer.newLine();
            }
            writer.newLine();
        }
    }
}
