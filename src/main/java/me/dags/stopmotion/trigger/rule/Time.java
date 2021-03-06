package me.dags.stopmotion.trigger.rule;

import me.dags.pitaya.config.Node;
import me.dags.stopmotion.trigger.Context;
import org.spongepowered.api.world.World;

public class Time implements Rule {

    private final long min;
    private final long max;

    public Time(long min, long max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return "min=" + min + ", max=" + max;
    }

    @Override
    public boolean test(Context context) {
        return test(context.world);
    }

    public boolean test(World world) {
        return test(world.getProperties().getWorldTime());
    }

    public boolean test(long time) {
        if (max < min) {
            return time >= min || time <= max;
        }
        return time >= min && time <= max;
    }

    @Override
    public RuleType getType() {
        return RuleType.TIME;
    }

    @Override
    public Rule fromNode(Node node) {
        return new Time(
                node.get("min", 0L),
                node.get("max", 0L)
        );
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        node.set("min", min);
        node.set("max", max);
    }
}
