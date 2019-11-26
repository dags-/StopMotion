package me.dags.animations.util.iterator;

public class LoopingIterator<T> implements Iterator<T> {

    private final Iterator<T> timeline;
    private final int loops;

    private int loopCount = 0;

    public LoopingIterator(Iterator<T> timeline, int loops) {
        this.timeline = timeline;
        this.loops = Math.max(0, loops);
    }

    @Override
    public boolean hasNext() {
        return loops == -1 || loopCount < loops;
    }

    @Override
    public T next() {
        if (!timeline.hasNext()) {
            if (loopCount < loops) {
                loopCount++;
                timeline.reset();
            }
        }
        return timeline.next();
    }

    @Override
    public void reset() {
        loopCount = 0;
        timeline.reset();
    }

    @Override
    public Iterator<T> reverse() {
        return new LoopingIterator<>(timeline.reverse(), loops);
    }
}
