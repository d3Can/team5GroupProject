package com.team5.io;

import com.team5.model.Student;

/**
 * Разбор строки формата {@code группа;балл;зачётка} с валидацией.
 */
public final class StudentParser {

    public static final String SEPARATOR = ";";

    private StudentParser() {
    }

    public static Student parse(String line) {
        if (line == null || line.isBlank()) {
            throw new RuntimeException("Пустая строка");
        }
        String[] parts = line.split(SEPARATOR, -1);
        if (parts.length != 3) {
            throw new RuntimeException("Ожидается формат 'группа;балл;зачётка', получено: " + line);
        }
        int group = parseInt(parts[0], "Номер группы");
        double gpa = parseDouble(parts[1], "Средний балл");
        String recordBook = parseString(parts[2], "Номер зачётной книжки");
        return Student.builder()
                .GroupNumber(group)
                .Gpa(gpa)
                .RecordBookNumber(recordBook)
                .build();
    }

    private static int parseInt(String raw, String field) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(field + ": '" + raw.trim() + "' не является целым числом");
        }
    }

    private static double parseDouble(String raw, String field) {
        try {
            return Double.parseDouble(raw.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new RuntimeException(field + ": '" + raw.trim() + "' не является числом");
        }
    }

    private static String parseString(String raw, String field) {
        String value = raw.trim();
        if (value.isEmpty()) {
            throw new RuntimeException(field + ": значение не задано");
        }
        return value;
    }
}
