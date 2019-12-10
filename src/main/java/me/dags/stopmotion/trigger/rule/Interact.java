package me.dags.stopmotion.trigger.rule;

import com.flowpowered.math.vector.Vector3i;
import me.dags.stopmotion.trigger.Context;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.util.Translators;

public class Interact implements Rule {

    private final Vector3i min;
    private final Vector3i max;

    public Interact(Vector3i min, Vector3i max) {
        this.min = min.min(max);
        this.max = min.max(max);
    }

    @Override
    public boolean test(Context context) {
        if (context.type != getType()) {
            return false;
        }
        return above(context.clicked, min) && below(context.clicked, max);
    }

    @Override
    public RuleType getType() {
        return RuleType.INTERACT;
    }

    @Override
    public Rule fromNode(Node node) {
        return new Interact(
                Translators.vec3i(node.node("min")),
                Translators.vec3i(node.node("max"))
        );
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        Translators.vec3i(node.node("min"), min);
        Translators.vec3i(node.node("max"), max);
    }

    @Override
    public String toString() {
        return "min=" + min + ", max=" + max;
    }

    private static boolean above(Vector3i pos, Vector3i min) {
        return pos.getX() >= min.getX() && pos.getY() >= min.getY() && pos.getZ() >= min.getZ();
    }

    private static boolean below(Vector3i pos, Vector3i max) {
        return pos.getX() <= max.getX() && pos.getY() <= max.getY() && pos.getZ() <= max.getZ();
    }
}
