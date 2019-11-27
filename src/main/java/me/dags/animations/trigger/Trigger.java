package me.dags.animations.trigger;

import me.dags.pitaya.config.Node;
import org.spongepowered.api.CatalogType;

public class Trigger implements Rule, CatalogType {

    static final Trigger NONE = new Trigger("none", Rule.NONE);

    private final String name;
    private final Rule trigger;

    public Trigger(String name, Rule trigger) {
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
    public RuleType getType() {
        return RuleType.NAMED;
    }

    @Override
    public Trigger fromNode(Node node) {
        String name = node.get("name", "");
        Rule trigger = RuleType.deserialize(node);
        return new Trigger(name, trigger);
    }

    @Override
    public void toNode(Node node) {
        Rule.super.toNode(node);
        node.set("name", getName());
        trigger.toNode(node);
    }

    @Override
    public String toString() {
        return "Trigger{"
                + "name=" + name
                + ", rule=" + trigger
                + "}";
    }
}
