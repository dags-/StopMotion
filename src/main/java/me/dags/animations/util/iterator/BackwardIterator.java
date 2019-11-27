package me.dags.animations.util.iterator;

import java.util.List;

public class BackwardIterator<T> implements Iterator<T> {

    private final List<T> values;
    private final int start;

    private int index;

    public BackwardIterator(List<T> values) {
        this(values, values.size() - 1);
    }

    public BackwardIterator(List<T> values, int start) {
        this.start = start;
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

    @Override
    public void reset() {
        index = start;
    }

    @Override
    public Iterator<T> reverse() {
        return new ForwardIterator<>(values);
    }
}
