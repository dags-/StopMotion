package me.dags.stopmotion.command;

import com.flowpowered.math.vector.Vector3i;
import me.dags.stopmotion.StopMotion;
import me.dags.stopmotion.trigger.Trigger;
import me.dags.stopmotion.trigger.TriggerBuilder;
import me.dags.stopmotion.trigger.rule.*;
import me.dags.stopmotion.util.recorder.PosRecorder;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.*;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class TriggerCommands extends Cache<TriggerBuilder> {

    private final StopMotion plugin;

    public TriggerCommands(StopMotion plugin) {
        super(5, TimeUnit.MINUTES, TriggerBuilder::new);
        this.plugin = plugin;
    }

    @Command("trigger add message <message...>")
    @Permission("stopmotion.command.rule.message")
    @Description("Add a rule that listens for the given message in chat")
    public void message(@Src Player player, @Join String message) {
        Rule rule = must(player).add(new Message(message));
        Fmt.info("Added new message rule ").stress(rule).tell(player);
    }

    @Command("trigger add permission <name>")
    @Permission("stopmotion.command.rule.permission")
    @Description("Add a rule that checks for a permission node")
    public void permission(@Src Player player, String name) {
        Rule rule = must(player).add(new Perm(name));
        Fmt.info("Added new permission rule ").stress(rule).tell(player);
    }

    @Command("trigger add distance <radius>")
    @Permission("stopmotion.command.rule.distance")
    @Description("Add a rule that checks proximity to given position")
    public void distance(@Src Player player, int radius) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            Vector3i pos = recorder.getPos1().orElse(player.getLocation().getBlockPosition());
            distance(player, pos, radius);
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }

    @Command("trigger add distance <position> <radius>")
    @Permission("stopmotion.command.rule.distance")
    @Description("Add a rule that checks proximity to given position")
    public void distance(@Src Player player, Vector3i position, int radius) {
        Rule rule = must(player).add(new Distance(position, radius));
        Fmt.info("Added new distance rule ").stress(rule).tell(player);
    }

    @Command("trigger add time <min> <max>")
    @Permission("stopmotion.command.rule.time")
    @Description("Add a rule that checks for a min and max time")
    public void time(@Src Player player, long min, long max) {
        Rule rule = must(player).add(new Time(min, max));
        Fmt.info("Added new time rule ").stress(rule).tell(player);
    }

    @Command("trigger add interact")
    @Permission("stopmotion.command.rule.interact")
    @Description("Add a rule that checks interactions with blocks in a certain area")
    public void interact(@Src Player player) {
        PosRecorder.getSelection(player).ifPresent((pos1, pos2) -> {
            Rule rule = must(player).add(new Interact(pos1, pos2));
            Fmt.info("Added interaction rule ").stress(rule).tell(player);
        }).ifAbsent(() -> {
            Fmt.error("You must specify two points for this type of trigger").tell(player);
        });
    }

    @Command("trigger add interact <pos1> <pos2>")
    @Permission("stopmotion.command.rule.interact")
    @Description("Add a rule that checks interactions with blocks in a certain area")
    public void interact(@Src Player player, Vector3i pos1, Vector3i pos2) {
        Rule rule = must(player).add(new Interact(pos1, pos2));
        Fmt.info("Added interaction rule ").stress(rule).tell(player);
    }

    @Command("trigger save <name>")
    @Permission("stopmotion.command.trigger.save")
    @Description("Save the current set of rules as a trigger")
    public void save(@Src Player player, String name) {
        drain(player, "You must create some rules first").flatMap(builder -> builder.build(name)).onPass(trigger -> {
            plugin.getTriggers().register(trigger);
            Fmt.info("Registered trigger ").stress(trigger.getName()).tell(player);
        }).onFail(message -> {
            Fmt.error("Failed to create trigger: %s", message).tell(player);
        });
    }

    @Command("trigger clear")
    @Permission("stopmotion.command.trigger.clear")
    @Description("Clear your current trigger builder")
    public void clear(@Src Player player) {
        drain(player, "You are not currently building a trigger").onPass(builder -> {
            Fmt.info("Cleared your trigger builder").tell(player);
        }).onFail(error -> {
            Fmt.error(error).tell(player);
        });
    }

    @Command("trigger delete <name>")
    @Permission("stopmotion.command.trigger.delete")
    @Description("Delete an existing trigger")
    public void delete(@Src Player player, Trigger trigger) {
        plugin.getTriggers().delete(trigger.getId());
        Fmt.info("Deleted trigger ").stress(trigger.getId()).tell(player);
    }
}
