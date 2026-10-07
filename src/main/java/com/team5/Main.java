package com.team5;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.menu.LoopMenu;
import com.team5.model.Student;

public class Main {
    public static void main(String[] args) {

        MyList<Student> students = new MyArrayList<>();

        LoopMenu menu = new LoopMenu(students);
        menu.start();

    }
}
