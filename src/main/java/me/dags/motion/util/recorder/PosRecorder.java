package me.dags.motion.util.recorder;

import com.flowpowered.math.vector.Vector3i;
import me.dags.pitaya.cache.IdCache;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.PluginUtils;
import me.dags.pitaya.util.optional.BiOptional;
import me.dags.pitaya.util.optional.Result;
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

    private Vector3i pos1 = null;
    private Vector3i pos2 = null;

    public PosRecorder(ItemStack wand) {
        this.wand = wand;
    }

    public boolean matches(ItemStack stack) {
        return wand.equalTo(stack);
    }

    public BiOptional<Vector3i, Vector3i> getSelection() {
        return BiOptional.ofNullable(pos1, pos2);
    }

    public Optional<Vector3i> getPos1() {
        return Optional.ofNullable(pos1);
    }

    public Optional<Vector3i> getPos2() {
        return Optional.ofNullable(pos2);
    }

    public PosRecorder record(MessageReceiver receiver, Type type, Vector3i position) {
        if (type == Type.PRIMARY) {
            pos1 = position;
            Fmt.info("Set pos1 ").stress(position).tell(receiver);
        } else if (type == Type.SECONDARY) {
            pos2 = position;
            Fmt.info("Set pos2 ").stress(position).tell(receiver);
        }
        return this;
    }

    public enum Type {
        PRIMARY,
        SECONDARY,
    }

    public static BiOptional<Vector3i, Vector3i> getSelection(Player player) {
        return recorders.get(player).map(PosRecorder::getSelection).orElse(BiOptional.empty());
    }

    public static Result<PosRecorder, String> getRecorder(Player player) {
        Optional<ItemStack> stack = player.getItemInHand(HandTypes.MAIN_HAND);
        if (!stack.isPresent()) {
            return Result.fail("You must be holding an item!");
        }

        Optional<PosRecorder> recorder = recorders.get(player);
        if (!recorder.isPresent()) {
            return Result.fail("You have not created a selection wand yet!");
        }

        if (!recorder.get().matches(stack.get())) {
            return Result.fail("Your selection wand is not bound to that item!");
        }

        return Result.pass(recorder.get());
    }

    public static Result<PosRecorder, String> create(Player player) {
        Optional<ItemStack> stack = player.getItemInHand(HandTypes.MAIN_HAND);
        if (stack.isPresent()) {
            PosRecorder recorder = new PosRecorder(stack.get());
            recorders.put(player, recorder);
            return Result.pass(recorder);
        }
        return Result.fail("You must be holding an item!");
    }

    static {
        Sponge.getEventManager().registerListeners(PluginUtils.getCurrentPluginInstance(), new PosListener());
    }
}
