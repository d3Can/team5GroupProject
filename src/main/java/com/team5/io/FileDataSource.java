package com.team5.io;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Чтение студентов из файла. Некорректные строки пропускаются с сообщением.
 */
public class FileDataSource implements DataSource {

    private final Path path;

    public FileDataSource(Path path) {
        this.path = path;
    }

    @Override
    public MyList<Student> load(int length) {
        MyList<Student> result = new MyArrayList<>();
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.map(this::tryParse)
                    .filter(Objects::nonNull)
                    .limit(length)
                    .forEach(result::add);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать файл " + path, e);
        }
        return result;
    }

    private Student tryParse(String line) {
        if (line.isBlank()) {
            return null;
        }
        try {
            return StudentParser.parse(line);
        } catch (RuntimeException e) {
            System.out.println("  Пропущена строка '" + line + "': " + e.getMessage());
            return null;
        }
    }
}
