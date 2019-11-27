package me.dags.animations.trigger.rule;

import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Rule;
import me.dags.animations.trigger.RuleType;
import me.dags.pitaya.config.Node;

public class Perm implements Rule {

    private final String name;
    private final String node;

    public Perm(String name) {
        this.name = name;
        this.node = "animation.rule." + name;
    }

    @Override
    public boolean test(Context context) {
        return context.player.hasPermission(node);
    }

    @Override
    public RuleType getType() {
        return RuleType.PERMISSION;
    }

    @Override
    public Rule fromNode(Node node) {
        return new Perm(node.get("node", ""));
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        node.set("node", name);
    }

    @Override
    public String toString() {
        return "Permission{node=" + node + "}";
    }
}
