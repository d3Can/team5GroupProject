package com.team5.strategy;

import com.team5.collection.MyList;
import java.util.Comparator;

public interface SortStrategy<T> {

    void sort(MyList<T> data, Comparator<T> comparator);

    String getName();
}