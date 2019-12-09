package me.dags.motion.worker;

import me.dags.motion.entity.EntityInstance;

import java.util.List;

public class EntityPlaceWorker implements Worker {

    private final List<EntityInstance> entities;

    private boolean hasWork = true;

    public EntityPlaceWorker(List<EntityInstance> entities) {
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
            instance.setLock(false);
            instance.getOrigin().ifPresent(origin -> {
                if (instance.getRule().test(origin.getExtent())) {
                    instance.apply(origin);
                }
            });
        }
    }
}
