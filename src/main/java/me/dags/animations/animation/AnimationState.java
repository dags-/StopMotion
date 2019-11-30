package me.dags.animations.animation;

import me.dags.animations.frame.Frame;
import me.dags.animations.instance.Instance;
import me.dags.animations.util.iterator.Direction;
import me.dags.animations.worker.FrameWorker;
import me.dags.animations.worker.QueueWorker;
import me.dags.animations.worker.Worker;
import me.dags.pitaya.task.Promise;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AnimationState {

    private final AtomicInteger state = new AtomicInteger(0);

    public AnimationState(int state) {
        this.state.set(state);
    }

    public int getState() {
        return state.get();
    }

    public Promise<Worker> nextWorker(Instance instance) {
        return instance.getAnimation().getTimeline().then(timeline -> {
            Location<World> origin = instance.getLocation().orElseThrow(() -> new AnimationException("origin not valid"));
            if (instance.getAnimationMode() == AnimationMode.PUSH_PULL) {
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
        for (int i = directions.size() - 1; i >= 0; i--) {
            Iterator<Frame> iterator = directions.get(i).opposite().iterate(timeline.getFrames());
            workers.add(new FrameWorker(origin, iterator));
        }
        return new QueueWorker(workers);
    }
}
