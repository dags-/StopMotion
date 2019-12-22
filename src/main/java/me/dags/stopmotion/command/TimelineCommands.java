package me.dags.stopmotion.command;

import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.duration.Duration;
import me.dags.pitaya.util.pos.PosRecorder;
import me.dags.stopmotion.StopMotion;
import me.dags.stopmotion.animation.Animation;
import me.dags.stopmotion.frame.TimelineBuilder;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class TimelineCommands extends Cache<TimelineBuilder> {

    private final StopMotion plugin;

    public TimelineCommands(StopMotion plugin) {
        super(30, TimeUnit.MINUTES, TimelineBuilder::new);
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
        PosRecorder.getSelection(player).ifPresent((pos1, pos2) -> {
            TimelineBuilder builder = must(player);
            builder.add(player.getLocation(), pos1, pos2, new Duration(ticks * 50, TimeUnit.MILLISECONDS));
            Fmt.info("Added frame: #").stress(builder.frames.size()).tell(player);
        }).ifAbsent(() -> {
            Fmt.error("You must first select the frame you want to add").tell(player);
        });
    }

    @Command("timeline|til replace <frame_number>")
    @Permission("stopmotion.command.timeline.replace")
    @Description("Replace an existing frame with your current selection")
    public void replace(@Src Player player, int frame) {
        replace(player, frame, 1);
    }

    @Command("timeline|til replace <frame_number> <ticks>")
    @Permission("stopmotion.command.timeline.replace")
    @Description("Replace an existing frame with your current selection")
    public void replace(@Src Player player, int frame, int ticks) {
        PosRecorder.getSelection(player).ifPresent((pos1, pos2) -> {
            TimelineBuilder builder = must(player);
            if (frame < 1 || frame > builder.frames.size()) {
                Fmt.error("Frame number out of range: ").stress("%s-%s", 1, builder.frames.size()).tell(player);
                return;
            }
            builder.set(frame - 1, player.getLocation(), pos1, pos2, new Duration(ticks * 50, TimeUnit.MILLISECONDS));
            Fmt.info("Set frame: #").stress(builder.frames.size()).tell(player);
        }).ifAbsent(() -> {
            Fmt.error("You must first select the frame you want to add").tell(player);
        });
    }

    @Command("timeline|til save")
    @Permission("stopmotion.command.timeline.save")
    @Description("Save your edits to an existing timeline")
    public void save(@Src Player player) {
        TimelineBuilder builder = must(player);
        if (builder.name.isEmpty()) {
            Fmt.error("You must specify a name for this timeline").tell(player);
            return;
        }
        save(player, builder.name);
    }

    @Command("timeline|til save <name>")
    @Permission("stopmotion.command.timeline.save")
    @Description("Save the timeline to file")
    public void save(@Src Player player, String name) {
        must(player).build(name.toLowerCase()).onPass(timeline -> {
            drain(player, "");
            plugin.getTimelines().register(timeline);
            Fmt.info("Saved timeline ").stress(timeline.getName()).tell(player);
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }

    @Command("timeline|til clear")
    @Permission("stopmotion.command.timeline.clear")
    @Description("Clear your current timeline builder")
    public void clear(@Src Player player) {
        drain(player, "You are not currently building a timeline").onPass(builder -> {
            Fmt.info("Cleared your timeline builder").tell(player);
        }).onFail(error -> {
            Fmt.error(error).tell(player);
        });
    }

    @Command("timeline|til edit <timeline>")
    @Permission("stopmotion.command.timeline.edit")
    @Description("Load an existing timeline to be edited")
    public void edit(@Src Player player, Animation animation) {
        animation.getTimeline().handle((t, e) -> {
            if (e != null) {
                Fmt.error(e.getMessage()).tell(player);
                e.printStackTrace();
            }
        }).run(timeline -> {
            TimelineBuilder builder = must(player);
            builder.name = animation.getName();
            builder.frames.addAll(timeline.getFrames());
            Fmt.info("You are now editing timeline ").stress(animation.getName()).tell(player);
        });
    }

    @Command("timeline|til delete <timeline>")
    @Permission("stopmotion.command.timeline.delete")
    @Description("Delete an existing timeline")
    public void delete(@Src Player player, Animation animation) {
        plugin.getTimelines().delete(animation.getId());
        Fmt.info("Deleted timeline ").stress(animation.getId()).tell(player);
    }
}
