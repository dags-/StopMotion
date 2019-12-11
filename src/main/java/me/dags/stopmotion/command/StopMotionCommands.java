package me.dags.stopmotion.command;

import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import me.dags.pitaya.util.pos.PosRecorder;
import me.dags.stopmotion.StopMotion;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

public class StopMotionCommands {

    private final StopMotion plugin;

    public StopMotionCommands(StopMotion plugin) {
        this.plugin = plugin;
    }

    @Command("stopmotion|sm wand")
    @Permission("stopmotion.command.wand.create")
    @Description("Bind a new selection wand to your currently held item")
    public void create(@Src Player player) {
        PosRecorder.create(player).onPass(r -> {
            Fmt.info("Created new selection wand").tell(player);
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }

    @Command("stopmotion|sm pos1")
    @Permission("stopmotion.command.wand.pos")
    @Description("Set your current position as pos1 of your selection")
    public void pos1(@Src Player player) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            recorder.record(player, PosRecorder.Type.PRIMARY, player.getLocation().getBlockPosition());
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }

    @Command("stopmotion|sm pos2")
    @Permission("stopmotion.command.wand.pos")
    @Description("Set your current position as pos2 of your selection")
    public void pos2(@Src Player player) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            recorder.record(player, PosRecorder.Type.SECONDARY, player.getLocation().getBlockPosition());
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }

    @Command("stopmotion|sm reload")
    @Permission("stopmotion.command.reload")
    @Description("Reload the animations plugin")
    public void reload(@Src CommandSource source) {
        plugin.reload(null);
        Fmt.info("StopMotion reloaded").tell(source);
    }
}
