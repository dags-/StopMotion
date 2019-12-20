package me.dags.stopmotion.frame;

import com.flowpowered.math.vector.Vector3i;
import me.dags.pitaya.schematic.PitSchematic;
import me.dags.pitaya.schematic.history.History;
import me.dags.pitaya.schematic.history.HistoryManager;
import me.dags.pitaya.util.duration.Duration;
import me.dags.pitaya.util.optional.OptionalValue;
import me.dags.stopmotion.worker.Timed;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class Frame implements OptionalValue, Timed {

    public static final Frame EMPTY = new Frame(null, null);
    public static final FrameTranslator TRANSLATOR = new FrameTranslator();

    private final PitSchematic schematic;
    private final Duration duration;

    public Frame(PitSchematic schematic, Duration duration) {
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

    public PitSchematic getSchematic() {
        return schematic;
    }

    public History applyKeyFrame(Location<World> location) {
        // undo any previous transient block changes
        clearTransientFrame(location);

        // record history for key frames
        try (History history = HistoryManager.push()) {
            schematic.apply(location, BlockChangeFlags.NONE);
            return history;
        }
    }

    public History applyTransientFrame(Location<World> location) {
        World world = location.getExtent();
        Vector3i origin = location.getBlockPosition();
        schematic.getBlockWorker().iterate((volume, dx, dy, dz) -> {
            int x = origin.getX() + dx;
            int y = origin.getY() + dy;
            int z = origin.getZ() + dz;
            BlockState state = volume.getBlock(dx, dy, dz);
            world.sendBlockChange(x, y, z, state);
        });
        return History.NONE;
    }

    public void init(Location<World> location) {
        Vector3i min = location.getBlockPosition().add(schematic.getBlockMin());
        Vector3i max = location.getBlockPosition().add(schematic.getBlockMax()).add(Vector3i.ONE);
        AABB bounds = new AABB(min, max);
        for (Entity entity : location.getExtent().getIntersectingEntities(bounds, e -> !(e instanceof Player))) {
            entity.remove();
        }
    }

    private void clearTransientFrame(Location<World> location) {
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
