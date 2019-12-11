package me.dags.stopmotion.trigger.rule;

import me.dags.pitaya.config.Node;
import me.dags.stopmotion.trigger.Context;

import java.util.List;

public class And implements Rule {

    private final List<Rule> rules;

    public And(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public Rule fromNode(Node node) {
        return new And(node.node("rules").getList(RuleType::deserialize));
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        node.set("rules", rules);
    }

    @Override
    public boolean test(Context context) {
        for (Rule trigger : rules) {
            if (!trigger.test(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RuleType getType() {
        return RuleType.AND;
    }

    @Override
    public String toString() {
        return "And{rules=" + rules + "}";
    }
}
