package com.team5.strategy;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;

import java.util.Comparator;

public final class MergeSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(MyList<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);
        if (data.size() < 2) {
            return;
        }
        MyList<T> sorted = mergeSort(
                SortStrategySupport.subListCopy(data, 0, data.size()),
                comparator
        );
        for (int i = 0; i < sorted.size(); i++) {
            data.set(i, sorted.get(i));
        }
    }

    private MyList<T> mergeSort(MyList<T> data, Comparator<T> comparator) {
        if (data.size() < 2) {
            return data;
        }
        int middle = data.size() / 2;
        MyList<T> left = mergeSort(
                SortStrategySupport.subListCopy(data,0, middle),
                comparator
        );
        MyList<T> right = mergeSort(
                SortStrategySupport.subListCopy(data, middle, data.size()),
                comparator
        );
        return merge(left, right, comparator);
    }

    private MyList<T> merge(MyList<T> left,
                            MyList<T> right,
                          Comparator<T> comparator) {
        MyList<T> result = new MyArrayList<>(left.size() + right.size());
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