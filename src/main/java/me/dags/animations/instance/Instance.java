package me.dags.animations.instance;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableList;
import me.dags.animations.animation.Animation;
import me.dags.animations.animation.AnimationMode;
import me.dags.animations.animation.AnimationState;
import me.dags.animations.frame.iterator.Direction;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.util.worker.Worker;
import me.dags.pitaya.util.region.Positioned;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;

public class Instance implements CatalogType, Positioned {

    private final String name;
    private final String world;
    private final Vector3i origin;
    private final Animation animation;
    private final AnimationMode mode;
    private final List<Trigger> triggers;
    private final List<Direction> timeline;

    private transient final AnimationState state = new AnimationState();

    public Instance(InstanceBuilder builder) {
        this.name = builder.name;
        this.world = builder.world;
        this.origin = builder.origin;
        this.animation = builder.animation;
        this.mode = builder.mode;
        this.triggers = ImmutableList.copyOf(builder.triggers);
        this.timeline = ImmutableList.copyOf(builder.directions);
    }

    @Override
    public String toString() {
        return "Instance{" + getId() + "}";
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Vector3i getPosition() {
        return getOrigin();
    }

    public String getWorld() {
        return world;
    }

    public Animation getAnimation() {
        return animation;
    }

    public AnimationMode getAnimationMode() {
        return mode;
    }

    public Vector3i getOrigin() {
        return origin;
    }

    public Optional<Location<World>> getLocation() {
        return Sponge.getServer().getWorld(getWorld()).map(world -> world.getLocation(getOrigin()));
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<Direction> getDirections() {
        return timeline;
    }

    public Optional<Worker> getWorker() {
        return state.nextWorker(this);
    }
}
