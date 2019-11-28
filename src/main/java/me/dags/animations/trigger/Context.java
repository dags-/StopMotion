package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.rule.RuleType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.world.World;


public class Context {

    public final Player player;
    public final World world;
    public final Vector3i position;
    public final Cancellable event;
    public final RuleType type;

    public String message = "";
    public Vector3i clicked = Vector3i.ZERO;

    public Context(Cancellable event, Player player, RuleType rule) {
        this.event = event;
        this.type = rule;
        this.player = player;
        this.world = player.getWorld();
        this.position = player.getLocation().getBlockPosition();
    }
}
