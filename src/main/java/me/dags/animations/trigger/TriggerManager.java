package me.dags.animations.trigger;

import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.registry.NodeRegistry;

import java.util.Optional;

public class TriggerManager extends NodeRegistry<Trigger> {

    public TriggerManager(Config storage) {
        super(storage);
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
