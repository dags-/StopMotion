package me.dags.motion.instance;

import me.dags.motion.animation.AnimationException;
import me.dags.motion.animation.AnimationMode;
import me.dags.motion.animation.Timeline;
import me.dags.motion.entity.EntityInstance;
import me.dags.motion.frame.Frame;
import me.dags.motion.util.iterator.Direction;
import me.dags.motion.worker.*;
import me.dags.pitaya.task.Promise;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InstanceState {

    private final AtomicInteger state = new AtomicInteger(0);
    private final List<EntityInstance> linkedEntities = new LinkedList<>();

    public InstanceState(int state) {
        this.state.set(state);
    }

    public synchronized void addEntity(EntityInstance instance) {
        linkedEntities.add(instance);
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
                    return push(origin, instance, timeline);
                }
                if (stateValue == 1) {
                    state.set(0);
                    return pull(origin, instance, timeline);
                }
            }
            return single(origin, instance, timeline);
        });
    }

    private synchronized Worker single(Location<World> origin, Instance instance, Timeline timeline) {
        List<Worker> workers = new LinkedList<>();
        List<EntityInstance> entities = new LinkedList<>(linkedEntities);
        // remove linked entities at start of animation
        workers.add(new EntityRemovalWorker(entities));
        // added the frames
        forwards(origin, timeline, instance.getDirections(), workers);
        // re-apply the linked entities at end of animation
        workers.add(new EntityPlaceWorker(entities));
        return new QueueWorker(workers);
    }

    private Worker push(Location<World> origin, Instance instance, Timeline timeline) {
        List<Worker> workers = new LinkedList<>();
        List<EntityInstance> entities = new LinkedList<>(linkedEntities);
        // remove linked entities at start of animation
        workers.add(new EntityRemovalWorker(entities));
        // added the frames
        forwards(origin, timeline, instance.getDirections(), workers);
        return new QueueWorker(workers);
    }

    private Worker pull(Location<World> origin, Instance instance, Timeline timeline) {
        List<Worker> workers = new LinkedList<>();
        List<EntityInstance> entities = new LinkedList<>(linkedEntities);
        // added the frames
        backwards(origin, timeline, instance.getDirections(), workers);
        // re-apply the linked entities at end of animation
        workers.add(new EntityPlaceWorker(entities));
        return new QueueWorker(workers);
    }

    private void forwards(Location<World> origin, Timeline timeline, List<Direction> directions, List<Worker> list) {
        for (Direction direction : directions) {
            Iterator<Frame> iterator = direction.iterate(timeline.getFrames());
            list.add(new FrameWorker(origin, iterator));
        }
    }

    private void backwards(Location<World> origin, Timeline timeline, List<Direction> directions, List<Worker> list) {
        for (int i = directions.size() - 1; i >= 0; i--) {
            Iterator<Frame> iterator = directions.get(i).opposite().iterate(timeline.getFrames());
            list.add(new FrameWorker(origin, iterator));
        }
    }
}
