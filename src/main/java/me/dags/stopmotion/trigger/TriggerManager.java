package me.dags.stopmotion.trigger;

import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.registry.NodeRegistry;
import me.dags.stopmotion.StopMotion;
import me.dags.stopmotion.util.ClassUtils;

import java.util.Optional;

public class TriggerManager extends NodeRegistry<Trigger> {

    public TriggerManager(Config storage) {
        super(storage);
    }

    @Override
    public void load() {
        super.load();
        StopMotion.log("Registry load complete. Registry: {}, Size: {}", ClassUtils.getTypeName(this), registry.size());
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
