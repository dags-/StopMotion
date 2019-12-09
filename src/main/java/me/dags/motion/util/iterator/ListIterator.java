package me.dags.motion.util.iterator;

import java.util.Iterator;
import java.util.List;

public class ListIterator<T> implements Iterator<T> {

    private final List<T> values;
    private final int max;

    private int index;

    public ListIterator(List<T> values) {
        this(values, -1);
    }

    public ListIterator(List<T> values, int start) {
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
