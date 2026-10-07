package com.team5.threads;

import com.team5.TestSupport;
import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;

import static com.team5.TestSupport.captureOutput;
import static com.team5.TestSupport.check;
import static com.team5.TestSupport.expectThrows;
import static com.team5.TestSupport.run;
import static com.team5.TestSupport.section;
import static com.team5.TestSupport.student;

/**
 * Тесты пакета threads: многопоточный подсчёт вхождений.
 */
public final class OccurrenceCounterTest {

    private static final int[] THREAD_COUNTS = {1, 2, 3, 4, 7, 16, 64};

    private OccurrenceCounterTest() {
    }

    public static void main(String[] args) {
        runAll();
        TestSupport.finish();
    }

    public static void runAll() {
        section("OccurrenceCounterTest: многопоточный подсчёт");
        run("Разное число потоков даёт одинаковый результат", OccurrenceCounterTest::testSameResultForAnyThreadCount);
        run("Пустой список", OccurrenceCounterTest::testEmptyList);
        run("Элемент не найден / все совпадают", OccurrenceCounterTest::testNotFoundAndAllMatch);
        run("Потоков больше, чем элементов", OccurrenceCounterTest::testMoreThreadsThanElements);
        run("Размер не делится на число потоков (границы чанков)", OccurrenceCounterTest::testUneven);
        run("Сравнение по equals, а не по ссылке", OccurrenceCounterTest::testUsesEquals);
        run("Другие типы и null в качестве цели", OccurrenceCounterTest::testOtherTypesAndNullTarget);
        run("Список не изменяется", OccurrenceCounterTest::testListUnchanged);
        run("Результат стабилен при повторных запусках", OccurrenceCounterTest::testRepeatable);
        run("countAndPrint возвращает и печатает результат", OccurrenceCounterTest::testCountAndPrintWorks);
        run("Неверное число потоков", OccurrenceCounterTest::testInvalidThreadCount);
    }

    private static MyList<Student> thousandWithEvery10thTarget() {
        MyList<Student> list = new MyArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i % 10 == 0 ? student(7, 4.0, 700_007) : student(1, 3.0, 100_001));
        }
        return list;
    }

    private static void testSameResultForAnyThreadCount() {
        MyList<Student> list = thousandWithEvery10thTarget();
        for (int threads : THREAD_COUNTS) {
            check(new OccurrenceCounter(threads).count(list, student(7, 4.0, 700_007)) == 100,
                    "потоков " + threads);
        }
    }

    private static void testEmptyList() {
        for (int threads : THREAD_COUNTS) {
            check(new OccurrenceCounter(threads).count(new MyArrayList<>(), student(7, 4.0, 700_007)) == 0,
                    "потоков " + threads);
        }
    }

    private static void testNotFoundAndAllMatch() {
        check(new OccurrenceCounter(4).count(thousandWithEvery10thTarget(), student(9, 1.0, 900_009)) == 0,
                "не найден");
        MyList<Student> same = new MyArrayList<>();
        for (int i = 0; i < 101; i++) {
            same.add(student(7, 4.0, 700_007));
        }
        for (int threads : THREAD_COUNTS) {
            check(new OccurrenceCounter(threads).count(same, student(7, 4.0, 700_007)) == 101,
                    "все совпадают, потоков " + threads);
        }
    }

    private static void testMoreThreadsThanElements() {
        Student target = student(7, 4.0, 700_007);
        MyList<Student> one = new MyArrayList<>();
        one.add(target);
        check(new OccurrenceCounter(16).count(one, target) == 1, "один элемент");
        MyList<Student> two = new MyArrayList<>();
        two.add(target);
        two.add(student(1, 1, 100_001));
        check(new OccurrenceCounter(64).count(two, target) == 1, "два элемента");
    }

    private static void testUneven() {
        Student target = student(7, 4.0, 700_007);
        Student other = student(1, 3.0, 100_001);
        for (int size = 1; size <= 25; size++) {
            for (int position : new int[]{0, size - 1}) {
                MyList<Student> list = new MyArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(i == position ? target : other);
                }
                for (int threads : THREAD_COUNTS) {
                    check(new OccurrenceCounter(threads).count(list, target) == 1,
                            "размер " + size + ", позиция " + position + ", потоков " + threads);
                }
            }
        }
    }

    private static void testUsesEquals() {
        MyList<Student> list = new MyArrayList<>();
        list.add(student(7, 4.0, 700_007));
        list.add(student(7, 4.0, 700_007));
        list.add(student(7, 4.5, 700_007));
        check(new OccurrenceCounter(2).count(list, student(7, 4.0, 700_007)) == 2, "разные экземпляры, равные значения");
    }

    private static void testOtherTypesAndNullTarget() {
        MyList<Integer> numbers = new MyArrayList<>();
        for (int value : new int[]{1, 5, 5, 2, 5}) {
            numbers.add(value);
        }
        check(new OccurrenceCounter(3).count(numbers, 5) == 3, "Integer");
        check(new OccurrenceCounter(3).count(numbers, null) == 0, "null-цель");
    }

    private static void testListUnchanged() {
        MyList<Student> list = thousandWithEvery10thTarget();
        Student first = list.get(0);
        Student last = list.get(999);
        new OccurrenceCounter(8).count(list, student(7, 4.0, 700_007));
        check(list.size() == 1000 && list.get(0) == first && list.get(999) == last, "список изменился");
    }

    private static void testRepeatable() {
        MyList<Student> list = new MyArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            list.add(i % 7 == 0 ? student(7, 4.0, 700_007) : student(1, 3.0, 100_001));
        }
        int expected = (10_000 + 6) / 7;
        OccurrenceCounter counter = new OccurrenceCounter(8);
        for (int attempt = 0; attempt < 50; attempt++) {
            check(counter.count(list, student(7, 4.0, 700_007)) == expected, "запуск " + attempt);
        }
    }

    private static void testCountAndPrintWorks() throws Exception {
        MyList<Student> list = thousandWithEvery10thTarget();
        int[] result = new int[1];
        String output = captureOutput(
                () -> result[0] = new OccurrenceCounter(4).countAndPrint(list, student(7, 4.0, 700_007)));
        check(result[0] == 100, "возвращаемое значение");
        check(output.contains("Количество вхождений") && output.contains(": 100"), "вывод: " + output);
        check(output.contains("700007"), "в выводе указан искомый элемент");
    }

    private static void testInvalidThreadCount() {
        expectThrows(IllegalArgumentException.class, () -> new OccurrenceCounter(0));
        expectThrows(IllegalArgumentException.class, () -> new OccurrenceCounter(-3));
        new OccurrenceCounter(1);
    }
}
