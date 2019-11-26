package me.dags.animations.util.iterator;

public interface Iterator<T> {

    boolean hasNext();

    T next();

    void reset();

    Iterator<T> reverse();
}
