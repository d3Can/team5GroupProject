package com.team5.model.comparator;

import com.team5.model.Student;
import java.util.Comparator;

/**
 * Сравнивает {@link Student} по среднему баллу в порядке возрастания.
 */
public final class GpaComparator implements Comparator<Student> {
    @Override
    public int compare(Student first, Student second) {
        return Double.compare(first.getGpa(), second.getGpa());
    }
}
