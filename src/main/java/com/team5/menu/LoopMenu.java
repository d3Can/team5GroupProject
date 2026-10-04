package com.team5.menu;

import com.team5.model.Student;
import java.util.List;
import java.util.Scanner;

public class LoopMenu {

    private final List<Student> students;
    private final Scanner scanner = new Scanner(System.in);

    public LoopMenu(List<Student> students) {
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
                case 2 -> System.out.println("Иммитация передачи в сортировку");
                //todo СДЕЛАТЬ ВЫБОР СОРТИРОВКИ!!!!!!!!!
                case 3 -> System.out.println(students);
                case 4 -> System.out.println("Иммитация передачи в запись в файл");
                case 5 -> System.out.println("Иммитация передачи в подсчёт количества вхождений");
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
                    System.out.println("происходит иммитация передачи в ввод из файла");
                    //todo В КОНЦЕ ОБНОВИТЬ НА НАСТОЯЩЕЕ
                    inputMethodRunning = false;
                }
                case 2 -> {
                    int collectionSize = readIntInput("Выберете длину коллекции");
                    System.out.println("происходит иммитация передачи в случайный ввод");
                    //todo В КОНЦЕ ОБНОВИТЬ НА НАСТОЯЩЕЕ
                    inputMethodRunning = false;
                }
                case 3 -> {
                    int collectionSize = readIntInput("Выберете длину коллекции");
                    for (int i = 0; i < collectionSize; i++) {
                        System.out.println("происходит иммитация передачи в ручной ввод");
                    }
                    inputMethodRunning = false;
                }
                case 4 -> inputMethodRunning = false;
                default -> System.out.println("Выберете пункт из предложенных");
            }
        }
    }
}
