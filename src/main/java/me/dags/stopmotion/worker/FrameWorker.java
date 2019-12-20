package me.dags.stopmotion.worker;

import me.dags.pitaya.schematic.history.History;
import me.dags.stopmotion.frame.Frame;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;

public class FrameWorker extends IterateWorker<Frame> {

    private final Location<World> origin;

    private History history = History.NONE;

    public FrameWorker(Location<World> origin, Iterator<Frame> iterator) {
        super(iterator);
        this.origin = origin;
    }

    @Override
    protected void apply(Frame frame) {
        // remove any entities that have previously been placed
        history.entities(Entity::remove);

        // apply and store history
        history = frame.applyKeyFrame(origin);
    }

    @Override
    protected void applyTransient(Frame frame) {
        if (isFirst()) {
            // if first frame, remove any entities that may exist inside the frame area
            frame.init(origin);
        } else {
            // remove any entities that have previously been placed
            history.entities(Entity::remove);
        }

        // apply and store history
        history = frame.applyTransientFrame(origin);
    }
}
