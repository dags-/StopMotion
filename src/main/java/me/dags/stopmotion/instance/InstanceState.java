package me.dags.stopmotion.instance;

import me.dags.stopmotion.animation.AnimationException;
import me.dags.stopmotion.animation.AnimationMode;
import me.dags.stopmotion.attachment.Attachment;
import me.dags.stopmotion.attachment.AttachmentWorker;
import me.dags.stopmotion.frame.Frame;
import me.dags.stopmotion.frame.Timeline;
import me.dags.stopmotion.util.iterator.Direction;
import me.dags.stopmotion.worker.FrameWorker;
import me.dags.stopmotion.worker.QueueWorker;
import me.dags.stopmotion.worker.Worker;
import me.dags.pitaya.task.Promise;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InstanceState {

    private final AtomicInteger state = new AtomicInteger(0);
    private final List<Attachment> attachments = new LinkedList<>();

    public InstanceState(int state) {
        this.state.set(state);
    }

    public synchronized void attach(Attachment attachment) {
        attachments.add(attachment);
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
        List<Attachment> attachments = new LinkedList<>(this.attachments);
        // remove linked entities at start of animation
        workers.add(AttachmentWorker.remove(attachments));
        // added the frames
        forwards(origin, timeline, instance.getDirections(), workers);
        // re-apply the linked entities at end of animation
        workers.add(AttachmentWorker.apply(attachments));
        return new QueueWorker(workers);
    }

    private Worker push(Location<World> origin, Instance instance, Timeline timeline) {
        List<Worker> workers = new LinkedList<>();
        List<Attachment> attachments = new LinkedList<>(this.attachments);
        // remove linked entities at start of animation
        workers.add(AttachmentWorker.remove(attachments));
        // added the frames
        forwards(origin, timeline, instance.getDirections(), workers);
        return new QueueWorker(workers);
    }

    private Worker pull(Location<World> origin, Instance instance, Timeline timeline) {
        List<Worker> workers = new LinkedList<>();
        List<Attachment> attachments = new LinkedList<>(this.attachments);
        // added the frames
        backwards(origin, timeline, instance.getDirections(), workers);
        // re-apply the linked entities at end of animation
        workers.add(AttachmentWorker.apply(attachments));
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
