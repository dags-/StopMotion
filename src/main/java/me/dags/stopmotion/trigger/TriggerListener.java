package me.dags.stopmotion.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.pitaya.util.region.RegionMap;
import me.dags.stopmotion.StopMotion;
import me.dags.stopmotion.instance.Instance;
import me.dags.stopmotion.trigger.rule.RuleType;
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
    private final StopMotion plugin;
    private final RegionMap<Instance> regions = new RegionMap<>(6);

    public TriggerListener(StopMotion plugin, UUID world, List<Instance> instances) {
        StopMotion.debug("Created trigger listener for instances: {}", instances.size());
        this.world = world;
        this.plugin = plugin;
        for (Instance instance : instances) {
            StopMotion.debug("Adding instance triggers for: {}", instance.getName());
            regions.add(instance, Trigger.MAX_RADIUS);
        }
    }

    @Listener(order = Order.PRE)
    public void onChat(MessageChannelEvent.Chat event, @Root Player player) {
        if (isWorld(player)) {
            Context context = new Context(RuleType.MESSAGE, event, player);
            context.message = event.getRawMessage().toPlain();
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
            Context context = new Context(RuleType.DISTANCE, event, player);
            test(context, false);
        }
    }

    @Listener(order = Order.PRE)
    public void onPrimary(InteractBlockEvent.Primary.MainHand event, @Root Player player) {
        onInteract(event, player);
    }

    @Listener(order = Order.PRE)
    public void onSecondary(InteractBlockEvent.Secondary.MainHand event, @Root Player player) {
        onInteract(event, player);
    }

    private void onInteract(InteractBlockEvent event, Player player) {
        if (isWorld(event.getTargetBlock().getWorldUniqueId())) {
            Context context = new Context(RuleType.INTERACT, event, player);
            context.clicked = event.getTargetBlock().getPosition();
            test(context, true);
        }
    }

    private boolean isWorld(UUID uuid) {
        return uuid == world;
    }

    private boolean isWorld(Locatable locatable) {
        return isWorld(locatable.getWorld().getUniqueId());
    }

    private void test(Context context, boolean cancel) {
        regions.visit(context.position, Trigger.MAX_RADIUS, instance -> {
            for (Trigger trigger : instance.getTriggers()) {
                if (trigger.test(context)) {
                    context.event.setCancelled(cancel);
                    plugin.getPlaybackManager().submit(instance);
                    return;
                }
            }
        });
    }
}
