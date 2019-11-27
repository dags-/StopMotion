package me.dags.animations.trigger.rule;

import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Rule;
import me.dags.animations.trigger.RuleType;
import me.dags.pitaya.config.Node;

public class None implements Rule {

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public boolean test(Context context) {
        return false;
    }

    @Override
    public RuleType getType() {
        return RuleType.NONE;
    }

    @Override
    public Rule fromNode(Node node) {
        return this;
    }

    @Override
    public void toNode(Node node) {

    }
}
