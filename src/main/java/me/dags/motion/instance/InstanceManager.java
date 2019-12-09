package me.dags.motion.instance;

import me.dags.motion.StopMotion;
import me.dags.motion.animation.Animation;
import me.dags.motion.animation.AnimationMode;
import me.dags.motion.trigger.Trigger;
import me.dags.motion.util.ClassUtils;
import me.dags.motion.util.iterator.Direction;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.registry.NodeRegistry;
import me.dags.pitaya.util.Translators;
import org.spongepowered.api.Sponge;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstanceManager extends NodeRegistry<Instance> {

    public InstanceManager(Config storage) {
        super(storage);
    }

    @Override
    public void load() {
        super.load();
        StopMotion.log("Registry load complete. Registry: {}, Size: {}", ClassUtils.getTypeName(this), registry.size());
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
            builder.animation = animation;
            builder.name = node.get("name", "");
            builder.world = node.get("world", "");
            builder.state = node.get("state", 0);
            builder.origin = Translators.vec3i(node.node("origin"));
            builder.triggers = getTriggerList(node.node("triggers"));
            builder.mode = AnimationMode.valueOf(node.get("mode", ""));
            builder.directions = node.node("directions").getList(Direction::deserialize);
            return new Instance(builder);
        });
    }

    private static List<Trigger> getTriggerList(Node node) {
        List<Trigger> list = new LinkedList<>();
        node.iterate(value -> Sponge.getRegistry().getType(Trigger.class, value.get("")).ifPresent(list::add));
        return list;
    }
}
