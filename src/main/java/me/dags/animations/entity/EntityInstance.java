package me.dags.animations.entity;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import me.dags.animations.trigger.rule.Time;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntityInstance implements CatalogType {

    private final Time time;
    private final String name;
    private final String world;
    private final String animation;
    private final Vector3d position;
    private final List<EntityArchetype> entities;

    private final EntityState state = new EntityState();

    public EntityInstance(EntityInstanceBuilder builder) {
        this.time = builder.rule;
        this.name = builder.name;
        this.world = builder.world;
        this.animation = builder.link;
        this.position = builder.origin;
        this.state.addAll(builder.state);
        this.entities = ImmutableList.copyOf(builder.entities);
    }

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getLink() {
        return animation;
    }

    public String getWorldName() {
        return world;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Time getRule() {
        return time;
    }

    public Collection<UUID> getState() {
        return state.getUuids();
    }

    public List<EntityArchetype> getEntities() {
        return entities;
    }

    public Optional<World> getWorld() {
        return Sponge.getServer().getWorld(world);
    }

    public Optional<Location<World>> getOrigin() {
        return getWorld().map(world -> world.getLocation(position));
    }

    public void setLock(boolean lock) {
        state.setLock(lock);
    }

    public boolean apply(Location<World> origin) {
        return state.apply(origin, entities);
    }

    public boolean remove() {
        return getOrigin().map(state::remove).orElse(false);
    }
}
