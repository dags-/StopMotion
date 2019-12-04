package me.dags.animations.command;

import me.dags.animations.entity.EntityInstanceBuilder;
import me.dags.animations.trigger.rule.Time;
import me.dags.animations.util.recorder.PosRecorder;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.extent.ArchetypeVolume;

import java.util.concurrent.TimeUnit;

public class EntityCommands extends Cache<EntityInstanceBuilder> {

    public EntityCommands() {
        super(5, TimeUnit.MINUTES, EntityInstanceBuilder::new);
    }

    @Command("ent time <min> <max>")
    public void time(@Src Player player, long min, long max) {
        must(player).rule = new Time(min, max);
        Fmt.info("Set time rule").tell(player);
    }

    @Command("ent select")
    public void select(@Src Player player) {
        PosRecorder.getSelection(player).ifPresent((pos1, pos2) -> {
            ArchetypeVolume volume = player.getWorld().createArchetypeVolume(pos1, pos2, player.getLocation().getBlockPosition());

            for (EntityArchetype entity : volume.getEntityArchetypes()) {
                must(player).entities = entity;
                Fmt.info("Added entity ").stress(entity.getType()).tell(player);
            }
        }).ifAbsent(() -> {
            Fmt.error("Please make a selection around the entity").tell(player);
        });
    }
}
