package me.dags.animations.command;

import me.dags.animations.Animations;
import me.dags.animations.animation.Animation;
import me.dags.animations.instance.InstanceBuilder;
import me.dags.animations.trigger.NamedTrigger;
import me.dags.animations.util.iterator.Direction;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class InstanceCommands extends BuilderCommand<InstanceBuilder> {

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
        Fmt.info("Set origin ").stress(player.getLocation()).tell(player);
    }

    @Command("animation|anim animation <animation>")
    @Permission("animation.command.animation.animation")
    @Description("Set the animation to play at this location")
    public void animation(@Src Player player, Animation animation) {
        must(player).animation = animation;
        Fmt.info("Set animation ").stress(animation.getName()).tell(player);
    }

    @Command("animation|anim trigger <trigger...>")
    @Permission("animation.command.animation.trigger")
    @Description("Set the triggers that cause the animation to play")
    public void triggers(@Src Player player, NamedTrigger... triggers) {
        must(player).trigger(triggers);
        Fmt.info("Set animation triggers").tell(player);
    }

    @Command("animation|anim timeline <directions...>")
    @Permission("animation.command.animation.timeline")
    @Description("Set the directions that the animation will play in")
    public void timeline(@Src Player player, Direction... directions) {
        must(player).add(directions);
        Fmt.info("Set timeline").tell(player);
    }

    @Command("animation|anim save <name>")
    @Permission("animation.command.animation.save")
    @Description("Save the animation instance to file")
    public void save(@Src Player player, String name) {
        must(player).build(name).ifPresent(instance -> {
            plugin.getInstances().register(instance);
            Fmt.info("Successfully created animation ").stress(instance).tell(player);
        });
    }
}
