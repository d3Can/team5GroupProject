package com.team5.io;

import com.team5.collection.CustomList;
import com.team5.model.Student;

/**
 * Источник данных для заполнения коллекции.
 */
public interface DataSource {

    CustomList<Student> load(int length);
}
