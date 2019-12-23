package me.dags.stopmotion.trigger;

import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.stopmotion.util.NamedRegistry;

import java.util.Optional;

public class TriggerManager extends NamedRegistry<Trigger> {

    public TriggerManager(Config storage) {
        super("Triggers", storage);
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
