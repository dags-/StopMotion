package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.Animations;
import me.dags.animations.instance.Instance;
import me.dags.animations.util.region.RegionMap;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.world.Locatable;

import java.util.List;
import java.util.UUID;

public class TriggerListener {

    private final UUID world;
    private final Animations plugin;
    private final RegionMap<Instance> regions = new RegionMap<>(7);

    public TriggerListener(Animations plugin, UUID world, List<Instance> instances) {
        Animations.log("Created trigger listener for instances: {}", instances.size());
        this.world = world;
        this.plugin = plugin;
        for (Instance instance : instances) {
            Animations.log("Adding instance triggers for: {}", instance.getName());
            regions.add(instance, 1);
        }
    }

    @Listener(order = Order.PRE)
    public void onChat(MessageChannelEvent.Chat event, @Root Player player) {
        if (isWorld(player)) {
            Context context = new Context(event, player);
            context.message = event.getRawMessage().toPlain();
            test(context, true);
        }
    }

    @Listener(order = Order.PRE)
    public void onClick(InteractBlockEvent event, @Root Player player) {
        if (isWorld(event.getTargetBlock().getWorldUniqueId())) {
            Context context = new Context(event, player);
            context.clicked = event.getTargetBlock().getPosition();
            test(context, true);
        }
    }

    @Listener(order = Order.POST)
    public void onMove(MoveEntityEvent.Position event, @Root Player player) {
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
            for (Rule trigger : instance.getTriggers()) {
                if (trigger.test(context)) {
                    context.event.setCancelled(cancel);
                    plugin.getPlaybackManager().play(instance);
                    return;
                }
            }
        });
    }
}
