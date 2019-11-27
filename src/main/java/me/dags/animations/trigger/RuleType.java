package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.rule.*;
import me.dags.pitaya.config.Node;

import java.util.Collections;

public enum RuleType {
    NONE(Rule.NONE),
    NAMED(new Trigger("named", new None())),
    AND(new And(Collections.emptyList())),
    MESSAGE(new Message("")),
    PERMISSION(new Perm("")),
    DISTANCE(new Distance(Vector3i.ZERO, 0)),
    INTERACT(new Interact(Vector3i.ZERO, Vector3i.ZERO)),
    ;

    private final Rule def;

    RuleType(Rule def) {
        this.def = def;
    }

    public Node.Value<Rule> serializer() {
        return def;
    }

    public static Rule deserialize(Node node) {
        return RuleType.fromName(node.get("type", "")).serializer().fromNode(node);
    }

    public static RuleType fromName(String name) {
        try {
            return RuleType.valueOf(name);
        } catch (Throwable t) {
            return RuleType.NONE;
        }
    }
}
