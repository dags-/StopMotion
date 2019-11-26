package me.dags.animations.util.iterator;

public class PushPullIterator<T> implements Iterator<T> {

    private final Iterator<T> forward;
    private final Iterator<T> backward;

    public PushPullIterator(Iterator<T> timeline) {
        this.forward = timeline;
        this.backward = timeline.reverse();
    }

    @Override
    public boolean hasNext() {
        return forward.hasNext() || backward.hasNext();
    }

    @Override
    public T next() {
        if (forward.hasNext()) {
            return forward.next();
        }
        return backward.next();
    }

    @Override
    public void reset() {
        forward.reset();
        backward.reset();
    }

    @Override
    public Iterator<T> reverse() {
        return new PushPullIterator<>(backward);
    }
}
