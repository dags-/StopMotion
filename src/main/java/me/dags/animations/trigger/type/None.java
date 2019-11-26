package me.dags.animations.trigger.type;

import me.dags.animations.trigger.Context;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerType;
import me.dags.pitaya.config.Node;

public class None implements Trigger {

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public boolean test(Context context) {
        return false;
    }

    @Override
    public TriggerType getType() {
        return TriggerType.NONE;
    }

    @Override
    public Trigger fromNode(Node node) {
        return this;
    }

    @Override
    public void toNode(Node node) {

    }
}
