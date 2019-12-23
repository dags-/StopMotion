package me.dags.stopmotion.instance;

import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.registry.NodeRegistry;
import me.dags.pitaya.util.Translators;
import me.dags.stopmotion.StopMotion;
import me.dags.stopmotion.animation.Animation;
import me.dags.stopmotion.animation.AnimationMode;
import me.dags.stopmotion.trigger.Trigger;
import me.dags.stopmotion.util.NamedRegistry;
import me.dags.stopmotion.util.iterator.Direction;
import org.spongepowered.api.Sponge;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstanceManager extends NamedRegistry<Instance> {

    public InstanceManager(Config storage) {
        super("Animations", storage);
    }

    @Override
    protected void serialize(Node node, Instance value) {
        node.set("name", value.getId());
        node.set("world", value.getWorld());
        Translators.vec3i(node.node("origin"), value.getOrigin());
        node.set("animation", value.getAnimation().getId());
        node.set("state", value.getState());
        node.set("mode", value.getAnimationMode().toString());
        node.set("triggers", value.getTriggers().stream().map(Trigger::getId).collect(Collectors.toList()));
        node.set("directions", value.getDirections().stream().map(Direction::toString).collect(Collectors.toList()));
    }

    @Override
    protected Optional<Instance> deserialize(String s, Node node) {
        return Sponge.getRegistry().getType(Animation.class, node.get("animation", "")).map(animation -> {
            InstanceBuilder builder = new InstanceBuilder();
            String name = node.get("name", "");
            builder.animation = animation;
            builder.world = node.get("world", "");
            builder.state = node.get("state", 0);
            builder.origin = Translators.vec3i(node.node("origin"));
            builder.triggers = getTriggerList(node.node("triggers"));
            builder.mode = AnimationMode.valueOf(node.get("mode", ""));
            builder.directions = node.node("directions").getList(Direction::deserialize);
            return new Instance(name, builder);
        });
    }

    private static List<Trigger> getTriggerList(Node node) {
        List<Trigger> list = new LinkedList<>();
        node.iterate(value -> Sponge.getRegistry().getType(Trigger.class, value.get("")).ifPresent(list::add));
        return list;
    }
}
