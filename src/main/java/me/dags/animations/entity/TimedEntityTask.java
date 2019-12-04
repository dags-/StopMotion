package me.dags.animations.entity;

import me.dags.animations.Animations;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class TimedEntityTask implements Runnable {

    private final Animations plugin;

    public TimedEntityTask(Animations plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (EntityInstance instance : plugin.getEntities().getAll()) {
            instance.getOrigin().ifPresent(origin -> {
                if (!origin.getExtent().isLoaded()) {
                    return;
                }

                if (!origin.getExtent().getChunk(origin.getChunkPosition()).isPresent()) {
                    return;
                }

                if (instance.getRule().test(origin.getExtent())) {
                    instance.apply(origin);
                } else {
                    instance.remove();
                }
            });
        }
    }

    public void start() {
        Task.builder().execute(this).interval(5, TimeUnit.SECONDS).submit(plugin);
    }
}
