package me.dags.animations.trigger;

import me.dags.pitaya.config.Node;

public interface Trigger extends Node.Value<Trigger> {

    boolean test(Context context);

    TriggerType getType();

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
