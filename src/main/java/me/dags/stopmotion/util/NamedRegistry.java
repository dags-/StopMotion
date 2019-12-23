package me.dags.stopmotion.util;

import me.dags.pitaya.config.Config;
import me.dags.pitaya.registry.NodeRegistry;
import me.dags.stopmotion.StopMotion;
import org.spongepowered.api.CatalogType;

public abstract class NamedRegistry<T extends CatalogType> extends NodeRegistry<T> {

    private final String name;

    public NamedRegistry(String name, Config storage) {
        super(storage);
        this.name = name;
    }

    @Override
    public void load() {
        super.load();
        StopMotion.log("Registry load complete. Registry: {}, Size: {}", name, registry.size());
    }
}
