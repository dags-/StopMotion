package me.dags.animations.entity;

import me.dags.animations.Animations;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimedEntityTask implements Runnable {

    private final Animations plugin;
    private final Map<UUID, Long> times = new HashMap<>();

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

                if (!hasTimeChanged(origin.getExtent())) {
                    return;
                }

                if (!origin.getExtent().getChunk(origin.getChunkPosition()).isPresent()) {
                    return;
                }

                if (instance.getRule().test(origin.getExtent())) {
                    Animations.debug("Applying entity animation");
                    instance.apply(origin);
                } else {
                    Animations.debug("Removing entity animation");
                    instance.remove();
                }
            });
        }
    }

    private boolean hasTimeChanged(World world) {
        long last = times.getOrDefault(world.getUniqueId(), -5000L);
        if (last != world.getProperties().getWorldTime()) {
            times.put(world.getUniqueId(), world.getProperties().getWorldTime());
            return true;
        }
        return false;
    }

    public void start() {
        Task.builder().execute(this).delayTicks(5).interval(5, TimeUnit.SECONDS).submit(plugin);
    }
}
