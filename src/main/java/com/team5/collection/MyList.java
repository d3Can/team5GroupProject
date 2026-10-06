package com.team5.collection;

import java.util.stream.Stream;

public interface MyList<T> extends Iterable<T> {
    void add(T element);
    void add(int index, T element);
    T get(int index);
    T set(int index, T element);
    T remove(int index);
    boolean remove(T element);
    int size();
    boolean isEmpty();
    void clear();
    boolean contains(T element);
    int indexOf(T element);
    Stream<T> stream();
}