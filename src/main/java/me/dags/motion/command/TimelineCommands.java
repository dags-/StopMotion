package me.dags.motion.command;

import me.dags.motion.StopMotion;
import me.dags.motion.animation.Animation;
import me.dags.motion.frame.FrameBuilder;
import me.dags.motion.frame.Timeline;
import me.dags.motion.util.recorder.PosRecorder;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.duration.Duration;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class TimelineCommands extends Cache<FrameBuilder> {

    private final StopMotion plugin;

    public TimelineCommands(StopMotion plugin) {
        super(30, TimeUnit.MINUTES, FrameBuilder::new);
        this.plugin = plugin;
    }

    @Command("timeline|til add <ticks>")
    @Permission("stopmotion.command.timeline.add")
    @Description("Add the selected frame to the timeline")
    public void add(@Src Player player) {
        add(player, 1);
    }

    @Command("timeline|til add <ticks>")
    @Permission("stopmotion.command.timeline.add")
    @Description("Add the selected frame to the timeline with the given duration")
    public void add(@Src Player player, int ticks) {
        add(player, ticks * 50, TimeUnit.MILLISECONDS);
    }

    @Command("timeline|til add <duration> <unit>")
    @Permission("stopmotion.command.timeline.add")
    @Description("Add the selected frame to the timeline with the given duration")
    public void add(@Src Player player, int duration, TimeUnit unit) {
        PosRecorder.getSelection(player).ifPresent((pos1, pos2) -> {
            FrameBuilder builder = must(player);
            builder.add(player.getLocation(), pos1, pos2, new Duration(duration, unit));
            Fmt.info("Added frame: #").stress(builder.frames.size()).tell(player);
        }).ifAbsent(() -> {
            Fmt.error("You must first select the frame you want to add").tell(player);
        });
    }

    @Command("timeline|til save <name>")
    @Permission("stopmotion.command.timeline.save")
    @Description("Save the timeline to file")
    public void save(@Src Player player, String name) {
        drain(player, "You have not created any frames yet").onPass(builder -> {
            Timeline timeline = builder.build(name);
            plugin.getTimelines().register(timeline);
            Fmt.info("Saved timeline ").stress(name).tell(player);
        }).onFail(message -> {
            Fmt.error("Failed to create timeline: %s", message).tell(player);
        });
    }

    @Command("timeline|til delete <name>")
    @Permission("stopmotion.command.timeline.delete")
    @Description("Delete an existing timeline")
    public void delete(@Src Player player, Animation animation) {
        plugin.getTimelines().delete(animation.getId());
        Fmt.info("Deleted timeline ").stress(animation.getId()).tell(player);
    }
}
