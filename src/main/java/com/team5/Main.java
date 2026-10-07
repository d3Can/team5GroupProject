package com.team5;

import com.team5.model.Student;
import com.team5.model.comparator.GpaComparator;
import com.team5.model.comparator.GroupNumberComparator;
import com.team5.model.comparator.RecordBookNumberComparator;

/**
 * Примеры использования модели Student
 */
public class Main {
    public static void main(String[] args) {
        // Создание
        Student student = Student.builder()
                .GroupNumber(101)
                .Gpa(4.8)
                .RecordBookNumber("ZB-9912")
                .build();

        // Чтение полей
        int groupNumber = student.getGroupNumber();
        double gpa = student.getGpa();
        String recordBookNumber = student.getRecordBookNumber();

        // Компараторы для сортировки по отдельным полям
        GroupNumberComparator groupComparator = new GroupNumberComparator();
        GpaComparator gpaComparator = new GpaComparator();
        RecordBookNumberComparator recordBookComparator = new RecordBookNumberComparator();

        System.out.println("Создан студент: " + student);
        System.out.println("Группа: " + student.getGroupNumber());
        System.out.println("Средний балл: " + student.getGpa());
        System.out.println("Зачётка: " + student.getRecordBookNumber());
        System.out.println("Групповой компаратор: " + GroupNumberComparator.class.getSimpleName());
        System.out.println("GPA компаратор: " + GpaComparator.class.getSimpleName());
        System.out.println("Компаратор зачёток: " + RecordBookNumberComparator.class.getSimpleName());
    }
}
