package me.dags.animations.worker;

import me.dags.animations.frame.Frame;
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
        frame.apply(origin);
    }

    @Override
    protected void applyTransient(Frame frame) {
        frame.applyTransient(origin);
    }
}
