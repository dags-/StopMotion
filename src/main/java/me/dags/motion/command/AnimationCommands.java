package me.dags.motion.command;

import me.dags.motion.StopMotion;
import me.dags.motion.animation.Animation;
import me.dags.motion.animation.AnimationMode;
import me.dags.motion.instance.InstanceBuilder;
import me.dags.motion.trigger.Trigger;
import me.dags.motion.util.iterator.Direction;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AnimationCommands extends Cache<InstanceBuilder> {

    private final StopMotion plugin;

    public AnimationCommands(StopMotion plugin) {
        super(5, TimeUnit.MINUTES, InstanceBuilder::new);
        this.plugin = plugin;
    }

    @Command("animation|anim origin")
    @Permission("stopmotion.command.animation.origin")
    @Description("Set the origin (past position) for the animation")
    public void origin(@Src Player player) {
        must(player).origin(player.getLocation());
        Fmt.info("Set origin ").stress(player.getLocation().getBlockPosition()).tell(player);
    }

    @Command("animation|anim timeline <timeline>")
    @Permission("stopmotion.command.animation.timeline")
    @Description("Set the timeline to be used in this animation")
    public void timeline(@Src Player player, Animation animation) {
        must(player).animation = animation;
        Fmt.info("Set the animation timeline to ").stress(animation.getName()).tell(player);
    }

    @Command("animation|anim trigger <trigger...>")
    @Permission("stopmotion.command.animation.trigger")
    @Description("Set the triggers that cause the animation to play")
    public void triggers(@Src Player player, Trigger... triggers) {
        must(player).trigger(triggers);
        Fmt.info("Set the animation triggers").tell(player);
    }

    @Command("animation|anim mode <mode>")
    @Permission("stopmotion.command.animation.mode")
    @Description("Set the playback mode for the animation")
    public void mode(@Src Player player, AnimationMode mode) {
        must(player).mode = mode;
        Fmt.info("Set playback mode to ").stress(mode).tell(player);
    }

    @Command("animation|anim direction <directions...>")
    @Permission("stopmotion.command.animation.direction")
    @Description("Set the directions that the animation will play in")
    public void direction(@Src Player player, Direction... directions) {
        must(player).add(directions);
        Fmt.info("Set the playback directions to ").stress(Arrays.asList(directions)).tell(player);
    }

    @Command("animation|anim save <name>")
    @Permission("stopmotion.command.animation.save")
    @Description("Save an animation using the current configuration")
    public void save(@Src Player player, String name) {
        must(player).build(name).onPass(instance -> {
            plugin.getAnimations().register(instance);
            Fmt.info("Successfully saved animation ").stress(instance).tell(player);
        }).onFail(error -> {
            Fmt.error("Failed to create animation: %s", error).tell(player);
        });
    }

    @Command("animation|anim delete <name>")
    @Permission("stopmotion.command.animation.delete")
    @Description("Delete the given animation")
    public void delete(@Src Player player, Animation animation) {
        plugin.getTriggers().delete(animation.getId());
        Fmt.info("Deleted animation ").stress(animation.getId()).tell(player);
    }
}
