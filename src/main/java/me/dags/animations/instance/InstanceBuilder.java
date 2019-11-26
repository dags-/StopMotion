package me.dags.animations.instance;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.AnimationRef;
import me.dags.animations.trigger.Trigger;
import me.dags.animations.trigger.TriggerType;
import me.dags.animations.trigger.type.And;
import me.dags.animations.trigger.type.Or;
import me.dags.animations.util.iterator.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class InstanceBuilder {

    public String name;
    public String world;
    public Vector3i origin;
    public AnimationRef animation;
    public List<Trigger> triggers;
    public List<Direction> timeline;

    private ListBuilder<Trigger> triggerBuilder;

    public void origin(Location<World> origin) {
        this.world = origin.getExtent().getName();
        this.origin = origin.getBlockPosition();
    }

    public ListBuilder<Trigger> triggers() {
        if (triggerBuilder == null) {
            triggerBuilder = new ListBuilder<>();
        }
        return triggerBuilder;
    }

    public void add(TriggerType type) {
        if (triggerBuilder == null) {
            return;
        }

        if (type == TriggerType.AND) {
            List<Trigger> triggers = triggerBuilder.build();
            Trigger trigger = new And(triggers);
            if (triggers == null) {
                triggers = new LinkedList<>();
            }
            triggers.add(trigger);
            triggerBuilder = new ListBuilder<>();
            return;
        }

        if (type == TriggerType.OR) {
            List<Trigger> triggers = triggerBuilder.build();
            Trigger trigger = new Or(triggers);
            if (triggers == null) {
                triggers = new LinkedList<>();
            }
            triggers.add(trigger);
            triggerBuilder = new ListBuilder<>();
        }
    }

    public void add(Direction... directions) {
        if (timeline == null) {
            timeline = new LinkedList<>();
        }
        Collections.addAll(timeline, directions);
    }

    public Optional<Instance> build(String name) {
        if (world == null || world.isEmpty()) {
            return Optional.empty();
        }
        if (origin == null || origin == Vector3i.ZERO) {
            return Optional.empty();
        }
        if (animation == null) {
            return Optional.empty();
        }
        if (triggers == null || triggers.isEmpty()) {
            return Optional.empty();
        }
        if (timeline == null || timeline.isEmpty()) {
            timeline = Collections.singletonList(Direction.FORWARD);
        }
        this.name = name;
        return Optional.of(new Instance(this));
    }

    public static class ListBuilder<T> {

        private final List<T> list = new LinkedList<>();

        public void add(T t) {
            list.add(t);
        }

        public List<T> build() {
            return new ArrayList<>(list);
        }
    }
}
