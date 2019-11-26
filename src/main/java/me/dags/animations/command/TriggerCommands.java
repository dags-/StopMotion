package me.dags.animations.command;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.Animations;
import me.dags.animations.trigger.NamedTrigger;
import me.dags.animations.trigger.TriggerBuilder;
import me.dags.animations.trigger.type.Distance;
import me.dags.animations.trigger.type.Interact;
import me.dags.animations.trigger.type.Message;
import me.dags.animations.util.recorder.PosRecorder;
import me.dags.pitaya.command.annotation.*;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TriggerCommands extends BuilderCommand<TriggerBuilder> {

    private final Animations plugin;

    public TriggerCommands(Animations plugin) {
        super(5, TimeUnit.MINUTES, TriggerBuilder::new);
        this.plugin = plugin;
    }

    @Command("trigger wand")
    @Permission("animation.command.trigger.wand")
    @Description("Create a position selection tool to create triggers with")
    public void wand(@Src Player player) {
        PosRecorder.create(player, must(player).pos()).ifPresent(recorder -> Fmt.info("Created new wand").tell(player));
    }

    @Command("trigger message <message...>")
    @Permission("animation.command.trigger.message")
    @Description("Create a trigger that listens for the given message in chat")
    public void message(@Src Player player, @Join String message) {
        must(player).add(new Message(message));
        Fmt.info("Added new message trigger").tell(player);
    }

    @Command("trigger distance <radius>")
    @Permission("animation.command.trigger.distance")
    @Description("Create a trigger that checks proximity to given position")
    public void distance(@Src Player player, int radius) {
        Optional<PosRecorder> recorder = PosRecorder.lookup(player);
        if (recorder.isPresent()) {
            if (must(player).distance(radius)) {
                Fmt.info("Added distance trigger").tell(player);
                return;
            }
        }
        distance(player, player.getLocation().getBlockPosition(), radius);
    }

    @Command("trigger distance <position> <radius>")
    @Permission("animation.command.trigger.distance")
    @Description("Create a trigger that checks proximity to given position")
    public void distance(@Src Player player, Vector3i position, int radius) {
        must(player).add(new Distance(position, radius));
        Fmt.info("Added new distance trigger").tell(player);
    }

    @Command("trigger interact")
    @Permission("animation.command.trigger.interact")
    @Description("Create a trigger that checks interactions with blocks in a certain area")
    public void interact(@Src Player player) {
        Optional<PosRecorder> recorder = PosRecorder.lookup(player);
        if (recorder.isPresent()) {
            if (must(player).interact()) {
                Fmt.info("Added interaction trigger").tell(player);
                return;
            }
        }
        Fmt.error("You must specify two points for this type of trigger").tell(player);
    }

    @Command("trigger interact <pos1> <pos2>")
    @Permission("animation.command.trigger.interact")
    @Description("Create a trigger that checks interactions with blocks in a certain area")
    public void interact(@Src Player player, Vector3i pos1, Vector3i pos2) {
        must(player).add(new Interact(pos1, pos2));
        Fmt.info("Added interaction trigger").tell(player);
    }

    @Command("trigger save <name>")
    @Permission("animation.command.trigger.save")
    @Description("Save and register the trigger with the given name")
    public void save(@Src Player player, String name) {
        drain(player, builder -> {
            NamedTrigger named = builder.build(name);
            if (named.isPresent()) {
                plugin.getTriggers().register(named);
                Fmt.info("Registered trigger ").stress(named.getName()).tell(player);
            }
        });
    }
}
