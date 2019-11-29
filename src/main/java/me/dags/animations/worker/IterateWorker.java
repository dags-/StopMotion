package me.dags.animations.worker;

import java.util.Iterator;

public abstract class IterateWorker<T extends Timed> implements Worker {

    private final Iterator<T> iterator;

    private T task = null;
    private long timestamp = 0L;
    private boolean first = true;

    public IterateWorker(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasWork(long now) {
        if (timestamp > now) {
            return true;
        }
        if (iterator.hasNext()) {
            task = iterator.next();
            timestamp = 0L;
            return true;
        }
        return false;
    }

    @Override
    public void work(long now) {
        if (task == null) {
            return;
        }

        if (timestamp > 0L) {
            return;
        }

        if (first) {
            applyTransient(task);
            first = false;
            timestamp = now;
            return;
        }

        if (iterator.hasNext()) {
            applyTransient(task);
        } else {
            apply(task);
        }

        timestamp = now + task.getDurationMS();
    }

    protected abstract void apply(T t);

    protected abstract void applyTransient(T t);
}
