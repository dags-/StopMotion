package me.dags.animations.trigger;

import me.dags.animations.trigger.rule.And;
import me.dags.animations.trigger.rule.Rule;

import java.util.LinkedList;

public class TriggerBuilder {

    private final LinkedList<Rule> triggers = new LinkedList<>();

    public TriggerBuilder add(Rule trigger) {
        triggers.add(trigger);
        return this;
    }

    public Trigger build(String name) {
        if (triggers.size() == 1) {
            return new Trigger(name, triggers.getFirst());
        }
        if (triggers.size() > 1) {
            return new Trigger(name, new And(triggers));
        }
        return Trigger.NONE;
    }
}
