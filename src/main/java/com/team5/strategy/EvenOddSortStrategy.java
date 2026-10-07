package com.team5.strategy;

import com.team5.collection.MyArrayList;
import com.team5.collection.MyList;

import java.util.Comparator;
import java.util.function.ToIntFunction;

public final class EvenOddSortStrategy<T> implements SortStrategy<T> {

    private final SortStrategy<T> delegate;
    private final ToIntFunction<T> numericFieldExtractor;
    private final String fieldName;

    public EvenOddSortStrategy(SortStrategy<T> delegate,
                               ToIntFunction<T> numericFieldExtractor,
                               String fieldName) {
        if (delegate == null) {
            throw new IllegalArgumentException(
                    "Внутренняя стратегия не должна быть null"
            );
        }
        if (numericFieldExtractor == null) {
            throw new IllegalArgumentException(
                    "Экстрактор числового поля не должен быть null"
            );
        }
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Имя поля не должно быть пустым"
            );
        }
        this.delegate = delegate;
        this.numericFieldExtractor = numericFieldExtractor;
        this.fieldName = fieldName;
    }

    @Override
    public void sort(MyList<T> data, Comparator<T> comparator) {
        SortStrategySupport.validateArguments(data, comparator);

        MyList<T> evenElements = new MyArrayList<>();
        MyList<Integer> evenPositions = new MyArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (numericFieldExtractor.applyAsInt(data.get(i)) % 2 == 0) {
                evenElements.add(data.get(i));
                evenPositions.add(i);
            }
        }

        delegate.sort(evenElements, comparator);

        for (int i = 0; i < evenElements.size(); i++) {
            data.set(evenPositions.get(i), evenElements.get(i));
        }
    }

    @Override
    public String getName() {
        return delegate.getName() + " (even-odd by '" + fieldName + "')";
    }
}