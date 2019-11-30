package me.dags.animations.command;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.Animations;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerBuilder;
import me.dags.animations.trigger.rule.*;
import me.dags.animations.util.recorder.PosRecorder;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.*;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TriggerCommands extends Cache<TriggerBuilder> {

    private final Animations plugin;

    public TriggerCommands(Animations plugin) {
        super(5, TimeUnit.MINUTES, TriggerBuilder::new);
        this.plugin = plugin;
    }

    @Command("trigger wand")
    @Permission("animation.command.rule.wand")
    @Description("Create a position selection tool to create triggers with")
    public void wand(@Src Player player) {
        PosRecorder.create(player, must(player).pos()).ifPresent(recorder -> Fmt.info("Created new wand").tell(player));
    }

    @Command("trigger add message <message...>")
    @Permission("animation.command.rule.message")
    @Description("Add a rule that listens for the given message in chat")
    public void message(@Src Player player, @Join String message) {
        must(player).add(new Message(message));
        Fmt.info("Added new message rule").tell(player);
    }

    @Command("trigger add permission <name>")
    @Permission("animation.command.rule.permission")
    @Description("Add a rule that checks for a permission node")
    public void permission(@Src Player player, String name) {
        must(player).add(new Perm(name));
        Fmt.info("Added new permission rule").tell(player);
    }

    @Command("trigger add distance <radius>")
    @Permission("animation.command.rule.distance")
    @Description("Add a rule that checks proximity to given position")
    public void distance(@Src Player player, int radius) {
        Optional<PosRecorder> recorder = PosRecorder.lookup(player);
        if (recorder.isPresent()) {
            if (must(player).distance(radius)) {
                Fmt.info("Added distance rule").tell(player);
                return;
            }
        }
        distance(player, player.getLocation().getBlockPosition(), radius);
    }

    @Command("trigger add distance <position> <radius>")
    @Permission("animation.command.rule.distance")
    @Description("Add a rule that checks proximity to given position")
    public void distance(@Src Player player, Vector3i position, int radius) {
        must(player).add(new Distance(position, radius));
        Fmt.info("Added new distance rule").tell(player);
    }

    @Command("trigger add time <min> <max>")
    @Permission("animation.command.rule.time")
    @Description("Add a rule that checks for a min and max time")
    public void time(@Src Player player, long min, long max) {
        must(player).add(new Time(min, max));
        Fmt.info("Added new time rule").tell(player);
    }

    @Command("trigger add interact")
    @Permission("animation.command.rule.interact")
    @Description("Add a rule that checks interactions with blocks in a certain area")
    public void interact(@Src Player player) {
        Optional<PosRecorder> recorder = PosRecorder.lookup(player);
        if (recorder.isPresent()) {
            if (must(player).interact()) {
                Fmt.info("Added interaction rule").tell(player);
                return;
            }
        }
        Fmt.error("You must specify two points for this type of trigger").tell(player);
    }

    @Command("trigger add interact <pos1> <pos2>")
    @Permission("animation.command.rule.interact")
    @Description("Add a rule that checks interactions with blocks in a certain area")
    public void interact(@Src Player player, Vector3i pos1, Vector3i pos2) {
        must(player).add(new Interact(pos1, pos2));
        Fmt.info("Added interaction rule").tell(player);
    }

    @Command("trigger save <name>")
    @Permission("animation.command.trigger.save")
    @Description("Save the current set of rules as a trigger")
    public void save(@Src Player player, String name) {
        drain(player, "You must create some rules first").onPass(builder -> {
            Trigger named = builder.build(name);
            if (named.isPresent()) {
                plugin.getTriggers().register(named);
                Fmt.info("Registered trigger ").stress(named.getName()).tell(player);
            }
        }).onFail(message -> {
            Fmt.error("Failed to create trigger: %s", message).tell(player);
        });
    }

    @Command("trigger delete <name>")
    @Permission("animation.command.trigger.delete")
    @Description("Delete the given trigger")
    public void delete(@Src Player player, Trigger trigger) {
        plugin.getTriggers().delete(trigger.getId());
        Fmt.info("Deleted trigger ").stress(trigger.getId()).tell(player);
    }
}
