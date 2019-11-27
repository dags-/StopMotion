package me.dags.animations.frame.iterator;

import java.util.Iterator;
import java.util.List;

public class ForwardIterator<T> implements Iterator<T> {

    private final List<T> values;
    private final int max;
    private final int start;

    private int index;

    public ForwardIterator(List<T> values) {
        this(values, 0);
    }

    public ForwardIterator(List<T> values, int start) {
        this.start = start;
        this.values = values;
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
}
