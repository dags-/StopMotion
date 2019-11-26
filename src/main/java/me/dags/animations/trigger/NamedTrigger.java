package me.dags.animations.trigger;

import me.dags.pitaya.config.Node;
import org.spongepowered.api.CatalogType;

public class NamedTrigger implements Trigger, CatalogType {

    public static final NamedTrigger NONE = new NamedTrigger("none", Trigger.NONE);

    private final String name;
    private final Trigger trigger;

    public NamedTrigger(String name, Trigger trigger) {
        this.name = name.toLowerCase();
        this.trigger = trigger;
    }

    @Override
    public boolean isPresent() {
        return trigger.isPresent();
    }

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean test(Context context) {
        return trigger.test(context);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.NAMED;
    }

    @Override
    public NamedTrigger fromNode(Node node) {
        String name = node.get("name", "");
        Trigger trigger = Trigger.deserialize(node);
        return new NamedTrigger(name, trigger);
    }

    @Override
    public void toNode(Node node) {
        node.set("name", getName());
        Trigger.serialize(trigger);
    }
}
