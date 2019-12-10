package me.dags.stopmotion.trigger;

import me.dags.stopmotion.trigger.rule.Rule;
import me.dags.stopmotion.trigger.rule.RuleType;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.util.optional.OptionalValue;
import org.spongepowered.api.CatalogType;

public class Trigger implements OptionalValue, CatalogType, Node.Value<Trigger> {

    public static final int MAX_RADIUS = 128;
    static final Trigger NONE = new Trigger("none", Rule.NONE);

    private final String name;
    private final Rule rule;

    public Trigger(String name, Rule rule) {
        this.name = name.toLowerCase();
        this.rule = rule;
    }

    public boolean test(Context context) {
        return rule.test(context);
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
    public boolean isPresent() {
        return rule.isPresent();
    }

    @Override
    public Trigger fromNode(Node node) {
        String name = node.get("name", "");
        Rule rule = RuleType.deserialize(node.node("rule"));
        return new Trigger(name, rule);
    }

    @Override
    public void toNode(Node node) {
        node.node("name").set(getId());
        node.node("rule").set(rule);
    }

    @Override
    public String toString() {
        return "Trigger{"
                + "name=" + name
                + ", rule=" + rule
                + "}";
    }
}
