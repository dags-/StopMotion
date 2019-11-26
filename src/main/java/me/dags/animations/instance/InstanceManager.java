package me.dags.animations.instance;

import me.dags.animations.animation.AnimationRef;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.util.Registry;
import me.dags.animations.util.Translators;
import me.dags.animations.util.iterator.Direction;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import org.spongepowered.api.Sponge;

import java.util.Optional;
import java.util.stream.Collectors;

public class InstanceManager extends Registry<Instance> {

    public InstanceManager(Config storage) {
        super(storage, storage);
    }

    @Override
    protected Optional<Instance> deserialize(Node node) {
        return Sponge.getRegistry().getType(AnimationRef.class, node.get("animation", "")).map(animation -> {
            InstanceBuilder builder = new InstanceBuilder();
            builder.animation = animation;
            builder.name = node.get("name", "");
            builder.world = node.get("world", "");
            builder.origin = Translators.vec3i(node.node("origin"));
            builder.triggers = node.node("triggers").getList(Trigger::deserialize);
            builder.timeline = node.node("timeline").getList(Direction::deserialize);
            return new Instance(builder);
        });
    }

    @Override
    protected void serialize(Node node, Instance value) {
        node.set("name", value.getId());
        node.set("world", value.getWorld());
        node.set("origin", Translators.vec3i(value.getOrigin()));
        node.set("animation", value.getAnimation());
        node.set("triggers", value.getTriggers().stream().map(Trigger::serialize).collect(Collectors.toList()));
        node.set("timeline", value.getTimeline().stream().map(Direction::toString).collect(Collectors.toList()));
    }
}
