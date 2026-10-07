package com.team5.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
public class MyArrayList<T> implements MyList<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    public MyArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }
    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                    "Начальная ёмкость не может быть отрицательной: " + initialCapacity);
        }
        this.elements = new Object[Math.max(initialCapacity, 1)];
        this.size = 0;
    }

    @Override
    public void add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
    }
    @Override
    public void add(int index, T element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }
    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }
    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        checkIndex(index);
        T old = (T) elements[index];
        elements[index] = element;
        return old;
    }
    @SuppressWarnings("unchecked")
    @Override
    public T remove(int index) {
        checkIndex(index);
        T old = (T) elements[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(elements, index + 1, elements, index, moved);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx < 0) {
            return false;
        }
        remove(idx);
        return true;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }
    @Override
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }

    @Override
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                return i;
            }
        }
        return -1;
    }
    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1) + 1;
            if (newCapacity < requiredCapacity) {
                newCapacity = requiredCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс: " + index + ", размер: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Индекс: " + index + ", размер: " + size);
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }
    private class MyIterator implements Iterator<T> {

        private int cursor = 0;
        private int lastReturned = -1;
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Больше нет элементов");
            }
            lastReturned = cursor;
            canRemove = true;
            return (T) elements[cursor++];
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException(
                        "remove() можно вызвать только после next()");
            }
            MyArrayList.this.remove(lastReturned);
            cursor = lastReturned;
            lastReturned = -1;
            canRemove = false;
        }
    }
    @Override
    public Stream<T> stream() {
        Spliterator<T> spliterator = Spliterators.spliterator(
                iterator(),
                size,
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED
        );
        return StreamSupport.stream(spliterator, false);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append(']').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyList<?> other)) return false;
        if (this.size() != other.size()) return false;

        for (int i = 0; i < size; i++) {
            if (!Objects.equals(this.get(i), other.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            Object element = elements[i];
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }

    @Override
    public void addAll(MyList<? extends T> other) {
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }
}