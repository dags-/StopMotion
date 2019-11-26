package me.dags.animations.command;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.Animations;
import me.dags.animations.animation.AnimationRef;
import me.dags.animations.instance.InstanceBuilder;
import me.dags.animations.trigger.TriggerType;
import me.dags.animations.trigger.type.Interact;
import me.dags.animations.trigger.type.Message;
import me.dags.animations.trigger.type.Radius;
import me.dags.animations.util.iterator.Direction;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Join;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.cache.IdCache;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class InstanceCommands {

    private final Animations plugin;
    private final IdCache<InstanceBuilder> builders = new IdCache<>(30, TimeUnit.MINUTES);

    public InstanceCommands(Animations plugin) {
        this.plugin = plugin;
    }

    private InstanceBuilder must(Player player) {
        return builders.compute(player, u -> new InstanceBuilder());
    }

    @Command("animation|anim origin")
    public void origin(@Src Player player) {
        must(player).origin(player.getLocation());
        Fmt.info("Set origin to ").stress(player.getLocation()).tell(player);
    }

    @Command("animation|anim animation <animation>")
    public void animation(@Src Player player, AnimationRef animation) {
        must(player).animation = animation;
        Fmt.info("Set animation to ").stress(animation.getName()).tell(player);
    }

    @Command("animation|anim trigger message <message...>")
    public void message(@Src Player player, @Join String message) {
        must(player).triggers().add(new Message(message));
        Fmt.info("Added message trigger").tell(player);
    }

    @Command("animation|anim trigger radius <center> <radius>")
    public void radius(@Src Player player, int radius) {
        radius(player, player.getLocation().getBlockPosition(), radius);
    }

    @Command("animation|anim trigger radius <center> <radius>")
    public void radius(@Src Player player, Vector3i center, int radius) {
        must(player).triggers().add(new Radius(center, radius));
        Fmt.info("Added radius trigger").tell(player);
    }

    @Command("animation|anim trigger interact <pos1> <pos2>")
    public void interact(@Src Player player, Vector3i pos1, Vector3i pos2) {
        must(player).triggers().add(new Interact(pos1, pos2));
        Fmt.info("Added interaction trigger").tell(player);
    }

    @Command("animation|anim trigger create and")
    public void and(@Src Player player) {
        must(player).add(TriggerType.AND);
        Fmt.info("Created 'and' trigger").tell(player);
    }

    @Command("animation|anim trigger create or")
    public void or(@Src Player player) {
        must(player).add(TriggerType.OR);
        Fmt.info("Created 'or' trigger").tell(player);
    }

    @Command("animation|anim timeline <directions...>")
    public void timeline(@Src Player player, Direction... directions) {
        must(player).add(directions);
        Fmt.info("Set timeline").tell(player);
    }

    @Command("animation|anim save <name>")
    public void save(@Src Player player, String name) {
        must(player).build(name).ifPresent(instance -> {
            plugin.getInstances().register(instance);
            Fmt.info("Successfully created animation ").stress(instance).tell(player);
        });
    }
}
