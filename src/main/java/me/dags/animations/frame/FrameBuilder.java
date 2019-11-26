package me.dags.animations.frame;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.AnimationData;
import me.dags.animations.util.duration.Duration;
import me.dags.animations.util.recorder.PosRecord;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ArchetypeVolume;
import org.spongepowered.api.world.schematic.PaletteTypes;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

public class FrameBuilder {

    public final PosRecord pos = new PosRecord();
    public final LinkedList<Frame> frames = new LinkedList<>();

    public void add(Duration duration) {
        Sponge.getServer().getWorld(pos.world).ifPresent(world -> {
            ArchetypeVolume volume = world.createArchetypeVolume(pos.pos1, pos.pos2, pos.pos1);

            Schematic schematic = Schematic.builder()
                    .blockPalette(PaletteTypes.LOCAL_BLOCKS.create())
                    .volume(volume)
                    .build();

            frames.add(new Frame(schematic, duration));
        });
    }

    public void undo(World world, int count) {
        while (frames.size() > 0) {
            Frame frame = frames.pollLast();
            if (frame != null) {
                Vector3i position = frame.getSchematic().getBlockMin();
                Location<World> location = new Location<>(world, position);
                frame.apply(location);
            }

            if (count-- < 0) {
                break;
            }
        }
    }

    public AnimationData build(String name) {
        return new AnimationData(name, frames);
    }
}
