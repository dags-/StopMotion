package me.dags.animations.entity;

import com.flowpowered.math.vector.Vector3d;
import me.dags.animations.Animations;
import me.dags.animations.instance.Instance;
import me.dags.animations.trigger.rule.Rule;
import me.dags.animations.trigger.rule.RuleType;
import me.dags.animations.trigger.rule.Time;
import me.dags.animations.util.ClassUtils;
import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import me.dags.pitaya.registry.NodeRegistry;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityType;

import java.util.*;
import java.util.stream.Collectors;

public class EntityManager extends NodeRegistry<EntityInstance> {

    public EntityManager(Config config) {
        super(config);
    }

    @Override
    public void load() {
        super.load();
        Animations.log("Registry load complete. Registry: {}, Size: {}", ClassUtils.getTypeName(this), registry.size());
    }

    public void attachEntities() {
        for (EntityInstance entity : getAll()) {
            Optional<Instance> instance = Sponge.getRegistry().getType(Instance.class, entity.getLink());
            instance.ifPresent(value -> value.attachEntity(entity));
        }
    }

    @Override
    protected void serialize(Node node, EntityInstance entity) {
        node.set("name", entity.getId());
        node.set("link", entity.getLink());
        node.set("world", entity.getWorldName());
        rule(node.node("rule"), entity.getRule());
        vec(node.node("origin"), entity.getPosition());
        uuid(node.node("state"), entity.getState());
        entities(node.node("entities"), entity.getEntities());
    }

    @Override
    protected Optional<EntityInstance> deserialize(String name, Node node) {
        EntityInstanceBuilder builder = new EntityInstanceBuilder();
        builder.name = node.get("name", "");
        builder.link = node.get("link", "");
        builder.world = node.get("world", "");
        builder.rule = rule(node.node("rule"));
        builder.origin = vec(node.node("origin"));
        builder.entities = entities(node.node("entities"));
        builder.state = uuid(node.node("state"));
        if (builder.entities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new EntityInstance(builder));
    }

    private static void rule(Node node, Rule rule) {
        rule.toNode(node);
    }

    private static Time rule(Node node) {
        return (Time) RuleType.TIME.serializer().fromNode(node);
    }

    private static void vec(Node node, Vector3d vec) {
        node.set("x", vec.getX());
        node.set("y", vec.getY());
        node.set("z", vec.getZ());
    }

    private static Vector3d vec(Node node) {
        double x = node.get("x", 0D);
        double y = node.get("y", 0D);
        double z = node.get("z", 0D);
        return new Vector3d(x, y, z);
    }

    private static void entities(Node node, List<EntityArchetype> entities) {
        node.set(entities.stream().map(EntityManager::entity).collect(Collectors.toList()));
    }

    private static List<EntityArchetype> entities(Node node) {
        List<EntityArchetype> list = new LinkedList<>();
        node.iterate(n -> entity(n).ifPresent(list::add));
        return list;
    }

    private static Optional<EntityArchetype> entity(Node node) {
        return Sponge.getRegistry().getType(EntityType.class, node.get("type", "")).map(type -> {
            ConfigurationNode data = node.node("data").backing();
            DataView view = DataTranslators.CONFIGURATION_NODE.translate(data);
            return EntityArchetype.builder().type(type).entityData(view).build();
        });
    }

    private static CommentedConfigurationNode entity(EntityArchetype archetype) {
        CommentedConfigurationNode backing = SimpleCommentedConfigurationNode.root();
        ConfigurationNode data = DataTranslators.CONFIGURATION_NODE.translate(archetype.getEntityData());
        backing.getNode("type").setValue(archetype.getType().getId());
        backing.getNode("data").setValue(data);
        return backing;
    }

    private static void uuid(Node node, Collection<UUID> uuid) {
        node.set(uuid.stream().map(UUID::toString).collect(Collectors.toList()));
    }

    private static List<UUID> uuid(Node node) {
        return node.getList(n -> UUID.fromString(n.get("")));
    }
}
