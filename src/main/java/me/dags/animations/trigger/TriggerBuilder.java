package me.dags.animations.trigger;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.trigger.type.And;
import me.dags.animations.trigger.type.Distance;
import me.dags.animations.trigger.type.Interact;
import me.dags.animations.util.recorder.PosRecord;

import java.util.LinkedList;

public class TriggerBuilder {

    private final PosRecord pos = new PosRecord();
    private final LinkedList<Trigger> triggers = new LinkedList<>();

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

    public TriggerBuilder add(Trigger trigger) {
        triggers.add(trigger);
        return this;
    }

    public NamedTrigger build(String name) {
        if (triggers.size() == 1) {
            return new NamedTrigger(name, triggers.getFirst());
        }
        if (triggers.size() > 1) {
            return new NamedTrigger(name, new And(triggers));
        }
        return NamedTrigger.NONE;
    }
}
