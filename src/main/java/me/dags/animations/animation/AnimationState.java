package me.dags.animations.animation;

import me.dags.animations.animation.AnimationMode;
import me.dags.animations.animation.Timeline;
import me.dags.animations.frame.Frame;
import me.dags.animations.instance.Instance;
import me.dags.animations.util.iterator.Direction;
import me.dags.animations.util.iterator.Iterator;
import me.dags.animations.util.optional.BiOptional;
import me.dags.animations.util.worker.FrameWorker;
import me.dags.animations.util.worker.QueueWorker;
import me.dags.animations.util.worker.Worker;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class AnimationState {

    private final AnimationMode mode;
    private final AtomicInteger state = new AtomicInteger();

    public AnimationState(AnimationMode mode) {
        this.mode = mode;
    }

    public AnimationMode getMode() {
        return mode;
    }

    public Optional<Worker> nextWorker(Instance instance) {
        return BiOptional.of(instance.getLocation(), instance.getAnimation().getTimeline()).map((origin, timeline) -> {
            if (mode == AnimationMode.PUSH_PULL) {
                int stateValue = state.get();
                if (stateValue == 0) {
                    state.set(1);
                    return forwards(origin, timeline, instance.getDirections());
                }
                if (stateValue == 1) {
                    state.set(0);
                    return backwards(origin, timeline, instance.getDirections());
                }
            }
            return forwards(origin, timeline, instance.getDirections());
        });
    }

    private Worker forwards(Location<World> origin, Timeline timeline, List<Direction> directions) {
        List<Worker> workers = new LinkedList<>();
        for (Direction direction : directions) {
            Iterator<Frame> iterator = direction.iterate(timeline.getFrames());
            workers.add(new FrameWorker(origin, iterator));
        }
        return new QueueWorker(workers);
    }

    private Worker backwards(Location<World> origin, Timeline timeline, List<Direction> directions) {
        List<Worker> workers = new LinkedList<>();
        for (int i = directions.size() - 1; i > 0; i--) {
            Direction direction = directions.get(i);
            Iterator<Frame> iterator = direction.iterate(timeline.getFrames());
            workers.add(new FrameWorker(origin, iterator));
        }
        return new QueueWorker(workers);
    }
}
