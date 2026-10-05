package com.team5.strategy;

import java.util.Comparator;
import java.util.List;

final class SortStrategySupport {

    private SortStrategySupport() {
    }

    static void validateArguments(List<?> data, Comparator<?> comparator) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Список для сортировки не должен быть null"
            );
        }
        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Компаратор не должен быть null"
            );
        }
    }

    static <T> void swap(List<T> data, int firstIndex, int secondIndex) {
        T temporary = data.get(firstIndex);
        data.set(firstIndex, data.get(secondIndex));
        data.set(secondIndex, temporary);
    }
}