package me.dags.motion.trigger.rule;

import me.dags.motion.trigger.Context;
import me.dags.pitaya.command.element.function.Filter;
import me.dags.pitaya.config.Node;

public class Message implements Rule {

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    @Override
    public RuleType getType() {
        return RuleType.MESSAGE;
    }

    @Override
    public Rule fromNode(Node node) {
        return new Message(node.get("message", ""));
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        node.set("message", message);
    }

    @Override
    public boolean test(Context context) {
        if (context.type != getType()) {
            return false;
        }
        return !message.isEmpty() && Filter.EQUALS_IGNORE_CASE.test(context.message, message);
    }

    @Override
    public String toString() {
        return "message=" + message;
    }
}
