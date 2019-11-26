package me.dags.animations.trigger.type;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerType;
import me.dags.animations.util.Translators;
import me.dags.pitaya.config.Node;

public class Distance implements Trigger {

    private final int radius;
    private final int radius2;
    private final Vector3i position;

    public Distance(Vector3i position, int radius) {
        this.radius = radius;
        this.position = position;
        this.radius2 = radius * radius;
    }

    @Override
    public TriggerType getType() {
        return TriggerType.DISTANCE;
    }

    @Override
    public Trigger fromNode(Node node) {
        return new Distance(
                Translators.vec3i(node.node("center")),
                node.get("radius", 0)
        );
    }

    @Override
    public void toNode(Node node) {
        node.set("radius", radius);
        node.set("center", Translators.vec3i(position));
    }

    @Override
    public boolean test(Context context) {
        return context.position.distanceSquared(position) <= radius2;
    }

    @Override
    public String toString() {
        return "Radius{"
                + "center=" + position
                + ", radius=" + radius
                + "}";
    }
}
