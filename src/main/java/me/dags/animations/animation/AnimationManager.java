package me.dags.animations.animation;

import me.dags.pitaya.command.fmt.Fmt;
import org.spongepowered.api.registry.CatalogRegistryModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnimationManager implements CatalogRegistryModule<AnimationRef> {

    private final Path directory;
    private final Map<String, AnimationRef> registry = new HashMap<>();

    public AnimationManager(Path directory) {
        this.directory = directory;
    }

    @Override
    public Optional<AnimationRef> getById(String id) {
        return Optional.ofNullable(registry.get(id));
    }

    @Override
    public Collection<AnimationRef> getAll() {
        return registry.values();
    }

    @Override
    public void registerDefaults() {
        try {
            registry.clear();
            Files.createDirectories(directory);

            Files.newDirectoryStream(directory).forEach(path -> {
                String fileName = path.getFileName().toString();
                Fmt.info("Found file ").stress(fileName).log();
                if (fileName.endsWith(".nbt")) {
                    String name = fileName.replace(".nbt", "");
                    registry.put(name, new AnimationRef(path, name));
                    Fmt.info("Registered animation ").stress(name).log();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(Animation animation) {
        Path path = directory.resolve(animation.getId() + ".nbt");
        Animation.save(animation, path);
        registry.put(animation.getName(), new AnimationRef(path, animation));
    }
}
