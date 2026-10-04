package com.team5.collection;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class CustomList<T> implements Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    public void add(T element) {
    }

    public int size() {
        return 0;
    }
}
