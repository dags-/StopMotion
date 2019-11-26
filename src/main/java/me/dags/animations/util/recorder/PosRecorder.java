package me.dags.animations.util.recorder;

import com.flowpowered.math.vector.Vector3i;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.PluginUtils;
import me.dags.pitaya.util.cache.IdCache;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PosRecorder {

    private static final IdCache<PosRecorder> recorders = new IdCache<>(10, TimeUnit.MINUTES);

    private final ItemStack wand;
    private final PosRecord record;

    public PosRecorder(ItemStack wand, PosRecord record) {
        this.wand = wand;
        this.record = record;
    }

    public boolean matches(ItemStack stack) {
        return wand.equalTo(stack);
    }

    public PosRecorder record(MessageReceiver receiver, Type type, String world, Vector3i position) {
        if (type == Type.PRIMARY) {
            record.pos1 = position;
            record.world = world;
            Fmt.info("Set pos1 ").stress(position).tell(receiver);
        } else if (type == Type.SECONDARY) {
            record.pos2 = position;
            record.world = world;
            Fmt.info("Set pos2 ").stress(position).tell(receiver);
        }
        return this;
    }

    public enum Type {
        PRIMARY,
        SECONDARY,
    }

    public static Optional<PosRecorder> lookup(Player player) {
        return recorders.get(player);
    }

    public static Optional<PosRecorder> get(Player player) {
        return player.getItemInHand(HandTypes.MAIN_HAND)
                .flatMap(stack -> recorders.get(player).filter(recorder -> recorder.matches(stack)));
    }

    public static Optional<PosRecorder> create(Player player, PosRecord record) {
        return player.getItemInHand(HandTypes.MAIN_HAND).map(stack -> {
            PosRecorder recorder = new PosRecorder(stack, record);
            recorders.put(player, recorder);
            return recorder;
        });
    }

    static {
        Sponge.getEventManager().registerListeners(PluginUtils.getCurrentPluginInstance(), new PosListener());
    }
}
