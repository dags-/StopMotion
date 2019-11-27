package me.dags.animations.util.worker;

import me.dags.animations.frame.Frame;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;

public class FrameWorker implements Worker {

    private final Location<World> origin;
    private final Iterator<Frame> iterator;

    private Frame frame = Frame.EMPTY;
    private long timestamp = 0L;

    public FrameWorker(Location<World> origin, Iterator<Frame> iterator) {
        this.origin = origin;
        this.iterator = iterator;
    }

    @Override
    public boolean hasWork(long now) {
        if (timestamp > now) {
            return true;
        }
        if (iterator.hasNext()) {
            frame = iterator.next();
            timestamp = 0L;
            return true;
        }
        return false;
    }

    @Override
    public void work(long now) {
        if (frame.isAbsent()) {
            return;
        }

        if (timestamp > 0L) {
            return;
        }

        if (iterator.hasNext()) {
            frame.applyTransient(origin);
        } else {
            frame.apply(origin);
        }

        timestamp = now + frame.getDuration().getMS();
    }
}
