package me.dags.animations.command;

import me.dags.animations.Animations;
import me.dags.animations.animation.Animation;
import me.dags.animations.animation.Timeline;
import me.dags.animations.frame.FrameBuilder;
import me.dags.animations.util.duration.Duration;
import me.dags.animations.util.recorder.PosRecorder;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class FrameCommands extends BuilderCommand<FrameBuilder> {

    private final Animations plugin;

    public FrameCommands(Animations plugin) {
        super(30, TimeUnit.MINUTES, FrameBuilder::new);
        this.plugin = plugin;
    }

    @Command("frame wand")
    @Permission("animation.command.frame.wand")
    @Description("Set your held item as a position selection wand")
    public void wand(@Src Player player) {
        PosRecorder.create(player, must(player).pos).ifPresent(r -> Fmt.info("Created new frame wand").tell(player));
    }

    @Command("frame pos1")
    @Permission("animation.command.frame.pos")
    @Description("Set your current position as one of the corners of your frame selection")
    public void pos1(@Src Player player) {
        FrameBuilder builder = must(player);
        builder.pos.pos1 = player.getLocation().getBlockPosition();
        builder.pos.world = player.getLocation().getExtent().getName();
        Fmt.info("Set pos1 ").stress(builder.pos.pos1).tell(player);
    }

    @Command("frame pos2")
    @Permission("animation.command.frame.pos")
    @Description("Set your current position as one of the corners of your frame selection")
    public void pos2(@Src Player player) {
        FrameBuilder builder = must(player);
        builder.pos.pos2 = player.getLocation().getBlockPosition();
        builder.pos.world = player.getLocation().getExtent().getName();
        Fmt.info("Set pos2 ").stress(builder.pos.pos2).tell(player);
    }

    @Command("frame add <ticks>")
    @Permission("animation.command.frame.add")
    @Description("Add the current frame selection to the animation")
    public void add(@Src Player player) {
        add(player, 1);
    }

    @Command("frame add <ticks>")
    @Permission("animation.command.frame.add")
    @Description("Add the current frame selection to the animation with a duration in ticks")
    public void add(@Src Player player, int ticks) {
        add(player, ticks * 50, TimeUnit.MILLISECONDS);
    }

    @Command("frame add <duration> <unit>")
    @Permission("animation.command.frame.add")
    @Description("Add the current frame selection to the animation with the given duration")
    public void add(@Src Player player, int duration, TimeUnit unit) {
        FrameBuilder builder = must(player);
        builder.add(new Duration(duration, unit));
        Fmt.info("Added frame: #").stress(builder.frames.size()).tell(player);
    }

    @Command("frame undo <count>")
    @Permission("animation.command.frame.undo")
    @Description("Undo and discard a number of frames")
    public void undo(@Src Player player, int count) {
        must(player).undo(player.getWorld(), count);
        Fmt.info("Undid ").stress(count).tell(player);
    }

    @Command("timeline create <name>")
    @Permission("animation.command.timeline.create")
    @Description("Save your current list of frames to an animation timeline")
    public void save(@Src Player player, String name) {
        drain(player, builder -> {
            Timeline timeline = builder.build(name);
            plugin.getAnimations().register(timeline);
            Fmt.info("Saved animation ").stress(name).tell(player);
        });
    }

    @Command("timeline delete <name>")
    @Permission("animation.command.frame.delete")
    @Description("Delete the given animation frames")
    public void delete(@Src Player player, Animation animation) {
        plugin.getAnimations().delete(animation.getId());
        Fmt.info("Deleted animation ").stress(animation.getId()).tell(player);
    }
}
