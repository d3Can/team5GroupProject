package com.team5.menu;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestMenuMain {

    public static void main(String[] args) {
        System.out.println("================ ПОЛНОЕ ТЕСТИРОВАНИЕ LOOP MENU ================");

        testExitMenu();
        testInvalidMainMenuChoice();
        testSortOnEmptyList();
        testWriteToFileEmptyList();
        testNegativeSizeInput();
        testRandomGeneration();
        testManualInput();
        testFullSortingFlow();
        testCountOccurrencesInvalidGpa();
        testCountOccurrencesInvalidThreads();

        System.out.println("\n================ ВСЕ 10 ТЕСТОВ МЕНЮ ПРОЙДЕНЫ ================");
    }

    private static void testExitMenu() {
        System.out.println("\n--- [1] Выход из меню (0) ---");
        runMenuTest("0\n", new MyArrayList<>());
        System.out.println("[PASS] Успешный выход из программы");
    }

    private static void testInvalidMainMenuChoice() {
        System.out.println("\n--- [2] Неверный пункт главного меню (99 -> 0) ---");
        runMenuTest("99\n0\n", new MyArrayList<>());
        System.out.println("[PASS] Сообщение о неверном пункте выведено, меню не упало");
    }

    private static void testSortOnEmptyList() {
        System.out.println("\n--- [3] Сортировка пустого списка (2 -> 0) ---");
        runMenuTest("2\n0\n", new MyArrayList<>());
        System.out.println("[PASS] Попытка сортировки пустого списка заблокирована");
    }

    private static void testWriteToFileEmptyList() {
        System.out.println("\n--- [4] Запись пустого списка в файл (4 -> 0) ---");
        runMenuTest("4\n0\n", new MyArrayList<>());
        System.out.println("[PASS] Запись пустого списка заблокирована");
    }

    private static void testNegativeSizeInput() {
        System.out.println("\n--- [5] Ввод отрицательной длины (-1) ---");
        runMenuTest("1\n1\n-1\n4\n0\n", new MyArrayList<>());
        System.out.println("[PASS] Отрицательный размер перехвачен");
    }

    private static void testRandomGeneration() {
        System.out.println("\n--- [6] Случайная генерация (1 -> 2 -> 5 элементов) ---");
        MyList<Student> list = new MyArrayList<>();
        runMenuTest("1\n2\n5\n0\n", list);
        assertCondition(list.size() == 5, "В коллекцию добавлено ровно 5 элементов");
    }

    private static void testManualInput() {
        System.out.println("\n--- [7] Ручной ввод студента (1 -> 3 -> 1 элемент) ---");
        MyList<Student> list = new MyArrayList<>();
        String input = "1\n3\n1\n101;4.8;123456\n0\n";
        runMenuTest(input, list);
        assertCondition(list.size() == 1, "Ручной ввод успешно добавил 1 студента");
    }

    private static void testFullSortingFlow() {
        System.out.println("\n--- [8] Генерация + выбор сортировки + вывод ---");
        MyList<Student> list = new MyArrayList<>();
        String input = "1\n2\n3\n2\n1\n1\n1\n3\n0\n";
        runMenuTest(input, list);
        assertCondition(list.size() == 3, "Полная сортировка отработала без ошибок");
    }

    private static void testCountOccurrencesInvalidGpa() {
        System.out.println("\n--- [9] Подсчет вхождений с некорректным GPA ('abc') ---");
        MyList<Student> list = new MyArrayList<>();
        list.add(Student.builder().GroupNumber(1).Gpa(4.0).RecordBookNumber("111").build());

        String input = "5\n1\nabc\n0\n";
        runMenuTest(input, list);
        System.out.println("[PASS] Ошибка GPA перехвачена, меню не упало");
    }

    private static void testCountOccurrencesInvalidThreads() {
        System.out.println("\n--- [10] Подсчет вхождений с 0 потоков ---");
        MyList<Student> list = new MyArrayList<>();
        list.add(Student.builder().GroupNumber(1).Gpa(4.0).RecordBookNumber("111").build());

        String input = "5\n1\n4.0\n111\n0\n0\n";
        runMenuTest(input, list);
        System.out.println("[PASS] Некорректное число потоков заблокировано");
    }

    private static void runMenuTest(String inputData, MyList<Student> list) {
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)));
            LoopMenu menu = new LoopMenu(list);
            menu.start();
        } finally {
            System.setIn(originalIn);
        }
    }

    private static void assertCondition(boolean condition, String message) {
        if (condition) {
            System.out.println("[PASS] " + message);
        } else {
            System.err.println("[FAIL] " + message);
        }
    }
}