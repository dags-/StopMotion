package me.dags.animations.command;

import me.dags.animations.Animations;
import me.dags.animations.entity.EntityInstance;
import me.dags.animations.entity.EntityInstanceBuilder;
import me.dags.animations.trigger.rule.Time;
import me.dags.animations.util.recorder.PosRecorder;
import me.dags.pitaya.cache.Cache;
import me.dags.pitaya.command.annotation.Command;
import me.dags.pitaya.command.annotation.Description;
import me.dags.pitaya.command.annotation.Permission;
import me.dags.pitaya.command.annotation.Src;
import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.AABB;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EntityCommands extends Cache<EntityInstanceBuilder> {

    private final Animations plugin;

    public EntityCommands(Animations plugin) {
        super(5, TimeUnit.MINUTES, EntityInstanceBuilder::new);
        this.plugin = plugin;
    }

    @Command("entity|ent time <min> <max>")
    @Permission("animations.command.entity.time")
    @Description("Set the time range that this entity animation should display for")
    public void time(@Src Player player, long min, long max) {
        must(player).rule = new Time(min, max);
        Fmt.info("Set time rule min=").stress(min).info(", max=").stress(max).tell(player);
    }

    @Command("entity|ent select")
    @Permission("animations.command.entity.select")
    @Description("Add all entities between your two selected points to the entity animation")
    public void select(@Src Player player) {
        PosRecorder.getSelection(player).ifPresent((pos1, pos2) -> {
            EntityInstanceBuilder builder = must(player);

            Set<Entity> entities = player.getWorld().getIntersectingEntities(new AABB(pos1, pos2));
            for (Entity entity : entities) {
                EntityArchetype archetype = entity.createArchetype();
                builder.entities.add(archetype);
                builder.world = entity.getWorld().getName();
                builder.origin = entity.getLocation().getPosition().sub(0, 1, 0);
                Fmt.info("Added entity ").stress(entity.getType()).tell(player);
            }
        }).ifAbsent(() -> {
            Fmt.error("Please make a selection around the entity").tell(player);
        });
    }

    @Command("entity|ent save")
    @Permission("animations.command.entity.save")
    @Description("Save the entity animation to disk")
    public void save(@Src Player player, String name) {
        drain(player, "You are not currently building an entity animation").onPass(builder -> {
            builder.name = name.toLowerCase();
            EntityInstance instance = new EntityInstance(builder);
            plugin.getEntities().register(instance);
            Fmt.info("Saved entity animation ").stress(builder.name).tell(player);
        }).onPass(err -> {
            Fmt.error(err).tell(player);
        });
    }
}
