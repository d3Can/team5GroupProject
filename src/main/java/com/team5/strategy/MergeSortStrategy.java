package com.team5.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MergeSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);
        if (data.size() < 2) {
            return;
        }
        List<T> sorted = mergeSort(new ArrayList<>(data), comparator);
        for (int i = 0; i < sorted.size(); i++) {
            data.set(i, sorted.get(i));
        }
    }

    private List<T> mergeSort(List<T> data, Comparator<T> comparator) {
        if (data.size() < 2) {
            return data;
        }
        int middle = data.size() / 2;
        List<T> left = mergeSort(
                new ArrayList<>(data.subList(0, middle)),
                comparator
        );
        List<T> right = mergeSort(
                new ArrayList<>(data.subList(middle, data.size())),
                comparator
        );
        return merge(left, right, comparator);
    }

    private List<T> merge(List<T> left,
                          List<T> right,
                          Comparator<T> comparator) {
        List<T> result = new ArrayList<>(left.size() + right.size());
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }
        return result;
    }

    @Override
    public String getName() {
        return "Merge sort";
    }
}