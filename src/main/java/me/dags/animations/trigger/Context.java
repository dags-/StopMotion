package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.rule.RuleType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.world.World;

public class Context {

    public final World world;
    public final Player player;
    public final RuleType type;
    public final Vector3i position;
    public final Cancellable event;

    public String message = "";
    public Vector3i clicked = Vector3i.ZERO;

    public Context(RuleType rule, Cancellable event, Player player) {
        this.type = rule;
        this.event = event;
        this.player = player;
        this.world = player.getWorld();
        this.position = player.getLocation().getBlockPosition();
    }
}
