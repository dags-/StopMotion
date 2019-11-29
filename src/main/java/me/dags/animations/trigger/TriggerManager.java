package me.dags.animations.trigger;

import me.dags.animations.Animations;
import me.dags.animations.util.ClassUtils;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.registry.NodeRegistry;

import java.util.Optional;

public class TriggerManager extends NodeRegistry<Trigger> {

    public TriggerManager(Config storage) {
        super(storage);
    }

    @Override
    public void load() {
        super.load();
        Animations.log("Registry load complete. Registry: {}, Size: {}", ClassUtils.getTypeName(this), registry.size());
    }

    @Override
    protected void serialize(Node node, Trigger value) {
        value.toNode(node);
    }

    @Override
    protected Optional<Trigger> deserialize(String name, Node node) {
        Trigger trigger = Trigger.NONE.fromNode(node);
        if (trigger.isAbsent()) {
            return Optional.empty();
        }
        return Optional.of(trigger);
    }
}
