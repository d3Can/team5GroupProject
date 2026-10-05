package com.team5.strategy;

import java.util.Comparator;
import java.util.List;

public final class InsertionSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);
        int size = data.size();
        for (int i = 1; i < size; i++) {
            T current = data.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(data.get(j), current) > 0) {
                data.set(j + 1, data.get(j));
                j--;
            }
            data.set(j + 1, current);
        }
    }

    @Override
    public String getName() {
        return "Insertion sort";
    }
}