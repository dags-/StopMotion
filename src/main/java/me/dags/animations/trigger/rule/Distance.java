package me.dags.animations.trigger.rule;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.Context;
import me.dags.animations.util.Translators;
import me.dags.pitaya.config.Node;

public class Distance implements Rule {

    private final int radius;
    private final int radius2;
    private final Vector3i position;

    public Distance(Vector3i position, int radius) {
        this.radius = radius;
        this.position = position;
        this.radius2 = radius * radius;
    }

    @Override
    public RuleType getType() {
        return RuleType.DISTANCE;
    }

    @Override
    public Rule fromNode(Node node) {
        return new Distance(
                Translators.vec3i(node.node("position")),
                node.get("radius", 0)
        );
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        node.set("radius", radius);
        Translators.vec3i(node.node("position"), position);
    }

    @Override
    public boolean test(Context context) {
        return context.position.distanceSquared(position) <= radius2;
    }

    @Override
    public String toString() {
        return "Distance{"
                + "position=" + position
                + ", radius=" + radius
                + "}";
    }
}
