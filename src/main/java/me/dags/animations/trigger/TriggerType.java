package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.type.*;
import me.dags.pitaya.config.Node;

import java.util.Collections;

public enum TriggerType {
    NONE(Trigger.NONE),
    NAMED(new NamedTrigger("named", new None())),
    AND(new And(Collections.emptyList())),
    MESSAGE(new Message("")),
    DISTANCE(new Distance(Vector3i.ZERO, 0)),
    INTERACT(new Interact(Vector3i.ZERO, Vector3i.ZERO)),
    ;

    private final Trigger def;

    TriggerType(Trigger def) {
        this.def = def;
    }

    public Node.Value<Trigger> serializer() {
        return def;
    }

    public static TriggerType fromName(String name) {
        try {
            return TriggerType.valueOf(name);
        } catch (Throwable t) {
            return TriggerType.NONE;
        }
    }
}
