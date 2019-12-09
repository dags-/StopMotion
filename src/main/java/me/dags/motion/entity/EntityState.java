package me.dags.motion.entity;

import me.dags.motion.StopMotion;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntityState {

    private final Set<UUID> ids = new HashSet<>();
    private final AtomicBoolean lock = new AtomicBoolean(false);

    public synchronized boolean apply(Location<World> location, List<EntityArchetype> entities) {
        if (lock.get()) {
            StopMotion.debug("Cannot Apply - Locked");
            return false;
        }
        if (!ids.isEmpty()) {
            StopMotion.debug("Cannot Apply - Already present");
            return false;
        }
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
            for (EntityArchetype entity : entities) {
                entity.apply(location).ifPresent(e -> {
                    StopMotion.debug("Added entity: {}", e);
                    ids.add(e.getUniqueId());
                });
            }
        }
        return true;
    }

    public synchronized boolean remove(Location<World> location) {
        if (lock.get()) {
            StopMotion.debug("Cannot Remove - Locked");
            return false;
        }
        if (ids.isEmpty()) {
            StopMotion.debug("Cannot Remove - Nothing to remove");
            return false;
        }
        StopMotion.debug("Removing entities");
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            for (UUID id : ids) {
                location.getExtent().getEntity(id).ifPresent(Entity::remove);
            }
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
