package com.team5.io;

import com.team5.collection.CustomList;
import com.team5.model.Student;

import java.util.Scanner;

/**
 * Ручной ввод студентов с консоли. При ошибке ввод повторяется.
 */
public class ManualDataSource implements DataSource {

    private final Scanner scanner;

    public ManualDataSource(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public CustomList<Student> load(int length) {
        CustomList<Student> result = new CustomList<>();
        while (result.size() < length) {
            System.out.printf("Студент %d/%d (группа;балл;зачётка): ", result.size() + 1, length);
            if (!scanner.hasNextLine()) {
                break;
            }
            try {
                result.add(StudentParser.parse(scanner.nextLine()));
            } catch (RuntimeException e) {
                System.out.println("  Ошибка: " + e.getMessage());
            }
        }
        return result;
    }
}
