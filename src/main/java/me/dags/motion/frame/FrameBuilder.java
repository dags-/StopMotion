package me.dags.motion.frame;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableList;
import me.dags.motion.animation.Timeline;
import me.dags.pitaya.util.duration.Duration;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ArchetypeVolume;
import org.spongepowered.api.world.schematic.PaletteTypes;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.LinkedList;

public class FrameBuilder {

    public final LinkedList<Frame> frames = new LinkedList<>();

    public void add(Location<World> origin, Vector3i pos1, Vector3i pos2, Duration duration) {
        ArchetypeVolume volume = origin.getExtent().createArchetypeVolume(pos1, pos2, origin.getBlockPosition());

        Schematic schematic = Schematic.builder()
                .blockPalette(PaletteTypes.LOCAL_BLOCKS.create())
                .volume(volume)
                .build();

        frames.add(new Frame(schematic, duration));
    }

    public void undo(World world, int count) {
        while (frames.size() > 0) {
            Frame frame = frames.pollLast();
            if (frame != null) {
                Vector3i position = frame.getSchematic().getBlockMin();
                Location<World> location = new Location<>(world, position);
                frame.applyKeyFrame(location);
            }

            if (count-- < 0) {
                break;
            }
        }
    }

    public Timeline build(String name) {
        return new Timeline(name, ImmutableList.copyOf(frames));
    }
}
