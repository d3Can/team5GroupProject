package com.team5.strategy;

import java.util.Comparator;
import java.util.List;

public final class SelectionSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);
        int size = data.size();
        for (int i = 0; i < size - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (comparator.compare(data.get(j), data.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                SortStrategySupport.swap(data, i, minIndex);
            }
        }
    }

    @Override
    public String getName() {
        return "Selection sort";
    }
}