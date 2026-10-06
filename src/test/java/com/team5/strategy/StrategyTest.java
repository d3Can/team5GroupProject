package com.team5.strategy;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import com.team5.model.Student;
import com.team5.model.comparator.GpaComparator;
import com.team5.model.comparator.GroupNumberComparator;
import com.team5.model.comparator.RecordBookNumberComparator;

public class StrategyTest {
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        MyList<SortStrategy<Student>> strategies = myListOf(
                new BubbleSortStrategy<Student>(),
                new SelectionSortStrategy<Student>(),
                new InsertionSortStrategy<Student>(),
                new QuickSortStrategy<Student>(),
                new MergeSortStrategy<Student>()
        );

        for (SortStrategy<Student> strategy : strategies) {
            testStrategySortsByGroupNumber(strategy);
            testStrategySortsByGpa(strategy);
            testStrategySortsByRecordBookNumber(strategy);
            testStrategyHandlesEmptyList(strategy);
            testStrategyHandlesSingleElement(strategy);
            testStrategyRejectsNullData(strategy);
            testStrategyRejectsNullComparator(strategy);
        }

        testEvenOddKeepsOddInPlace();
        testEvenOddOnStudentsByGroupNumber();
        testEvenOddRejectsNullDelegate();
        testEvenOddRejectsNullExtractor();
        testEvenOddRejectsBlankFieldName();

        System.out.println();
        System.out.println("Пройдено тестов: " + passedTests);
        System.out.println("Провалено тестов: " + failedTests);

        if (failedTests > 0) {
            System.exit(1);
        }
    }

    private static void testStrategySortsByGroupNumber(SortStrategy<Student> strategy) {
        MyList<Student> data = myListOf(
                student(103, 4.5, "ZB-03"),
                student(101, 4.5, "ZB-01"),
                student(102, 4.5, "ZB-02")
        );
        strategy.sort(data, new GroupNumberComparator());
        int[] expected = {101, 102, 103};
        check(strategy.getName() + ": сортировка по groupNumber",
                groupNumbersOf(data).equals(toList(expected)));
    }

    private static void testStrategySortsByGpa(SortStrategy<Student> strategy) {
        MyList<Student> data = myListOf(
                student(101, 4.8, "ZB-01"),
                student(101, 3.2, "ZB-02"),
                student(101, 4.1, "ZB-03")
        );
        strategy.sort(data, new GpaComparator());
        double[] expected = {3.2, 4.1, 4.8};
        boolean ok = true;
        for (int i = 0; i < expected.length; i++) {
            if (Double.compare(data.get(i).getGpa(), expected[i]) != 0) {
                ok = false;
                break;
            }
        }
        check(strategy.getName() + ": сортировка по gpa", ok);
    }

    private static void testStrategySortsByRecordBookNumber(SortStrategy<Student> strategy) {
        MyList<Student> data = myListOf(
                student(101, 4.5, "ZB-0003"),
                student(101, 4.5, "ZB-0001"),
                student(101, 4.5, "ZB-0002")
        );
        strategy.sort(data, new RecordBookNumberComparator());
        MyList<String> expected = myListOf("ZB-0001", "ZB-0002", "ZB-0003");
        MyList<String> actual = new MyArrayList<String>();
        for (Student student : data) {
            actual.add(student.getRecordBookNumber());
        }
        check(strategy.getName() + ": сортировка по recordBookNumber",
                expected.equals(actual));
    }

    private static void testStrategyHandlesEmptyList(SortStrategy<Student> strategy) {
        MyList<Student> data = new MyArrayList<Student>();
        strategy.sort(data, new GroupNumberComparator());
        check(strategy.getName() + ": пустой список",
                data.isEmpty());
    }

    private static void testStrategyHandlesSingleElement(SortStrategy<Student> strategy) {
        MyList<Student> data = new MyArrayList<Student>();
        data.add(student(101, 4.5, "ZB-0001"));
        strategy.sort(data, new GroupNumberComparator());
        check(strategy.getName() + ": один элемент",
                data.size() == 1 && data.get(0).getGroupNumber() == 101);
    }

    private static void testStrategyRejectsNullData(SortStrategy<Student> strategy) {
        boolean thrown = false;
        try {
            strategy.sort(null, new GroupNumberComparator());
        } catch (IllegalArgumentException exception) {
            thrown = true;
        }
        check(strategy.getName() + ": null-список отклонён", thrown);
    }

    private static void testStrategyRejectsNullComparator(SortStrategy<Student> strategy) {
        boolean thrown = false;
        try {
            strategy.sort(new MyArrayList<Student>(), null);
        } catch (IllegalArgumentException exception) {
            thrown = true;
        }
        check(strategy.getName() + ": null-компаратор отклонён", thrown);
    }

    private static void testEvenOddKeepsOddInPlace() {
        MyList<Student> data = myListOf(
                student(3, 4.0, "ZB-01"),
                student(8, 4.0, "ZB-02"),
                student(5, 4.0, "ZB-03"),
                student(2, 4.0, "ZB-04"),
                student(7, 4.0, "ZB-05"),
                student(4, 4.0, "ZB-06"),
                student(9, 4.0, "ZB-07"),
                student(6, 4.0, "ZB-08")
        );

        SortStrategy<Student> strategy = new EvenOddSortStrategy<Student>(
                new QuickSortStrategy<Student>(),
                Student::getGroupNumber,
                "groupNumber"
        );
        strategy.sort(data, new GroupNumberComparator());

        MyList<Integer> expected = myListOf(3, 2, 5, 4, 7, 6, 9, 8);
        check("EvenOdd: нечётные на своих местах, чётные отсортированы",
                groupNumbersOf(data).equals(expected));
    }

    private static void testEvenOddOnStudentsByGroupNumber() {
        MyList<Student> data = myListOf(
                student(8, 4.0, "ZB-01"),
                student(2, 4.0, "ZB-02"),
                student(6, 4.0, "ZB-03"),
                student(4, 4.0, "ZB-04")
        );
        SortStrategy<Student> strategy = new EvenOddSortStrategy<Student>(
                new BubbleSortStrategy<Student>(),
                Student::getGroupNumber,
                "groupNumber"
        );
        strategy.sort(data, new GroupNumberComparator());

        MyList<Integer> expected = myListOf(2, 4, 6, 8);
        check("EvenOdd: только чётные — все сортируются",
                groupNumbersOf(data).equals(expected));
    }

    private static void testEvenOddRejectsNullDelegate() {
        boolean thrown = false;
        try {
            new EvenOddSortStrategy<Student>(
                    null,
                    Student::getGroupNumber,
                    "groupNumber"
            );
        } catch (IllegalArgumentException exception) {
            thrown = true;
        }
        check("EvenOdd: null-делегат отклонён", thrown);
    }

    private static void testEvenOddRejectsNullExtractor() {
        boolean thrown = false;
        try {
            new EvenOddSortStrategy<Student>(
                    new QuickSortStrategy<Student>(),
                    null,
                    "groupNumber"
            );
        } catch (IllegalArgumentException exception) {
            thrown = true;
        }
        check("EvenOdd: null-экстрактор отклонён", thrown);
    }

    private static void testEvenOddRejectsBlankFieldName() {
        boolean thrown = false;
        try {
            new EvenOddSortStrategy<Student>(
                    new QuickSortStrategy<Student>(),
                    Student::getGroupNumber,
                    "   "
            );
        } catch (IllegalArgumentException exception) {
            thrown = true;
        }
        check("EvenOdd: пустое имя поля отклонено", thrown);
    }

    private static Student student(int groupNumber, double gpa, String recordBookNumber) {
        return new Student.Builder()
                .setGroupNumber(groupNumber)
                .setGpa(gpa)
                .setRecordBookNumber(recordBookNumber)
                .build();
    }

    private static MyList<Integer> groupNumbersOf(MyList<Student> data) {
        MyList<Integer> result = new MyArrayList<Integer>();
        for (Student student : data) {
            result.add(student.getGroupNumber());
        }
        return result;
    }

    private static MyList<Integer> toList(int[] source) {
        MyList<Integer> result = new MyArrayList<Integer>(source.length);
        for (int value : source) {
            result.add(value);
        }
        return result;
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            passedTests++;
            System.out.println("PASS: " + testName);
        } else {
            failedTests++;
            System.out.println("FAIL: " + testName);
        }
    }

    @SafeVarargs
    private static <T> MyList<T> myListOf(T... elements) {
        MyList<T> list = new MyArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }
}
