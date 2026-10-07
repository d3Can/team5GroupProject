package com.team5;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Мини-фреймворк для ручных тестов: запуск, проверки и общие фабрики данных.
 */
public final class TestSupport {

    private static int passed;
    private static int failed;

    private TestSupport() {
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static void section(String title) {
        System.out.println("-- " + title + " --");
    }

    public static void run(String name, ThrowingRunnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[OK]   " + name);
        } catch (Throwable e) {
            failed++;
            System.out.println("[FAIL] " + name + ": " + e);
        }
    }

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /** Ошибка разбора и валидации данных в проекте — {@link RuntimeException}. */
    public static void expectInvalid(ThrowingRunnable action) {
        expectThrows(RuntimeException.class, action);
    }

    public static void expectThrows(Class<? extends Throwable> type, ThrowingRunnable action) {
        try {
            action.run();
        } catch (Throwable e) {
            if (type.isInstance(e)) {
                return;
            }
            throw new AssertionError("ожидалось " + type.getSimpleName() + ", получено " + e);
        }
        throw new AssertionError("ожидалось исключение " + type.getSimpleName());
    }

    /** Выполняет действие и возвращает всё, что оно напечатало в System.out. */
    public static String captureOutput(ThrowingRunnable action) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public static void finish() {
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    public static Student student(int group, double gpa, String recordBook) {
        return Student.builder()
                .GroupNumber(group)
                .Gpa(gpa)
                .RecordBookNumber(recordBook)
                .build();
    }

    /** Зачётка передаётся числом и дополняется нулями слева до 6 цифр. */
    public static Student student(int group, double gpa, int recordBook) {
        return student(group, gpa, String.format("%06d", recordBook));
    }

    /** Пять студентов с разными группами, баллами и зачётками. */
    public static MyList<Student> sample() {
        MyList<Student> list = new MyArrayList<>();
        list.add(student(3, 4.0, 300003));
        list.add(student(1, 5.0, 100001));
        list.add(student(2, 3.5, 200002));
        list.add(student(4, 2.5, 400004));
        list.add(student(1, 4.5, 100005));
        return list;
    }
}
