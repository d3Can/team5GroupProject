package com.team5.menu;

import com.team5.collection.MyList;
import com.team5.io.*;
import com.team5.model.Student;
import com.team5.model.comparator.GpaComparator;
import com.team5.model.comparator.GroupNumberComparator;
import com.team5.model.comparator.RecordBookNumberComparator;
import com.team5.strategy.*;
import com.team5.threads.OccurrenceCounter;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Scanner;

public class LoopMenu {

    private final MyList<Student> students;
    private final Scanner scanner = new Scanner(System.in);

    public LoopMenu(MyList<Student> students) {
        this.students = students;
    }

    public void start(){
        boolean menuIsRunning = true;

        while (menuIsRunning){
            printMenu();
            int choice = readIntInput("Выберете пункт меню");
            switch (choice){
                case 0 -> {
                    System.out.println("Завершение программы");
                    menuIsRunning = false;
                }
                case 1 -> inputMethodChoice();
                case 2 -> sortChoice();
                case 3 -> System.out.println(students);
                case 4 -> writeToFile();
                case 5 -> countOccurrences();
                default -> System.out.println("Введите корректно и согласно меню!");
            }
        }
    }

    private int readIntInput(String message){
        System.out.println(message);

        if (!scanner.hasNextInt()){
            scanner.nextLine();
            return -1;
        }

        int result = scanner.nextInt();
        scanner.nextLine();
        return result;
    }

    private void printMenu(){
        System.out.println("""
             
                =============== ГЛАВНОЕ МЕНЮ ===============
                1. Ввод студента
                2. Отсортировать
                3. Вывести список всех студентов
                4. Записать в файл
                5. Подсчитать количество вхождений элемента
                0. Выход из программы
                ============================================""");
    }

    private void inputMethodChoice(){
        boolean inputMethodRunning = true;
        while (inputMethodRunning) {
            int inputMethod = readIntInput("""
                                ===== Метод ввода студента =====
                                1. Из файла
                                2. Случайно
                                3. Вручную
                                4. Вернуться в главное меню
                                ================================
                                Выберете метод""");

            switch (inputMethod) {
                case 1 -> {
                    int collectionSize = readIntInput("Выберете длину коллекции");
                    if (collectionSize < 0) {
                        System.out.println("Длина некорректна");
                        break;
                    }

                    System.out.println("Введите путь к файлу");
                    String fileName = scanner.nextLine();

                    try {
                        MyList<Student> loaded = new FileDataSource(Path.of(fileName)).load(collectionSize);
                        students.clear();
                        students.addAll(loaded);
                        System.out.println("Ввод из файла успешно завершён");
                        inputMethodRunning = false;
                    } catch (Exception e) {
                        System.out.println("Ошибка чтения: " + e.getMessage());
                    }
                }
                case 2 -> {
                    int collectionSize = readIntInput("Выберете длину коллекции");
                    if (collectionSize < 0) {
                        System.out.println("Длина некорректна");
                        break;
                    }

                    try {
                        MyList<Student> loaded = new RandomDataSource().load(collectionSize);
                        students.clear();
                        students.addAll(loaded);
                        System.out.println("Ввод случайными данными успешно выполнен");
                        inputMethodRunning = false;
                    } catch (Exception e) {
                        System.out.println("Ошибка генерации: " + e.getMessage());
                    }
                }
                case 3 -> {
                    int collectionSize = readIntInput("Выберете длину коллекции");
                    if (collectionSize < 0) {
                        System.out.println("Длина некорректна");
                        break;
                    }

                    try {
                        MyList<Student> loaded = new ManualDataSource(scanner).load(collectionSize);
                        students.clear();
                        students.addAll(loaded);
                        System.out.println("Ввод вручную успешно выполнен");
                        inputMethodRunning = false;
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода: " + e.getMessage());
                    }
                }
                case 4 -> inputMethodRunning = false;
                default -> System.out.println("Выберете пункт из предложенных");
            }
        }
    }

    private void sortChoice(){
        if (students.isEmpty()) {
            System.out.println("Список пуст. Сначала добавьте студентов.");
            return;
        }

        int algorithmMethod = readIntInput("""
                            ====== Выбор алгоритма ======
                            1. Bubble
                            2. Selection
                            3. Insertion
                            4. Quick
                            5. Merge
                            6. Вернуться в главное меню
                            =============================
                            Выберете алгоритм""");
        SortStrategy<Student> strategy = switch (algorithmMethod) {
            case 1 -> new BubbleSortStrategy<>();
            case 2 -> new SelectionSortStrategy<>();
            case 3 -> new InsertionSortStrategy<>();
            case 4 -> new QuickSortStrategy<>();
            case 5 -> new MergeSortStrategy<>();
            default -> null;
        };
        if (strategy == null) return;

        int oddEvenChoice = readIntInput("""
                            ====== Выбор обычной или чётной/нечётной ======
                            1. Обычная
                            2. Чётная/нечётная
                            3. Вернуться в главное меню
                            =============================
                            Выберете способ""");
        if (oddEvenChoice == 2) {
            strategy = new EvenOddSortStrategy<>(
                    strategy,
                    Student::getGroupNumber,
                    "groupNumber"
            );
        } else if (oddEvenChoice != 1) {
            return;
        }

        int comparatorChoice = readIntInput("""
                            ====== Выбор метода сравнения ======
                            1. Группа
                            2. Средний балл
                            3. Зачётка
                            4. Натуральный порядок
                            5. Вернуться в главное меню
                            =============================
                            Выберете способ сравнения""");
        Comparator<Student> comparator = switch (comparatorChoice){
            case 1 -> new GroupNumberComparator();
            case 2 -> new GpaComparator();
            case 3 -> new RecordBookNumberComparator();
            case 4 -> Comparator.naturalOrder();
            default -> null;
        };
        if (comparator == null) return;

        strategy.sort(students, comparator);
        System.out.println("Готово: " + strategy.getName());
    }

    private void writeToFile() {
        if (students.isEmpty()) {
            System.out.println("Список пуст");
            return;
        }
        System.out.print("Путь к файлу: ");
        String path = scanner.nextLine().trim();
        System.out.print("Заголовок: ");
        String title = scanner.nextLine().trim();
        try {
            new ResultWriter().append(Path.of(path), title, students);
            System.out.println("Записано");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void countOccurrences() {
        if (students.isEmpty()) {
            System.out.println("Список пуст");
            return;
        }
        int group = readIntInput("Группа");
        if (group < 1) {
            System.out.println("Группа должна быть >= 1");
            return;
        }

        System.out.print("Средний балл: ");
        double gpa;
        try {
            gpa = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("Некорректный GPA (введите число, например: 4.5)");
            return;
        }

        System.out.print("Зачётка: ");
        String recordBook = scanner.nextLine().trim();

        Student target;
        try {
            target = Student.builder()
                    .groupNumber(group)
                    .gpa(gpa)
                    .recordBookNumber(recordBook)
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректные данные: " + e.getMessage());
            return;
        }

        int threads = readIntInput("Число потоков");
        if (threads < 1) {
            System.out.println("Потоков должно быть >= 1");
            return;
        }
        new OccurrenceCounter(threads).countAndPrint(students, target);
    }
}
