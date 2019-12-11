package me.dags.stopmotion.trigger.rule;

import me.dags.pitaya.config.Node;
import me.dags.stopmotion.trigger.Context;

public class Perm implements Rule {

    private final String name;
    private final String node;

    public Perm(String name) {
        this.name = name;
        this.node = "stopmotion.rule." + name;
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
        return "node=" + node;
    }
}
