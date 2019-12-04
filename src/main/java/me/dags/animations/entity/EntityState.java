package me.dags.animations.entity;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntityState {

    private final Set<UUID> ids = new HashSet<>();
    private final AtomicBoolean lock = new AtomicBoolean(false);

    public synchronized boolean apply(Location<World> location, List<EntityArchetype> entities) {
        if (lock.get()) {
            return false;
        }
        if (!ids.isEmpty()) {
            return false;
        }
        for (EntityArchetype entity : entities) {
            entity.apply(location).ifPresent(e -> ids.add(e.getUniqueId()));
        }
        return true;
    }

    public synchronized boolean remove(Location<World> location) {
        if (lock.get()) {
            return false;
        }
        if (!ids.isEmpty()) {
            return false;
        }
        for (UUID id : ids) {
            location.getExtent().getEntity(id).ifPresent(Entity::remove);
        }
        ids.clear();
        return true;
    }

    public synchronized void setLock(boolean state) {
        lock.set(state);
    }

    public synchronized boolean addAll(Collection<UUID> uuids) {
        ids.addAll(uuids);
        return true;
    }

    public synchronized Set<UUID> getUuids() {
        return ids;
    }
}
