package me.dags.animations.command;

import me.dags.animations.util.recorder.PosRecorder;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.living.player.Player;

public class WandCommands {

    @Command("animation|anim wand")
    @Permission("animation.command.wand.create")
    @Description("Bind a new selection wand to your currently held item")
    public void create(@Src Player player) {
        PosRecorder.create(player);
    }

    @Command("animation|anim wand")
    @Permission("animation.command.wand.clear")
    @Description("Clear your wand selection")
    public void clear(@Src Player player) {
        PosRecorder.create(player);
    }

    @Command("animation|anim pos1")
    @Permission("animation.command.wand.pos")
    @Description("Set your current position as pos1 of your selection")
    public void pos1(@Src Player player) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            recorder.record(player, PosRecorder.Type.PRIMARY, player.getLocation().getBlockPosition());
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }

    @Command("animation|anim pos2")
    @Permission("animation.command.wand.pos")
    @Description("Set your current position as pos2 of your selection")
    public void pos2(@Src Player player) {
        PosRecorder.getRecorder(player).onPass(recorder -> {
            recorder.record(player, PosRecorder.Type.SECONDARY, player.getLocation().getBlockPosition());
        }).onFail(message -> {
            Fmt.error(message).tell(player);
        });
    }
}
