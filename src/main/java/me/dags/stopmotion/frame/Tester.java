package me.dags.stopmotion.frame;

import com.flowpowered.math.vector.Vector3i;
import me.dags.pitaya.schematic.SchemUtils;
import me.dags.pitaya.util.duration.Duration;
import me.dags.stopmotion.worker.DelayWorker;
import me.dags.stopmotion.worker.FrameWorker;
import me.dags.stopmotion.worker.QueueWorker;
import me.dags.stopmotion.worker.Worker;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.Arrays;

public class Tester {

    private final Player owner;
    private final Timeline timeline;

    public Tester(Player owner, Timeline timeline) {
        this.owner = owner;
        this.timeline = timeline;
    }

    public String getTaskId() {
        // use owner's id so they can't run more than one test at a time
        return owner.getIdentifier();
    }

    public Worker getWorker(Location<World> origin) {
        Tuple<Vector3i, Vector3i> bounds = timeline.getBounds();
        Vector3i min = origin.getBlockPosition().add(bounds.getFirst());
        Vector3i max = origin.getBlockPosition().add(bounds.getSecond());
        Schematic schematic = SchemUtils.createLocal(origin, min, max);
        return getWorker(origin, schematic);
    }

    private Worker getWorker(Location<World> origin, Schematic schematic) {
        Worker delay = new DelayWorker(() -> {}, Duration.secs(1));
        Worker frames = new FrameWorker(origin, timeline.getFrames().iterator());
        Worker restore = new DelayWorker(() -> schematic.apply(origin, BlockChangeFlags.NONE), Duration.secs(3));
        return new QueueWorker(Arrays.asList(delay, frames, restore));
    }
}
