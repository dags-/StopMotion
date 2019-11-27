package me.dags.animations.trigger.rule;

import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Rule;
import me.dags.animations.trigger.RuleType;
import me.dags.pitaya.config.Node;

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
