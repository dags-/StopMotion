package me.dags.animations.trigger;

import me.dags.animations.trigger.rule.None;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.util.OptionalValue;

public interface Rule extends Node.Value<Rule>, OptionalValue {

    Rule NONE = new None();

    boolean test(Context context);

    RuleType getType();

    @Override
    default boolean isPresent() {
        return true;
    }

    @Override
    default void toNode(Node node) {
        node.set("type", getType().toString());
    }
}
