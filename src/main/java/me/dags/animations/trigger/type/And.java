package me.dags.animations.trigger.type;

import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerType;
import me.dags.pitaya.config.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class And implements Trigger {

    private final List<Trigger> triggers;

    public And(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    @Override
    public Trigger fromNode(Node node) {
        List<Trigger> triggers = new LinkedList<>();
        node.node("triggers").iterate(value -> {
            Trigger trigger = Trigger.deserialize(value);
            triggers.add(trigger);
        });
        return new And(triggers);
    }

    @Override
    public void toNode(Node node) {
        node.set("trigger", triggers.stream().map(Trigger::serialize).collect(Collectors.toList()));
    }

    @Override
    public boolean test(Context context) {
        for (Trigger trigger : triggers) {
            if (!trigger.test(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TriggerType getType() {
        return TriggerType.AND;
    }

    @Override
    public String toString() {
        return "And{triggers=" + triggers + "}";
    }
}
