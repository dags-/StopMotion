package me.dags.animations.util.recorder;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;

public class PosListener {

    @Listener(order = Order.PRE)
    public void onPrimary(InteractBlockEvent.Primary.MainHand event, @Root Player player) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            Vector3i position = event.getTargetBlock().getPosition();
            recorder.record(player, PosRecorder.Type.PRIMARY, position);
            event.setCancelled(true);
        });
    }

    @Listener(order = Order.PRE)
    public void onSecondary(InteractBlockEvent.Secondary.MainHand event, @Root Player player) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            String world = player.getWorld().getName();
            Vector3i position = event.getTargetBlock().getPosition();
            recorder.record(player, PosRecorder.Type.SECONDARY, position);
            event.setCancelled(true);
        });
    }
}
