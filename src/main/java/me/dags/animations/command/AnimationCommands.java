package me.dags.animations.command;

import me.dags.animations.Animations;
import me.dags.animations.animation.AnimationRef;
import me.dags.animations.util.iterator.Direction;
import me.dags.animations.util.worker.Worker;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;

public class AnimationCommands {

    private final Animations plugin;

    public AnimationCommands(Animations plugin) {
        this.plugin = plugin;
    }

    @Command("anim test <animation> <directions...>")
    public void test(@Src Player player, AnimationRef animation, Direction... directions) {
        animation.getAnimation().ifPresent(anim -> {
            Location<World> origin = player.getLocation();
            Worker worker = anim.getTimeline(origin, Arrays.asList(directions));
            plugin.getPlaybackManager().play("test", worker);
            Fmt.info("Started animation ").stress(anim.getName()).tell(player);
        });
    }
}
