package me.dags.animations.trigger;

import me.dags.animations.trigger.type.None;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.util.OptionalValue;

public interface Trigger extends Node.Value<Trigger>, OptionalValue {

    Trigger NONE = new None();

    boolean test(Context context);

    TriggerType getType();

    @Override
    default boolean isPresent() {
        return true;
    }

    static Trigger deserialize(Node node) {
        String type = node.get("type", "");
        return TriggerType.fromName(type).serializer().fromNode(node);
    }

    static Node serialize(Trigger trigger) {
        Node node = Node.create();
        node.set("type", trigger.getType());
        trigger.toNode(node);
        return node;
    }
}
