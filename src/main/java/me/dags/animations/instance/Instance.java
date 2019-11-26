package me.dags.animations.instance;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.Animation;
import me.dags.animations.trigger.NamedTrigger;
import me.dags.animations.util.BiOptional;
import me.dags.animations.util.iterator.Direction;
import me.dags.animations.util.region.Positioned;
import me.dags.animations.util.worker.Worker;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Instance implements CatalogType, Positioned {

    private final String name;
    private final String world;
    private final Vector3i origin;
    private final Animation animation;
    private final List<NamedTrigger> triggers;
    private final List<Direction> timeline;

    public Instance(InstanceBuilder builder) {
        this.name = builder.name;
        this.world = builder.world;
        this.origin = builder.origin;
        this.animation = builder.animation;
        this.triggers = new ArrayList<>(builder.triggers);
        this.timeline = new ArrayList<>(builder.timeline);
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

    public String getAnimation() {
        return animation.getId();
    }

    public Vector3i getOrigin() {
        return origin;
    }

    public Optional<Location<World>> getLocation() {
        return Sponge.getServer().getWorld(getWorld()).map(world -> world.getLocation(getOrigin()));
    }

    public List<NamedTrigger> getTriggers() {
        return triggers;
    }

    public List<Direction> getTimeline() {
        return timeline;
    }

    public Optional<Worker> compile() {
        return BiOptional.of(getLocation(), animation.getAnimation())
                .map((origin, animation) -> animation.getTimeline(origin, getTimeline()));
    }
}
