package com.team5.model.comparator;

import com.team5.model.Student;
import java.util.Comparator;

/**
 * Сравнивает {@link Student} по номеру группы в порядке возрастания.
 */
public final class GroupNumberComparator implements Comparator<Student> {
    @Override
    public int compare(Student first, Student second) {
        return Integer.compare(first.getGroupNumber(), second.getGroupNumber());
    }
}
