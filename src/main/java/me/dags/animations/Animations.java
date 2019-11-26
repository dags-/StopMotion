package me.dags.animations;

import com.google.inject.Inject;
import me.dags.animations.animation.AnimationManager;
import me.dags.animations.animation.AnimationRef;
import me.dags.animations.command.AnimationCommands;
import me.dags.animations.command.FrameCommands;
import me.dags.animations.command.InstanceCommands;
import me.dags.animations.instance.Instance;
import me.dags.animations.instance.InstanceManager;
import me.dags.pitaya.command.CommandBus;
import me.dags.pitaya.config.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin(id = "animations")
public class Animations {

    private final InstanceManager instances;
    private final AnimationManager animations;
    private final PlaybackManager playback = new PlaybackManager();
    private final Map<UUID, WorldListener> listeners = new HashMap<>();

    @Inject
    public Animations(@ConfigDir(sharedRoot = false) Path dir) {
        animations = new AnimationManager(dir.resolve("animations"));
        instances = new InstanceManager(Config.must(dir, "instances.conf"));
    }

    @Listener
    public void pre(GamePreInitializationEvent event) {
        Sponge.getRegistry().registerModule(Instance.class, instances);
        Sponge.getRegistry().registerModule(AnimationRef.class, animations);
        CommandBus.create()
                .register(new FrameCommands(this))
                .register(new InstanceCommands(this))
                .register(new AnimationCommands(this))
                .submit();
    }

    @Listener
    public void post(GamePostInitializationEvent event) {
        // instances need to load after animations
        instances.load();
        // sets up world listeners
        refreshListeners();
    }

    @Listener
    public void reload(GameReloadEvent event) {
        // load animations first
        animations.registerDefaults();
        // instances need to load after animations
        instances.load();
        // sets up world listeners
        refreshListeners();
    }

    @Listener
    public void unload(UnloadWorldEvent event) {
        // unregister listener for the given world
        WorldListener listener = listeners.remove(event.getTargetWorld().getUniqueId());
        if (listener != null) {
            Sponge.getEventManager().unregisterListeners(listener);
        }
    }

    @Listener
    public void stop(GameStoppingEvent event) {
        getPlaybackManager().cancelAll();
    }

    public AnimationManager getAnimations() {
        return animations;
    }

    public InstanceManager getInstances() {
        return instances;
    }

    public PlaybackManager getPlaybackManager() {
        return playback;
    }

    private void refreshListeners() {
        // unregister and dispose previous listeners
        listeners.values().forEach(Sponge.getEventManager()::unregisterListeners);
        listeners.clear();

        // sort instances by world
        Map<String, List<Instance>> instances = getInstances().getAll().stream().collect(Collectors.groupingBy(Instance::getWorld));

        // register listener for each world, assuming the world exists
        for (Map.Entry<String, List<Instance>> entry : instances.entrySet()) {
            Sponge.getServer().getWorld(entry.getKey()).ifPresent(world -> {
                WorldListener listener = new WorldListener(this, world.getUniqueId(), entry.getValue());
                Sponge.getEventManager().registerListeners(this, listener);
                listeners.put(world.getUniqueId(), listener);
            });
        }
    }
}
