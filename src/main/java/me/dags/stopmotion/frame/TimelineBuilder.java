package me.dags.stopmotion.frame;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableList;
import me.dags.pitaya.util.SchemHelper;
import me.dags.pitaya.util.duration.Duration;
import me.dags.pitaya.util.optional.Result;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.LinkedList;

public class TimelineBuilder {

    public final LinkedList<Frame> frames = new LinkedList<>();

    public void add(Location<World> origin, Vector3i pos1, Vector3i pos2, Duration duration) {
        Schematic schematic = SchemHelper.createLocal(origin, pos1, pos2);
        frames.add(new Frame(schematic, duration));
    }

    public Result<Timeline, String> build(String name) {
        if (frames.isEmpty()) {
            return Result.fail("No frames have been added to the timeline");
        }
        return Result.pass(new Timeline(name, ImmutableList.copyOf(frames)));
    }
}
