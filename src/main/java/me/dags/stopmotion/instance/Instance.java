package me.dags.stopmotion.instance;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableList;
import me.dags.pitaya.task.Promise;
import me.dags.pitaya.util.region.Positioned;
import me.dags.stopmotion.animation.Animation;
import me.dags.stopmotion.animation.AnimationMode;
import me.dags.stopmotion.attachment.Attachment;
import me.dags.stopmotion.trigger.Trigger;
import me.dags.stopmotion.util.iterator.Direction;
import me.dags.stopmotion.worker.Worker;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Instance implements CatalogType, Positioned {

    private final String name;
    private final String world;
    private final Vector3i origin;
    private final Animation animation;
    private final AnimationMode mode;
    private final List<Trigger> triggers;
    private final List<Direction> directions;
    private final AtomicBoolean lock = new AtomicBoolean(false);

    private transient final InstanceState state;

    public Instance(String name, InstanceBuilder builder) {
        this.name = name;
        this.world = builder.world;
        this.origin = builder.origin;
        this.animation = builder.animation;
        this.mode = builder.mode;
        this.state = new InstanceState(builder.state);
        this.triggers = ImmutableList.copyOf(builder.triggers);
        this.directions = ImmutableList.copyOf(builder.directions);
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

    public int getState() {
        return state.getState();
    }

    public AnimationMode getAnimationMode() {
        return mode;
    }

    public Vector3i getOrigin() {
        return origin;
    }

    public boolean isLocked() {
        return lock.get();
    }

    public void setLocked(boolean locked) {
        lock.set(locked);
    }

    public void attach(Attachment attachment) {
        state.attach(attachment);
    }

    public Optional<Location<World>> getLocation() {
        return Sponge.getServer().getWorld(getWorld()).map(world -> world.getLocation(getOrigin()));
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public Promise<Worker> getWorker() {
        return state.nextWorker(this);
    }
}
