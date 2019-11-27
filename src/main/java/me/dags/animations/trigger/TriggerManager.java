package me.dags.animations.trigger;

import me.dags.animations.util.Registry;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;

import java.util.Optional;

public class TriggerManager extends Registry<Trigger> {

    public TriggerManager(Config storage) {
        super(storage, storage);
    }

    @Override
    protected Optional<Trigger> deserialize(Node node) {
        Trigger trigger = Trigger.NONE.fromNode(node);
        if (trigger.isAbsent()) {
            return Optional.empty();
        }
        return Optional.of(trigger);
    }

    @Override
    protected void serialize(Node node, Trigger value) {
        value.toNode(node);
    }
}
