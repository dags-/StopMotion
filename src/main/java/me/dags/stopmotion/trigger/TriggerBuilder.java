package me.dags.stopmotion.trigger;

import me.dags.pitaya.util.optional.Result;
import me.dags.stopmotion.trigger.rule.And;
import me.dags.stopmotion.trigger.rule.Rule;

import java.util.LinkedList;

public class TriggerBuilder {

    private final LinkedList<Rule> triggers = new LinkedList<>();

    public Rule add(Rule rule) {
        triggers.add(rule);
        return rule;
    }

    public Result<Trigger, String> build(String name) {
        if (triggers.size() == 1) {
            return Result.pass(new Trigger(name, triggers.getFirst()));
        }
        if (triggers.size() > 1) {
            return Result.pass(new Trigger(name, new And(triggers)));
        }
        return Result.fail("No rules defined");
    }
}
