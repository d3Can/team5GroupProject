package com.team5.io;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;

import java.util.Random;
import java.util.stream.Stream;

/**
 * Генерация случайных студентов; заполнение коллекции выполняется через стрим.
 */
public class RandomDataSource implements DataSource {

    public static final int MIN_GROUP = 1;
    public static final int MAX_GROUP = 9;
    public static final int MIN_RECORD_BOOK = 1;
    public static final int MAX_RECORD_BOOK = 999_999;
    public static final int RECORD_BOOK_CAPACITY = MAX_RECORD_BOOK - MIN_RECORD_BOOK + 1;

    private final Random random;
    private static int CURRENT_RECORD_BOOK = MIN_RECORD_BOOK;

    public RandomDataSource() {
        this(new Random());
    }

    public RandomDataSource(Random random) {
        this.random = random;
    }

    @Override
    public MyList<Student> load(int length) {
        if (length > RECORD_BOOK_CAPACITY) {
            throw new RuntimeException("Невозможно сгенерировать больше "
                    + RECORD_BOOK_CAPACITY + " студентов с уникальными зачётками");
        }
        MyList<Student> result = new MyArrayList<>();
        Stream.generate(this::randomStudent).limit(length).forEach(result::add);
        return result;
    }

    private Student randomStudent() {
        int group = MIN_GROUP + random.nextInt(MAX_GROUP - MIN_GROUP);
        double gpa = Math.round(random.nextDouble() * 500) / 100.0;
        String recordBook = String.valueOf(CURRENT_RECORD_BOOK++);
        return new Student.Builder()
                .setGroupNumber(group)
                .setGpa(gpa)
                .setRecordBookNumber(recordBook)
                .build();
    }
}
