package me.dags.animations.worker;

import me.dags.animations.entity.EntityInstance;

import java.util.List;

public class EntityRemovalWorker implements Worker {

    private final List<EntityInstance> entities;

    private boolean hasWork = true;

    public EntityRemovalWorker(List<EntityInstance> entities) {
        this.entities = entities;
    }

    @Override
    public boolean hasWork(long now) {
        return hasWork;
    }

    @Override
    public void work(long now) {
        hasWork = false;
        for (EntityInstance instance : entities) {
            instance.remove();
            instance.setLock(true);
        }
    }
}
