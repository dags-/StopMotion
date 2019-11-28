package me.dags.animations.frame.iterator;

import java.util.Iterator;
import java.util.List;

public class BackwardIterator<T> implements Iterator<T> {

    private final List<T> values;

    private int index;

    public BackwardIterator(List<T> values) {
        this(values, values.size());
    }

    public BackwardIterator(List<T> values, int start) {
        this.values = values;
        this.index = Math.min(start, values.size());
    }

    @Override
    public boolean hasNext() {
        return index > 0;
    }

    @Override
    public T next() {
        return values.get(--index);
    }
}
