package me.dags.motion.worker;

import me.dags.motion.frame.Frame;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;

public class FrameWorker extends IterateWorker<Frame> {

    private final Location<World> origin;

    public FrameWorker(Location<World> origin, Iterator<Frame> iterator) {
        super(iterator);
        this.origin = origin;
    }

    @Override
    protected void apply(Frame frame) {
        frame.applyKeyFrame(origin);
    }

    @Override
    protected void applyTransient(Frame frame) {
        frame.applyTransientFrame(origin);
    }
}
