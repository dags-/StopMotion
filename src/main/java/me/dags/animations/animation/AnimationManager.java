package me.dags.animations.animation;

import me.dags.animations.Animations;
import me.dags.animations.util.Registry;
import me.dags.animations.util.duration.Duration;
import me.dags.pitaya.util.PluginUtils;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AnimationManager implements CatalogRegistryModule<Animation> {

    private final Path directory;
    private final Duration expiration;
    private final Map<String, Animation> registry = new HashMap<>();

    private Task task = null;

    public AnimationManager(Path directory) {
        this.directory = directory;
        this.expiration = Duration.mins(10);
    }

    @Override
    public Optional<Animation> getById(String id) {
        return Optional.ofNullable(registry.get(id));
    }

    @Override
    public Collection<Animation> getAll() {
        return registry.values();
    }

    public void delete(String name) {
        getById(name).ifPresent(animation -> {
            try {
                registry.remove(name);
                Files.delete(animation.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void load() {
        if (task != null) {
            task.cancel();
        }

        task = Task.builder()
                .execute(this::tick)
                .delay(1, TimeUnit.MINUTES)
                .interval(1, TimeUnit.MINUTES)
                .submit(PluginUtils.getCurrentPluginInstance());

        try {
            registry.clear();
            Files.createDirectories(directory);

            Files.newDirectoryStream(directory).forEach(path -> {
                String fileName = path.getFileName().toString();
                if (fileName.endsWith(".nbt")) {
                    String name = fileName.replace(".nbt", "");
                    registry.put(name, new Animation(path, name, expiration));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Animations.log("Registry load complete. Registry: {}, Size: {}", Registry.getTypeName(this), registry.size());
    }

    public void register(Timeline animation) {
        Path path = directory.resolve(animation.getName() + ".nbt");
        Timeline.save(animation, path);
        registry.put(animation.getName(), new Animation(path, animation, expiration));
    }

    private void tick() {
        long now = System.currentTimeMillis();
        for (Animation ref : registry.values()) {
            if (ref.hasExpired(now)) {
                ref.expire();
            }
        }
    }
}
