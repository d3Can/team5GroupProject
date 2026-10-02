package com.team5.model.comparator;

import com.team5.model.Student;
import java.util.Comparator;

/**
 * Сравнивает {@link Student} по номеру зачётной книжки
 * в лексикографическом порядке возрастания.
 */
public final class RecordBookNumberComparator implements Comparator<Student> {
    @Override
    public int compare(Student first, Student second) {
        return first.getRecordBookNumber()
                .compareTo(second.getRecordBookNumber());
    }
}
