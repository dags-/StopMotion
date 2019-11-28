package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.rule.And;
import me.dags.animations.trigger.rule.Distance;
import me.dags.animations.trigger.rule.Interact;
import me.dags.animations.trigger.rule.Rule;
import me.dags.animations.util.recorder.PosRecord;

import java.util.LinkedList;

public class TriggerBuilder {

    private final PosRecord pos = new PosRecord();
    private final LinkedList<Rule> triggers = new LinkedList<>();

    public PosRecord pos() {
        return pos;
    }

    public boolean distance(int radius) {
        if (pos.pos1 != Vector3i.ZERO) {
            triggers.add(new Distance(pos.pos1, radius));
            pos.reset();
            return true;
        }
        return false;
    }

    public boolean interact() {
        if (pos.pos1 != Vector3i.ZERO && pos.pos2 != Vector3i.ZERO) {
            triggers.add(new Interact(pos.pos1, pos.pos2));
            pos.reset();
            return true;
        }
        return false;
    }

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
