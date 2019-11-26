package me.dags.animations.util;

import me.dags.pitaya.config.Config;
import me.dags.pitaya.config.Node;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.registry.CatalogRegistryModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Registry<T extends CatalogType> implements CatalogRegistryModule<T> {

    private final Node section;
    private final Config storage;
    private final Map<String, T> registry = new HashMap<>();

    public Registry(Config storage, Node section) {
        this.section = section;
        this.storage = storage;
    }

    @Override
    public Optional<T> getById(String id) {
        return Optional.ofNullable(registry.get(id));
    }

    @Override
    public Collection<T> getAll() {
        return registry.values();
    }

    public void register(T value) {
        registry.put(value.getId(), value);
        serialize(section.node(value.getId()), value);
        storage.save();
    }

    public void load() {
        registry.clear();
        section.iterate((key, value) -> deserialize(value).ifPresent(t -> registry.put(t.getId(), t)));
    }

    public void delete(String name) {
        if (registry.remove(name) != null) {
            section.clear(name);
            storage.save();
        }
    }

    protected abstract Optional<T> deserialize(Node node);

    protected abstract void serialize(Node node, T value);
}
