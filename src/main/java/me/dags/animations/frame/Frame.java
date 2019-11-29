package me.dags.animations.frame;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.worker.Timed;
import me.dags.pitaya.util.duration.Duration;
import me.dags.pitaya.util.optional.OptionalValue;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.Schematic;

public class Frame implements OptionalValue, Timed {

    public static final Frame EMPTY = new Frame(null, null);

    private final Schematic schematic;
    private final Duration duration;

    public Frame(Schematic schematic, Duration duration) {
        this.schematic = schematic;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public long getDurationMS() {
        return getDuration().getMS();
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public void apply(Location<World> location) {
        World world = location.getExtent();
        Vector3i origin = location.getBlockPosition();
        schematic.getBlockWorker().iterate((volume, dx, dy, dz) -> {
            int x = origin.getX() + dx;
            int y = origin.getY() + dy;
            int z = origin.getZ() + dz;
            BlockState state = volume.getBlock(dx, dy, dz);
            world.resetBlockChange(x, y, z);
            world.setBlock(x, y, z, state);
        });
    }

    public void applyTransient(Location<World> location) {
        World world = location.getExtent();
        Vector3i origin = location.getBlockPosition();
        schematic.getBlockWorker().iterate((volume, dx, dy, dz) -> {
            int x = origin.getX() + dx;
            int y = origin.getY() + dy;
            int z = origin.getZ() + dz;
            BlockState state = volume.getBlock(dx, dy, dz);
            world.sendBlockChange(x, y, z, state);
        });
    }

    public void reset(Location<World> location) {
        World world = location.getExtent();
        Vector3i origin = location.getBlockPosition();
        schematic.getBlockWorker().iterate((volume, dx, dy, dz) -> {
            int x = origin.getX() + dx;
            int y = origin.getY() + dy;
            int z = origin.getZ() + dz;
            world.resetBlockChange(x, y, z);
        });
    }

    @Override
    public boolean isPresent() {
        return this != EMPTY;
    }
}
