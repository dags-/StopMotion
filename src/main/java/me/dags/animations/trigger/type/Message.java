package me.dags.animations.trigger.type;

import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerType;
import me.dags.pitaya.command.element.function.Filter;
import me.dags.pitaya.config.Node;

public class Message implements Trigger {

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    @Override
    public TriggerType getType() {
        return TriggerType.MESSAGE;
    }

    @Override
    public Trigger fromNode(Node node) {
        return new Message(node.get("message", ""));
    }

    @Override
    public void toNode(Node node) {
        node.set("message", message);
    }

    @Override
    public boolean test(Context context) {
        return !message.isEmpty() && Filter.EQUALS_IGNORE_CASE.test(context.message, message);
    }

    @Override
    public String toString() {
        return "Message{message=" + message + "}";
    }
}
