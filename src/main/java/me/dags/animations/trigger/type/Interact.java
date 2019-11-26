package me.dags.animations.trigger.type;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerType;
import me.dags.animations.util.Translators;
import me.dags.pitaya.config.Node;

public class Interact implements Trigger {

    private final Vector3i min;
    private final Vector3i max;

    public Interact(Vector3i min, Vector3i max) {
        this.min = min.min(max);
        this.max = min.max(max);
    }

    @Override
    public boolean test(Context context) {
        return above(context.position, min) && below(context.position, max);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.INTERACT;
    }

    @Override
    public Trigger fromNode(Node node) {
        return new Interact(
                Translators.vec3i(node.node("min")),
                Translators.vec3i(node.node("max"))
        );
    }

    @Override
    public void toNode(Node node) {
        node.set("min", Translators.vec3i(min));
        node.set("max", Translators.vec3i(max));
    }

    @Override
    public String toString() {
        return "Interact{"
                + "min=" + min
                + ", max=" + max
                + "}";
    }

    private static boolean above(Vector3i pos, Vector3i min) {
        return pos.getX() >= min.getX() && pos.getY() >= min.getY() && pos.getZ() >= min.getZ();
    }

    private static boolean below(Vector3i pos, Vector3i max) {
        return pos.getX() <= max.getX() && pos.getY() <= max.getY() && pos.getZ() <= max.getZ();
    }
}
