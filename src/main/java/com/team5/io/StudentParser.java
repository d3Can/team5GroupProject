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
                .groupNumber(group)
                .gpa(gpa)
                .recordBookNumber(recordBook)
                .build();
    }

    /** Префикс строки, которую записывает {@code ResultWriter} (формат {@code Student.toString()}). */
    public static final String RECORD_PREFIX = "Student{";

    private static final String GROUP_KEY = "groupNumber=";
    private static final String GPA_KEY = ", gpa=";
    private static final String BOOK_KEY = ", recordBookNumber='";
    private static final String BOOK_END = "'}";

    /**
     * Разбор строки в формате, в котором студентов записывает {@code ResultWriter}:
     * {@code Student{groupNumber=1, gpa=4.5, recordBookNumber='123456'}}.
     * Формат должен совпадать с {@code Student.toString()}; это проверяет тест
     * «записал - прочитал».
     */
    public static Student parseRecord(String line) {
        if (line == null || line.isBlank()) {
            throw new RuntimeException("Пустая строка");
        }
        String text = line.trim();
        int gpaKey = text.indexOf(GPA_KEY);
        int bookKey = gpaKey < 0 ? -1 : text.indexOf(BOOK_KEY, gpaKey);
        int groupStart = RECORD_PREFIX.length() + GROUP_KEY.length();
        int gpaStart = gpaKey + GPA_KEY.length();
        int bookStart = bookKey + BOOK_KEY.length();
        int bookEnd = text.length() - BOOK_END.length();
        if (!text.startsWith(RECORD_PREFIX + GROUP_KEY) || !text.endsWith(BOOK_END)
                || gpaKey < groupStart || bookKey < gpaStart || bookEnd < bookStart) {
            throw new RuntimeException("Ожидается формат "
                    + "'Student{groupNumber=..., gpa=..., recordBookNumber='...'}', получено: " + line);
        }
        int group = parseInt(text.substring(groupStart, gpaKey), "Номер группы");
        double gpa = parseDouble(text.substring(gpaStart, bookKey), "Средний балл");
        String recordBook = parseString(text.substring(bookStart, bookEnd), "Номер зачётной книжки");
        return Student.builder()
                .groupNumber(group)
                .gpa(gpa)
                .recordBookNumber(recordBook)
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
