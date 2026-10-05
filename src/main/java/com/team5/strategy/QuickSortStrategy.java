package com.team5.strategy;

import java.util.Comparator;
import java.util.List;

public final class QuickSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);
        quickSort(data, comparator, 0, data.size() - 1);
    }

    private void quickSort(List<T> data,
                           Comparator<T> comparator,
                           int low,
                           int high) {
        if (low >= high) {
            return;
        }
        int pivotIndex = partition(data, comparator, low, high);
        quickSort(data, comparator, low, pivotIndex - 1);
        quickSort(data, comparator, pivotIndex + 1, high);
    }

    private int partition(List<T> data,
                          Comparator<T> comparator,
                          int low,
                          int high) {
        T pivot = data.get(high);
        int boundary = low - 1;
        for (int i = low; i < high; i++) {
            if (comparator.compare(data.get(i), pivot) <= 0) {
                boundary++;
                SortStrategySupport.swap(data, boundary, i);
            }
        }
        SortStrategySupport.swap(data, boundary + 1, high);
        return boundary + 1;
    }

    @Override
    public String getName() {
        return "Quick sort";
    }
}