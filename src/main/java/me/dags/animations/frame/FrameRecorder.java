package me.dags.animations.frame;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.Animation;
import me.dags.animations.util.duration.Duration;
import me.dags.animations.util.iterator.BackwardIterator;
import me.dags.animations.util.iterator.ForwardIterator;
import me.dags.animations.util.worker.FrameWorker;
import me.dags.animations.util.worker.QueueWorker;
import me.dags.animations.util.worker.WorkerTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ArchetypeVolume;
import org.spongepowered.api.world.schematic.PaletteTypes;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

public class FrameRecorder {

    public String world = "";
    public Vector3i pos1 = Vector3i.ZERO;
    public Vector3i pos2 = Vector3i.ZERO;
    public LinkedList<Frame> frames = new LinkedList<>();

    public FrameRecorder(UUID id) {

    }

    public void add(Duration duration) {
        Sponge.getServer().getWorld(world).ifPresent(world -> {
            ArchetypeVolume volume = world.createArchetypeVolume(pos1, pos2, pos1);
            Schematic schematic = Schematic.builder()
                    .volume(volume)
                    .blockPalette(PaletteTypes.LOCAL_BLOCKS.create())
                    .build();
            frames.add(new Frame(schematic, duration));
        });
    }

    public Animation create(String name) {
        return new Animation(name, frames);
    }

    public void test() {
        Sponge.getServer().getWorld(world).ifPresent(world -> {
            Location<World> origin = new Location<>(world, pos1);
            WorkerTask task = new WorkerTask(new QueueWorker(Arrays.asList(
                    new FrameWorker(origin, new ForwardIterator<>(frames)),
                    new FrameWorker(origin, new BackwardIterator<>(frames, frames.size() - 1))
            )));
            task.start();
        });
    }

    public void reset() {
        Sponge.getServer().getWorld(world).ifPresent(world -> {
            Location<World> origin = new Location<>(world, pos1);
            frames.getLast().reset(origin);
        });
    }
}
