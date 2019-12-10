package me.dags.stopmotion.trigger.rule;

import me.dags.stopmotion.trigger.Context;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.util.optional.OptionalValue;

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
