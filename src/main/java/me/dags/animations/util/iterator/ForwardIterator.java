package me.dags.animations.util.iterator;

import java.util.ArrayList;
import java.util.List;

public class ForwardIterator<T> implements Iterator<T> {

    private final List<T> values;
    private final int max;
    private final int start;
    private int index = 0;

    public ForwardIterator(List<T> values) {
        this(values, -1);
    }

    public ForwardIterator(List<T> values, int start) {
        this.values = new ArrayList<>(values);
        this.start = start;
        this.max = values.size() - 1;
        this.index = Math.max(-1, start - 1);
    }

    @Override
    public boolean hasNext() {
        return index < max;
    }

    @Override
    public T next() {
        return values.get(++index);
    }

    @Override
    public void reset() {
        index = start;
    }

    @Override
    public Iterator<T> reverse() {
        return new BackwardIterator<>(values, values.size());
    }
}
