package me.dags.animations.util.recorder;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;

public class PosListener {

    @Listener
    public void onPrimary(InteractBlockEvent.Primary event, @Root Player player) {
        PosRecorder.get(player).ifPresent(recorder -> {
            String world = player.getWorld().getName();
            Vector3i position = event.getTargetBlock().getPosition();
            recorder.record(player, PosRecorder.Type.PRIMARY, world, position);
        });
    }

    @Listener
    public void onSecondary(InteractBlockEvent.Primary event, @Root Player player) {
        PosRecorder.get(player).ifPresent(recorder -> {
            String world = player.getWorld().getName();
            Vector3i position = event.getTargetBlock().getPosition();
            recorder.record(player, PosRecorder.Type.SECONDARY, world, position);
        });
    }
}
