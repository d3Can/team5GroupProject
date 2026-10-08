package com.team5.model;

/**
 * Неизменяемый класс, представляющий данные студента.
 * <p>
 * Экземпляры создаются исключительно через {@link Builder}.
 * Естественный порядок сортировки определён по всем трём полям:
 * номер группы, средний балл, номер зачётной книжки.
 * </p>
 */
public final class Student implements Comparable<Student> {
    private final int groupNumber;
    private final double gpa;
    private final String recordBookNumber;

    private Student(Builder builder) {
        this.groupNumber = builder.groupNumber;
        this.gpa = builder.gpa;
        this.recordBookNumber = builder.recordBookNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public double getGpa() {
        return gpa;
    }

    public String getRecordBookNumber() {
        return recordBookNumber;
    }

    @Override
    public int compareTo(Student other) {
        int result = Integer.compare(this.groupNumber, other.groupNumber);
        if (result != 0) {
            return result;
        }
        result = Double.compare(this.gpa, other.gpa);
        if (result != 0) {
            return result;
        }
        return this.recordBookNumber.compareTo(other.recordBookNumber);
    }

    @Override
    public String toString() {
        return "Student{" +
                "groupNumber=" + groupNumber +
                ", gpa=" + gpa +
                ", recordBookNumber='" + recordBookNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return groupNumber == student.groupNumber
                && Double.compare(student.gpa, gpa) == 0
                && recordBookNumber.equals(student.recordBookNumber);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(groupNumber, gpa, recordBookNumber);
    }

    /**
     * Builder для создания экземпляров {@link Student} с валидацией.
     * <p>
     * Пример использования:
     * <pre>
     * Student student = Student.builder()
     *         .GroupNumber(101)
     *         .Gpa(4.8)
     *         .RecordBookNumber("ZB-9912")
     *         .build();
     * </pre>
     * </p>
     */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private static final double MIN_GPA = 0.0;
        private static final double MAX_GPA = 5.0;
        private static final int MIN_GROUP_NUMBER = 1;

        private int groupNumber;
        private double gpa;
        private String recordBookNumber;

        public Builder groupNumber(int groupNumber) {
            this.groupNumber = groupNumber;
            return this;
        }

        public Builder gpa(double gpa) {
            this.gpa = gpa;
            return this;
        }

        public Builder recordBookNumber(String recordBookNumber) {
            this.recordBookNumber = recordBookNumber;
            return this;
        }

        /**
         * Создаёт и валидирует экземпляр {@link Student}.
         *
         * @return неизменяемый объект Student
         * @throws IllegalArgumentException если валидация не пройдена
         */
        public Student build() {
            validate();
            return new Student(this);
        }

        private void validate() {
            if (groupNumber < MIN_GROUP_NUMBER) {
                throw new IllegalArgumentException(
                        "Номер группы должен быть >= " + MIN_GROUP_NUMBER
                );
            }
            if (gpa < MIN_GPA || gpa > MAX_GPA) {
                throw new IllegalArgumentException(
                        String.format(
                                "Средний балл должен быть от %.1f до %.1f",
                                MIN_GPA, MAX_GPA
                        )
                );
            }
            if (recordBookNumber == null || recordBookNumber.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Номер зачётной книжки не должен быть пустым"
                );
            }
        }
    }
}
