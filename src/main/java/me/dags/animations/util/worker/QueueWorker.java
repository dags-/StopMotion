package me.dags.animations.util.worker;

import me.dags.animations.util.iterator.ForwardIterator;
import me.dags.animations.util.iterator.Iterator;

import java.util.List;

public class QueueWorker implements Worker {

    private final Iterator<Worker> iterator;
    private Worker current = null;

    public QueueWorker(List<Worker> iterator) {
        this(new ForwardIterator<>(iterator));
    }

    public QueueWorker(Iterator<Worker> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasWork(long now) {
        if (current != null) {
            if (current.hasWork(now)) {
                return true;
            }
        }
        if (iterator.hasNext()) {
            current = iterator.next();
            return current.hasWork(now);
        }
        return false;
    }

    @Override
    public void work(long now) {
        if (current != null) {
            current.work(now);
        }
    }
}
