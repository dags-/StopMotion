package me.dags.animations.trigger.rule;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Rule;
import me.dags.animations.trigger.RuleType;
import me.dags.animations.util.Translators;
import me.dags.pitaya.config.Node;

public class Interact implements Rule {

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
        Translators.vec3i(node.node("min"), max);
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
