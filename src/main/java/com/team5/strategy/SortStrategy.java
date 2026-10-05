package com.team5.strategy;

import java.util.Comparator;
import java.util.List;

public interface SortStrategy<T> {

    void sort(List<T> data, Comparator<T> comparator);

    String getName();
}