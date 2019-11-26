package me.dags.animations.trigger;

import me.dags.animations.util.Registry;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;

import java.util.Optional;

public class TriggerManager extends Registry<NamedTrigger> {

    public TriggerManager(Config storage) {
        super(storage, storage);
    }

    @Override
    protected Optional<NamedTrigger> deserialize(Node node) {
        NamedTrigger trigger = NamedTrigger.NONE.fromNode(node);
        if (trigger.isAbsent()) {
            return Optional.empty();
        }
        return Optional.of(trigger);
    }

    @Override
    protected void serialize(Node node, NamedTrigger value) {
        value.toNode(node);
    }
}
