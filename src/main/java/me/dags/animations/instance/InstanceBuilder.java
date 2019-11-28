package me.dags.animations.instance;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.Animation;
import me.dags.animations.animation.AnimationMode;
import me.dags.animations.frame.iterator.Direction;
import me.dags.animations.trigger.Trigger;
import me.dags.pitaya.util.optional.Result;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InstanceBuilder {

    public String name;
    public String world;
    public Vector3i origin;
    public Animation animation;
    public int state = 0;
    public AnimationMode mode = AnimationMode.SINGLE;
    public List<Trigger> triggers = new LinkedList<>();
    public List<Direction> directions = new LinkedList<>();

    public void origin(Location<World> origin) {
        this.world = origin.getExtent().getName();
        this.origin = origin.getBlockPosition();
    }

    public void trigger(Trigger... trigger) {
        Collections.addAll(triggers, trigger);
    }

    public void add(Direction... directions) {
        if (this.directions == null) {
            this.directions = new LinkedList<>();
        }
        Collections.addAll(this.directions, directions);
    }

    public Result<Instance, String> build(String name) {
        if (world == null || world.isEmpty()) {
            return Result.fail("World has not been set");
        }
        if (origin == null || origin == Vector3i.ZERO) {
            return Result.fail("Origin has not been set");
        }
        if (animation == null) {
            return Result.fail("Timeline has not been set");
        }
        if (triggers == null || triggers.isEmpty()) {
            return Result.fail("Trigger(s) has not been set");
        }
        if (directions == null || directions.isEmpty()) {
            directions = Collections.singletonList(Direction.FORWARD);
        }
        this.name = name;
        return Result.pass(new Instance(this));
    }
}
