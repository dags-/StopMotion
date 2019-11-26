package me.dags.animations;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.instance.Instance;
import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.util.region.RegionMap;
import me.dags.pitaya.command.annotation.Src;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.world.Locatable;

import java.util.List;
import java.util.UUID;

public class WorldListener {

    private final UUID world;
    private final Animations plugin;
    private final RegionMap<Instance> regions = new RegionMap<>(5);

    public WorldListener(Animations plugin, UUID world, List<Instance> instances) {
        this.world = world;
        this.plugin = plugin;
        for (Instance instance : instances) {
            regions.add(instance, 2);
        }
    }

    @Listener(order = Order.PRE)
    public void onChat(MessageChannelEvent.Chat event, @Src Player player) {
        if (isWorld(player)) {
            Context context = new Context(event, player);
            context.message = event.getOriginalMessage().toPlain();
            test(context, true);
        }
    }

    @Listener(order = Order.PRE)
    public void onClick(InteractBlockEvent event, @Src Player player) {
        if (isWorld(event.getTargetBlock().getWorldUniqueId())) {
            Context context = new Context(event, player);
            context.clicked = event.getTargetBlock().getPosition();
            test(context, true);
        }
    }

    @Listener(order = Order.POST)
    public void onMove(MoveEntityEvent.Position event, @Src Player player) {
        if (isWorld(player)) {
            Vector3i from = event.getFromTransform().getLocation().getBiomePosition();
            Vector3i to = event.getToTransform().getLocation().getBiomePosition();
            if (from.equals(to)) {
                return;
            }
            Context context = new Context(event, player);
            test(context, false);
        }
    }

    private boolean isWorld(UUID uuid) {
        return uuid == world;
    }

    private boolean isWorld(Locatable locatable) {
        return isWorld(locatable.getWorld().getUniqueId());
    }

    private void test(Context context, boolean cancel) {
        regions.visit(context.position, 1, instance -> {
            for (Trigger trigger : instance.getTriggers()) {
                if (trigger.test(context)) {
                    context.event.setCancelled(cancel);
                    plugin.getPlaybackManager().play(instance);
                    return;
                }
            }
        });
    }
}
