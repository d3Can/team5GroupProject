package com.team5.strategy;

import java.util.Comparator;
import java.util.List;

public final class BubbleSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);
        int size = data.size();
        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - 1 - i; j++) {
                if (comparator.compare(data.get(j), data.get(j + 1)) > 0) {
                    SortStrategySupport.swap(data, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "Bubble sort";
    }
}