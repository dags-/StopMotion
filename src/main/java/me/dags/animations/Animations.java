package me.dags.animations;

import com.google.inject.Inject;
import me.dags.animations.animation.Animation;
import me.dags.animations.animation.AnimationManager;
import me.dags.animations.command.*;
import me.dags.animations.entity.EntityInstance;
import me.dags.animations.entity.EntityManager;
import me.dags.animations.entity.TimedEntityTask;
import me.dags.animations.instance.Instance;
import me.dags.animations.instance.InstanceManager;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerListener;
import me.dags.animations.trigger.TriggerManager;
import me.dags.pitaya.command.CommandBus;
import me.dags.pitaya.command.fmt.Format;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.text.Formats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.world.UnloadWorldEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin(id = "animations")
public class Animations {

    private static final Logger logger = LoggerFactory.getLogger("Animations");

    private final EntityManager entities;
    private final TriggerManager triggers;
    private final PlaybackManager playback;
    private final InstanceManager animations;
    private final AnimationManager timelines;
    private final Map<UUID, TriggerListener> listeners = new HashMap<>();

    @Inject
    public Animations(@ConfigDir(sharedRoot = false) Path dir) {
        playback = new PlaybackManager(this);
        timelines = new AnimationManager(dir.resolve("timelines"));
        entities = new EntityManager(Config.must(dir, "entities.conf"));
        triggers = new TriggerManager(Config.must(dir, "triggers.conf"));
        animations = new InstanceManager(Config.must(dir, "animations.conf"));
    }

    @Listener
    public void init(GameInitializationEvent event) {
        Formats.init(Animations::getDefaultFormat);

        Sponge.getRegistry().registerModule(Trigger.class, triggers);
        Sponge.getRegistry().registerModule(Instance.class, animations);
        Sponge.getRegistry().registerModule(Animation.class, timelines);
        Sponge.getRegistry().registerModule(EntityInstance.class, entities);

        CommandBus.create()
                .register(new AnimationCommands(this))
                .register(new TimelineCommands(this))
                .register(new TriggerCommands(this))
                .register(new EntityCommands(this))
                .register(new WandCommands())
                .submit();
    }

    @Listener
    public void started(GameStartedServerEvent event) {
        reload(null);
        new TimedEntityTask(this).start();
    }

    @Listener
    public void reload(GameReloadEvent event) {
        entities.load();
        triggers.load();
        entities.load();
        timelines.load();
        animations.load();
        entities.attachEntities();
        refreshListeners();
    }

    @Listener
    public void unload(UnloadWorldEvent event) {
        // unregister listener for the given world
        TriggerListener listener = listeners.remove(event.getTargetWorld().getUniqueId());
        if (listener != null) {
            Sponge.getEventManager().unregisterListeners(listener);
        }
    }

    @Listener
    public void stop(GameStoppingEvent event) {
        getPlaybackManager().cancelAll();
    }

    public EntityManager getEntities() {
        return entities;
    }

    public TriggerManager getTriggers() {
        return triggers;
    }

    public InstanceManager getAnimations() {
        return animations;
    }

    public AnimationManager getTimelines() {
        return timelines;
    }

    public PlaybackManager getPlaybackManager() {
        return playback;
    }

    private void refreshListeners() {
        // unregister and dispose previous listeners
        listeners.values().forEach(Sponge.getEventManager()::unregisterListeners);
        listeners.clear();

        // sort instances by world
        Map<String, List<Instance>> instances = getAnimations().getAll().stream().collect(Collectors.groupingBy(Instance::getWorld));
        // register listener for each world, assuming the world exists
        for (Map.Entry<String, List<Instance>> entry : instances.entrySet()) {
            Sponge.getServer().getWorld(entry.getKey()).ifPresent(world -> {
                debug("Registering trigger listener for world: {}", world.getName());
                TriggerListener listener = new TriggerListener(this, world.getUniqueId(), entry.getValue());
                Sponge.getEventManager().registerListeners(this, listener);
                listeners.put(world.getUniqueId(), listener);
            });
        }
    }

    public static void log(String message, Object... args) {
        logger.info(message, args);
    }

    public static void debug(String message, Object... args) {
        logger.debug(message, args);
    }


    private static Format getDefaultFormat() {
        return Format.builder()
                .warn(TextColors.RED)
                .info(TextColors.YELLOW)
                .stress(TextColors.DARK_AQUA)
                .error(TextColors.RED, TextStyles.ITALIC)
                .subdued(TextColors.YELLOW, TextStyles.ITALIC)
                .build();
    }
}
