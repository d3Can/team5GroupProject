package com.team5.model;

import com.team5.model.comparator.GroupNumberComparator;
import com.team5.model.comparator.GpaComparator;
import com.team5.model.comparator.RecordBookNumberComparator;

/**
 * Ручной тестовый набор для {@link Student}, валидации Builder
 * и компараторов по полям.
 * <p>
 * Запуск:
 * <pre>
 * java com.team5.model.StudentTest
 * </pre>
 * </p>
 */
public class StudentTest {
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        testBuilderCreatesValidStudent();
        testBuilderRejectsNegativeGroupNumber();
        testBuilderRejectsInvalidGpa();
        testBuilderRejectsBlankRecordBookNumber();
        testGroupNumberComparator();
        testGpaComparator();
        testRecordBookNumberComparator();
        testNaturalOrdering();

        System.out.println();
        System.out.println("Пройдено тестов: " + passedTests);
        System.out.println("Провалено тестов: " + failedTests);

        if (failedTests > 0) {
            System.exit(1);
        }
    }

    private static void testBuilderCreatesValidStudent() {
        try {
            Student student = Student.builder()
                    .groupNumber(101)
                    .gpa(4.8)
                    .recordBookNumber("ZB-9912")
                    .build();

            boolean fieldsCorrect = student.getGroupNumber() == 101
                    && student.getGpa() == 4.8
                    && "ZB-9912".equals(student.getRecordBookNumber());

            if (fieldsCorrect) {
                passedTests++;
                System.out.println("PASS: testBuilderCreatesValidStudent");
            } else {
                failedTests++;
                System.out.println("FAIL: testBuilderCreatesValidStudent");
            }
        } catch (Exception exception) {
            failedTests++;
            System.out.println(
                    "FAIL: testBuilderCreatesValidStudent - "
                            + exception.getMessage()
            );
        }
    }

    private static void testBuilderRejectsNegativeGroupNumber() {
        try {
            Student.builder()
                    .groupNumber(-1)
                    .gpa(4.0)
                    .recordBookNumber("ZB-0001")
                    .build();
            failedTests++;
            System.out.println("FAIL: testBuilderRejectsNegativeGroupNumber");
        } catch (IllegalArgumentException exception) {
            passedTests++;
            System.out.println("PASS: testBuilderRejectsNegativeGroupNumber");
        } catch (Exception exception) {
            failedTests++;
            System.out.println(
                    "FAIL: testBuilderRejectsNegativeGroupNumber - "
                            + exception.getClass().getSimpleName()
            );
        }
    }

    private static void testBuilderRejectsInvalidGpa() {
        try {
            Student.builder()
                    .groupNumber(101)
                    .gpa(6.0)
                    .recordBookNumber("ZB-0001")
                    .build();
            failedTests++;
            System.out.println("FAIL: testBuilderRejectsInvalidGpa");
        } catch (IllegalArgumentException exception) {
            passedTests++;
            System.out.println("PASS: testBuilderRejectsInvalidGpa");
        } catch (Exception exception) {
            failedTests++;
            System.out.println(
                    "FAIL: testBuilderRejectsInvalidGpa - "
                            + exception.getClass().getSimpleName()
            );
        }
    }

    private static void testBuilderRejectsBlankRecordBookNumber() {
        try {
            Student.builder()
                    .groupNumber(101)
                    .gpa(4.0)
                    .recordBookNumber("   ")
                    .build();
            failedTests++;
            System.out.println("FAIL: testBuilderRejectsBlankRecordBookNumber");
        } catch (IllegalArgumentException exception) {
            passedTests++;
            System.out.println("PASS: testBuilderRejectsBlankRecordBookNumber");
        } catch (Exception exception) {
            failedTests++;
            System.out.println(
                    "FAIL: testBuilderRejectsBlankRecordBookNumber - "
                            + exception.getClass().getSimpleName()
            );
        }
    }

    private static void testGroupNumberComparator() {
        Student first = Student.builder()
                .groupNumber(101)
                .gpa(4.0)
                .recordBookNumber("ZB-0001")
                .build();
        Student second = Student.builder()
                .groupNumber(102)
                .gpa(4.0)
                .recordBookNumber("ZB-0002")
                .build();

        if (new GroupNumberComparator().compare(first, second) < 0) {
            passedTests++;
            System.out.println("PASS: testGroupNumberComparator");
        } else {
            failedTests++;
            System.out.println("FAIL: testGroupNumberComparator");
        }
    }

    private static void testGpaComparator() {
        Student first = Student.builder()
                .groupNumber(101)
                .gpa(4.5)
                .recordBookNumber("ZB-0001")
                .build();
        Student second = Student.builder()
                .groupNumber(101)
                .gpa(3.5)
                .recordBookNumber("ZB-0002")
                .build();

        if (new GpaComparator().compare(first, second) > 0) {
            passedTests++;
            System.out.println("PASS: testGpaComparator");
        } else {
            failedTests++;
            System.out.println("FAIL: testGpaComparator");
        }
    }

    private static void testRecordBookNumberComparator() {
        Student first = Student.builder()
                .groupNumber(101)
                .gpa(4.0)
                .recordBookNumber("ZB-0001")
                .build();
        Student second = Student.builder()
                .groupNumber(101)
                .gpa(4.0)
                .recordBookNumber("ZB-0002")
                .build();

        if (new RecordBookNumberComparator().compare(first, second) < 0) {
            passedTests++;
            System.out.println("PASS: testRecordBookNumberComparator");
        } else {
            failedTests++;
            System.out.println("FAIL: testRecordBookNumberComparator");
        }
    }

    private static void testNaturalOrdering() {
        Student first = Student.builder()
                .groupNumber(101)
                .gpa(4.0)
                .recordBookNumber("ZB-0002")
                .build();
        Student second = Student.builder()
                .groupNumber(101)
                .gpa(4.0)
                .recordBookNumber("ZB-0001")
                .build();

        if (first.compareTo(second) > 0) {
            passedTests++;
            System.out.println("PASS: testNaturalOrdering");
        } else {
            failedTests++;
            System.out.println("FAIL: testNaturalOrdering");
        }
    }
}
