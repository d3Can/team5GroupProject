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
 * Чтение студентов из файла. Понимает формат, в котором пишет {@code ResultWriter}
 * ({@code Student{groupNumber=.., gpa=.., recordBookNumber='..'}}, заголовки {@code # ..}
 * пропускаются), а также компактный формат {@code группа;балл;зачётка}.
 * Некорректные строки пропускаются с сообщением.
 */
public class FileDataSource implements DataSource {

    private static final String RESULT_HEADER = "#";

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
        String text = line.trim();
        // пустые строки и заголовки блоков ("# ..."), которые пишет ResultWriter
        if (text.isEmpty() || text.startsWith(RESULT_HEADER)) {
            return null;
        }
        try {
            return text.startsWith(StudentParser.RECORD_PREFIX)
                    ? StudentParser.parseRecord(text)
                    : StudentParser.parse(text);
        } catch (RuntimeException e) {
            System.out.println("  Пропущена строка '" + line + "': " + e.getMessage());
            return null;
        }
    }
}
