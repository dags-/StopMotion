package me.dags.animations.instance;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.Animation;
import me.dags.animations.trigger.NamedTrigger;
import me.dags.animations.util.iterator.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class InstanceBuilder {

    public String name;
    public String world;
    public Vector3i origin;
    public Animation animation;
    public List<NamedTrigger> triggers = new LinkedList<>();
    public List<Direction> timeline = new LinkedList<>();

    public void origin(Location<World> origin) {
        this.world = origin.getExtent().getName();
        this.origin = origin.getBlockPosition();
    }

    public void trigger(NamedTrigger... trigger) {
        Collections.addAll(triggers, trigger);
    }

    public void add(Direction... directions) {
        if (timeline == null) {
            timeline = new LinkedList<>();
        }
        Collections.addAll(timeline, directions);
    }

    public Optional<Instance> build(String name) {
        if (world == null || world.isEmpty()) {
            return Optional.empty();
        }
        if (origin == null || origin == Vector3i.ZERO) {
            return Optional.empty();
        }
        if (animation == null) {
            return Optional.empty();
        }
        if (triggers == null || triggers.isEmpty()) {
            return Optional.empty();
        }
        if (timeline == null || timeline.isEmpty()) {
            timeline = Collections.singletonList(Direction.FORWARD);
        }
        this.name = name;
        return Optional.of(new Instance(this));
    }
}
