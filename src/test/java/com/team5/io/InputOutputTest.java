package com.team5.io;

import com.team5.TestSupport;
import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static com.team5.TestSupport.check;
import static com.team5.TestSupport.expectInvalid;
import static com.team5.TestSupport.expectThrows;
import static com.team5.TestSupport.run;
import static com.team5.TestSupport.sample;
import static com.team5.TestSupport.section;

/**
 * Тесты пакета io: парсер, источники данных (файл, рандом, ручной ввод) и запись в файл.
 */
public final class InputOutputTest {

    private InputOutputTest() {
    }

    public static void main(String[] args) {
        runAll();
        TestSupport.finish();
    }

    public static void runAll() {
        section("InputOutputTest: парсер, источники данных, запись");
        run("Парсер: корректные строки", InputOutputTest::testParserValid);
        run("Парсер: граничные значения", InputOutputTest::testParserBoundaries);
        run("Парсер: некорректные строки", InputOutputTest::testParserInvalid);
        run("Парсер: сообщения называют поле", InputOutputTest::testParserMessages);
        run("Рандом: длина и допустимые значения", InputOutputTest::testRandomBasics);
        run("Рандом: нулевая длина", InputOutputTest::testRandomZeroLength);
        run("Рандом: зачётки уникальны (в том числе между загрузками)", InputOutputTest::testRandomUniqueRecordBooks);
        run("Рандом: одинаковый seed даёт одинаковые группы и баллы", InputOutputTest::testRandomSeed);
        run("Рандом: слишком большая длина", InputOutputTest::testRandomTooLong);
        run("Файл: мусор и пустые строки пропускаются", InputOutputTest::testFileSkipsInvalid);
        run("Файл: порядок строк сохраняется", InputOutputTest::testFileKeepsOrder);
        run("Файл: ограничение длины", InputOutputTest::testFileLengthLimit);
        run("Файл: пустой файл", InputOutputTest::testFileEmpty);
        run("Файл: переводы строк Windows", InputOutputTest::testFileCrlf);
        run("Файл: отсутствующий файл", InputOutputTest::testFileMissing);
        run("Ручной ввод: ошибки повторяют запрос", InputOutputTest::testManualInput);
        run("Ручной ввод: ввод закончился раньше длины", InputOutputTest::testManualShortInput);
        run("Ручной ввод: нулевая длина", InputOutputTest::testManualZeroLength);
        run("Запись: дописывает, а не перезаписывает", InputOutputTest::testWriterAppends);
        run("Запись: создаёт новый файл", InputOutputTest::testWriterCreatesFile);
        run("Запись: пустая коллекция и не-Student", InputOutputTest::testWriterEmptyAndGeneric);
        run("Запись: кириллица в заголовке", InputOutputTest::testWriterUtf8);
        run("Запись: недопустимый путь", InputOutputTest::testWriterBadPath);
    }

    // ---------- StudentParser ----------

    private static void testParserValid() {
        Student student = StudentParser.parse("5; 4,25 ;654321");
        check(student.getGroupNumber() == 5, "группа");
        check(student.getGpa() == 4.25, "балл с запятой");
        check(student.getRecordBookNumber().equals("654321"), "зачётка");
        check(StudentParser.parse("12;4.5;ZB-9912").getRecordBookNumber().equals("ZB-9912"), "не число");
        check(StudentParser.parse("1;4; 012345 ").getRecordBookNumber().equals("012345"),
                "пробелы обрезаются, ведущий ноль сохраняется");
    }

    private static void testParserBoundaries() {
        check(StudentParser.parse("1;0;A1").getGpa() == 0.0, "нижняя граница балла");
        check(StudentParser.parse("1;5;A1").getGpa() == 5.0, "верхняя граница балла");
        check(StudentParser.parse("1;4;A1").getGroupNumber() == 1, "минимальная группа");
        check(StudentParser.parse("2147483647;4;A1").getGroupNumber() == Integer.MAX_VALUE, "большая группа");
    }

    private static void testParserInvalid() {
        expectInvalid(() -> StudentParser.parse(null));
        expectInvalid(() -> StudentParser.parse(""));
        expectInvalid(() -> StudentParser.parse("   "));
        expectInvalid(() -> StudentParser.parse("1;2"));
        expectInvalid(() -> StudentParser.parse("1;4;123456;7"));
        expectInvalid(() -> StudentParser.parse("abc;4;123456"));
        expectInvalid(() -> StudentParser.parse("1.5;4;123456"));
        expectInvalid(() -> StudentParser.parse("1;xyz;123456"));
        expectInvalid(() -> StudentParser.parse("0;4;123456"));
        expectInvalid(() -> StudentParser.parse("-3;4;123456"));
        expectInvalid(() -> StudentParser.parse("1;6;123456"));
        expectInvalid(() -> StudentParser.parse("1;-0.1;123456"));
        expectInvalid(() -> StudentParser.parse("1;Infinity;123456"));
        expectInvalid(() -> StudentParser.parse("1;4;"));
        expectInvalid(() -> StudentParser.parse("1;4;   "));
        expectInvalid(() -> StudentParser.parse("99999999999;4;123456"));
    }

    private static void testParserMessages() {
        check(testMessageOf("abc;4;123456").contains("Номер группы"), "группа");
        check(testMessageOf("1;xyz;123456").contains("Средний балл"), "балл");
        check(testMessageOf("1;4;   ").contains("зачётной книжки"), "зачётка");
        check(testMessageOf("1;2").contains("группа;балл;зачётка"), "формат");
    }

    private static String testMessageOf(String line) {
        try {
            StudentParser.parse(line);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        throw new AssertionError("ожидалось исключение для: " + line);
    }

    // ---------- RandomDataSource ----------

    private static void testRandomBasics() {
        check(new RandomDataSource().load(1).size() == 1, "длина 1");
        MyList<Student> list = new RandomDataSource().load(500);
        check(list.size() == 500, "длина");
        for (Student student : list) {
            check(student.getGroupNumber() >= RandomDataSource.MIN_GROUP
                    && student.getGroupNumber() <= RandomDataSource.MAX_GROUP, "группа: " + student);
            check(student.getGpa() >= 0.0 && student.getGpa() <= 5.0, "балл: " + student);
            double hundredths = student.getGpa() * 100;
            check(Math.abs(hundredths - Math.round(hundredths)) < 1e-9, "две цифры после запятой: " + student);
            check(!student.getRecordBookNumber().isBlank(), "зачётка: " + student);
        }
    }

    private static void testRandomZeroLength() {
        check(new RandomDataSource().load(0).isEmpty(), "пусто");
    }

    private static void testRandomUniqueRecordBooks() {
        Set<String> seen = new HashSet<>();
        for (int load = 0; load < 2; load++) {
            for (Student student : new RandomDataSource().load(5000)) {
                check(seen.add(student.getRecordBookNumber()), "дубликат зачётки " + student.getRecordBookNumber());
            }
        }
        check(seen.size() == 10_000, "всего уникальных");
    }

    private static void testRandomSeed() {
        MyList<Student> a = new RandomDataSource(new Random(7)).load(50);
        MyList<Student> b = new RandomDataSource(new Random(7)).load(50);
        for (int i = 0; i < 50; i++) {
            check(a.get(i).getGroupNumber() == b.get(i).getGroupNumber(), "группа " + i);
            check(a.get(i).getGpa() == b.get(i).getGpa(), "балл " + i);
        }
    }

    private static void testRandomTooLong() {
        expectInvalid(() -> new RandomDataSource().load(RandomDataSource.RECORD_BOOK_CAPACITY + 1));
    }

    // ---------- FileDataSource ----------

    private static Path tempFile(List<String> lines) throws IOException {
        Path file = Files.createTempFile("team5-students", ".txt");
        Files.write(file, lines);
        return file;
    }

    private static void testFileSkipsInvalid() throws Exception {
        Path file = tempFile(List.of("1;4.5;123456", "bad line", "", "2;9;123456", "   ", "3;3.0;654321"));
        try {
            MyList<Student> list = new FileDataSource(file).load(10);
            check(list.size() == 2, "ожидалось 2 валидных, получено " + list.size());
            check(list.get(0).getGroupNumber() == 1 && list.get(1).getGroupNumber() == 3, "состав");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testFileKeepsOrder() throws Exception {
        Path file = tempFile(List.of("5;4;A5", "1;4;A1", "3;4;A3"));
        try {
            MyList<Student> list = new FileDataSource(file).load(10);
            check(list.get(0).getGroupNumber() == 5 && list.get(1).getGroupNumber() == 1
                    && list.get(2).getGroupNumber() == 3, "порядок как в файле");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testFileLengthLimit() throws Exception {
        Path file = tempFile(List.of("1;4;100001", "1;4;100002", "1;4;100003", "1;4;100004", "1;4;100005"));
        try {
            check(new FileDataSource(file).load(3).size() == 3, "лимит 3");
            check(new FileDataSource(file).load(100).size() == 5, "меньше, чем запрошено");
            check(new FileDataSource(file).load(0).isEmpty(), "лимит 0");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testFileEmpty() throws Exception {
        Path file = tempFile(List.of());
        try {
            check(new FileDataSource(file).load(5).isEmpty(), "пустой файл");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testFileCrlf() throws Exception {
        Path file = Files.createTempFile("team5-crlf", ".txt");
        try {
            Files.writeString(file, "1;4;ABC\r\n2;3;DEF\r\n");
            MyList<Student> list = new FileDataSource(file).load(10);
            check(list.size() == 2, "две строки");
            check(list.get(0).getRecordBookNumber().equals("ABC"), "без символа \\r");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testFileMissing() {
        expectThrows(UncheckedIOException.class,
                () -> new FileDataSource(Path.of("no/such/dir/missing.txt")).load(1));
    }

    // ---------- ManualDataSource ----------

    private static void testManualInput() {
        Scanner scanner = new Scanner("1;4;123456\nbad\n0;4;111111\n3;4;654321\n");
        MyList<Student> list = new ManualDataSource(scanner).load(2);
        check(list.size() == 2, "размер");
        check(list.get(0).getRecordBookNumber().equals("123456")
                && list.get(1).getRecordBookNumber().equals("654321"), "мусор отклонён, ввод повторён");
    }

    private static void testManualShortInput() {
        check(new ManualDataSource(new Scanner("1;4;123456\n")).load(3).size() == 1, "конец ввода");
    }

    private static void testManualZeroLength() {
        Scanner scanner = new Scanner("1;4;123456\n");
        check(new ManualDataSource(scanner).load(0).isEmpty(), "пусто");
        check(scanner.hasNextLine(), "ввод не должен читаться");
    }

    // ---------- ResultWriter ----------

    private static void testWriterAppends() throws Exception {
        Path file = Files.createTempFile("team5-result", ".txt");
        try {
            ResultWriter writer = new ResultWriter();
            writer.append(file, "первый", sample());
            long first = Files.size(file);
            writer.append(file, "второй", sample());
            check(Files.size(file) > first, "данные не дописались");
            List<String> lines = Files.readAllLines(file);
            check(lines.stream().filter(l -> l.startsWith("# ")).count() == 2, "два заголовка");
            check(lines.stream().filter(l -> l.startsWith("Student{")).count() == 10, "десять записей");
            check(lines.getFirst().equals("# первый"), "первый блок сохранён");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testWriterCreatesFile() throws Exception {
        Path file = Files.createTempFile("team5-new", ".txt");
        Files.delete(file);
        try {
            new ResultWriter().append(file, "новый", sample());
            check(Files.exists(file) && Files.readAllLines(file).size() == 7, "заголовок + 5 записей + пустая строка");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testWriterEmptyAndGeneric() throws Exception {
        Path file = Files.createTempFile("team5-generic", ".txt");
        try {
            ResultWriter writer = new ResultWriter();
            writer.append(file, "пусто", new MyArrayList<Student>());
            check(Files.readAllLines(file).equals(List.of("# пусто", "")), "только заголовок");
            MyList<Integer> numbers = new MyArrayList<>();
            numbers.add(1);
            numbers.add(2);
            writer.append(file, "числа", numbers);
            check(Files.readAllLines(file).containsAll(List.of("# числа", "1", "2")), "не-Student");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testWriterUtf8() throws Exception {
        Path file = Files.createTempFile("team5-utf8", ".txt");
        try {
            new ResultWriter().append(file, "Сортировка по полю «Группа»", sample());
            check(Files.readAllLines(file).getFirst().equals("# Сортировка по полю «Группа»"), "UTF-8");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static void testWriterBadPath() throws Exception {
        Path directory = Files.createTempDirectory("team5-dir");
        try {
            expectThrows(IOException.class, () -> new ResultWriter().append(directory, "x", sample()));
        } finally {
            Files.deleteIfExists(directory);
        }
    }
}
