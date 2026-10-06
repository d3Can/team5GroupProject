package com.team5.strategy;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;
import java.util.Comparator;

final class SortStrategySupport {

    private SortStrategySupport() {
    }

    static void validateArguments(MyList<?> data, Comparator<?> comparator) {
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

    static <T> void swap(MyList<T> data, int firstIndex, int secondIndex) {
        T temporary = data.get(firstIndex);
        data.set(firstIndex, data.get(secondIndex));
        data.set(secondIndex, temporary);
    }

    static <T> MyList<T> subListCopy(MyList<T> source, int fromIndex, int toIndex) {
        MyList<T> result = new MyArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            result.add(source.get(i));
        }
        return result;
    }
}