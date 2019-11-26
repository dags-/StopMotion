package me.dags.animations.command;

import me.dags.animations.Animations;
import me.dags.animations.animation.Animation;
import me.dags.animations.frame.FrameRecorder;
import me.dags.animations.util.duration.Duration;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.cache.IdCache;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class FrameCommands {

    private final Animations plugin;
    private final IdCache<FrameRecorder> cache = new IdCache<>(60, TimeUnit.MINUTES);

    public FrameCommands(Animations plugin) {
        this.plugin = plugin;
    }

    @Command("frame pos1")
    public void pos1(@Src Player player) {
        FrameRecorder recorder = cache.compute(player, FrameRecorder::new);
        recorder.pos1 = player.getLocation().getBlockPosition();
        recorder.world = player.getLocation().getExtent().getName();
        Fmt.info("Set pos1 ").stress(recorder.pos1).tell(player);
    }

    @Command("frame pos2")
    public void pos2(@Src Player player) {
        FrameRecorder recorder = cache.compute(player, FrameRecorder::new);
        recorder.pos2 = player.getLocation().getBlockPosition();
        recorder.world = player.getLocation().getExtent().getName();
        Fmt.info("Set pos2 ").stress(recorder.pos2).tell(player);
    }

    @Command("frame add <ticks>")
    public void add(@Src Player player, int ticks) {
        add(player, ticks * 50, TimeUnit.MILLISECONDS);
    }

    @Command("frame add <duration> <unit>")
    public void add(@Src Player player, int duration, TimeUnit unit) {
        FrameRecorder recorder = cache.compute(player, FrameRecorder::new);
        recorder.add(new Duration(duration, unit));
        Fmt.info("Added frame: #").stress(recorder.frames.size()).tell(player);
    }

    @Command("frame save <name>")
    public void save(@Src Player player, String name) {
        cache.get(player).ifPresent(recorder -> {
            Animation animation = recorder.create(name);
            plugin.getAnimations().register(animation);
            Fmt.info("Saved animation ").stress(name).tell(player);
        });
    }

    @Command("frame test")
    public void test(@Src Player player) {
        cache.compute(player, FrameRecorder::new).test();
        Fmt.info("Started test animation").tell(player);
    }

    @Command("frame reset")
    public void reset(@Src Player player) {
        FrameRecorder recorder = cache.compute(player, FrameRecorder::new);
        recorder.reset();
        Fmt.info("Started test animation").tell(player);
    }
}
