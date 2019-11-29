package me.dags.animations.command;

import me.dags.animations.Animations;
import me.dags.animations.animation.Animation;
import me.dags.animations.animation.AnimationMode;
import me.dags.animations.instance.InstanceBuilder;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.util.iterator.Direction;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class InstanceCommands extends Cache<InstanceBuilder> {

    private final Animations plugin;

    public InstanceCommands(Animations plugin) {
        super(5, TimeUnit.MINUTES, InstanceBuilder::new);
        this.plugin = plugin;
    }

    @Command("animation|anim origin")
    @Permission("animation.command.animation.origin")
    @Description("Set the origin (past position) for the animation")
    public void origin(@Src Player player) {
        must(player).origin(player.getLocation());
        Fmt.info("Set origin ").stress(player.getLocation().getBlockPosition()).tell(player);
    }

    @Command("animation|anim timeline <timeline>")
    @Permission("animation.command.animation.timeline")
    @Description("Set the timeline to play at this location")
    public void timeline(@Src Player player, Animation animation) {
        must(player).animation = animation;
        Fmt.info("Set animation ").stress(animation.getName()).tell(player);
    }

    @Command("animation|anim trigger <trigger...>")
    @Permission("animation.command.animation.trigger")
    @Description("Set the triggers that cause the animation to play")
    public void triggers(@Src Player player, Trigger... triggers) {
        must(player).trigger(triggers);
        Fmt.info("Set animation triggers").tell(player);
    }

    @Command("animation|anim mode <mode>")
    @Permission("animation.command.animation.mode")
    @Description("Set the playback mode for that the animation")
    public void mode(@Src Player player, AnimationMode mode) {
        must(player).mode = mode;
        Fmt.info("Set mode ").stress(mode).tell(player);
    }

    @Command("animation|anim direction <directions...>")
    @Permission("animation.command.animation.direction")
    @Description("Set the directions that the animation will play in")
    public void direction(@Src Player player, Direction... directions) {
        must(player).add(directions);
        Fmt.info("Set timeline").tell(player);
    }

    @Command("animation|anim create <name>")
    @Permission("animation.command.animation.create")
    @Description("Create an animation using the current configuration")
    public void save(@Src Player player, String name) {
        must(player).build(name).onPass(instance -> {
            plugin.getInstances().register(instance);
            Fmt.info("Successfully created animation ").stress(instance).tell(player);
        }).onFail(error -> {
            Fmt.error("Failed to create animation: %s", error).tell(player);
        });
    }

    @Command("animation|anim delete <name>")
    @Permission("animation.command.animation.delete")
    @Description("Delete the given animation")
    public void delete(@Src Player player, Animation animation) {
        plugin.getTriggers().delete(animation.getId());
        Fmt.info("Deleted animation ").stress(animation.getId()).tell(player);
    }

    @Command("animation|anim reload")
    @Permission("animation.command.animations.reload")
    @Description("Reload the animations plugin")
    public void reload(@Src Player player) {
        plugin.reload(null);
        Fmt.info("Animations reloaded").tell(player);
    }
}
