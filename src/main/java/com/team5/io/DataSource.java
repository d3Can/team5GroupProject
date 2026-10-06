package com.team5.io;

import com.team5.collection.MyList;
import com.team5.model.Student;

/**
 * Источник данных для заполнения коллекции.
 */
public interface DataSource {

    MyList<Student> load(int length);
}
