package com.team5;

import com.team5.menu.LoopMenu;
import com.team5.model.Student;

//todo УДАЛИТЬ ПРО АРЕЙ ЛИСТ!!!!!!!!!!!!!!
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //todo ВРЕМЕННАЯ ЗАГЛУШКА, НЕ ЗАБЫТЬ ПОМЕНЯТЬ НА КАСТОМНЫЙ ЛИСТ
        List<Student> students = new ArrayList<>();

        LoopMenu menu = new LoopMenu(students);
        menu.start();

    }
}
