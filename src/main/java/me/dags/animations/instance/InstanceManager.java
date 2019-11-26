package me.dags.animations.instance;

import me.dags.animations.animation.Animation;
import me.dags.animations.trigger.NamedTrigger;
import me.dags.animations.util.Registry;
import me.dags.animations.util.Translators;
import me.dags.animations.util.iterator.Direction;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import org.spongepowered.api.Sponge;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstanceManager extends Registry<Instance> {

    public InstanceManager(Config storage) {
        super(storage, storage);
    }

    @Override
    protected Optional<Instance> deserialize(Node node) {
        return Sponge.getRegistry().getType(Animation.class, node.get("animation", "")).map(animation -> {
            InstanceBuilder builder = new InstanceBuilder();
            builder.animation = animation;
            builder.name = node.get("name", "");
            builder.world = node.get("world", "");
            builder.origin = Translators.vec3i(node.node("origin"));
            builder.triggers = getList(node.node("triggers"));
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
        node.set("triggers", value.getTriggers().stream().map(NamedTrigger::getId).collect(Collectors.toList()));
        node.set("timeline", value.getTimeline().stream().map(Direction::toString).collect(Collectors.toList()));
    }

    private static List<NamedTrigger> getList(Node node) {
        List<NamedTrigger> list = new LinkedList<>();
        node.iterate(value -> Sponge.getRegistry().getType(NamedTrigger.class, value.get("")).ifPresent(list::add));
        return list;
    }
}
