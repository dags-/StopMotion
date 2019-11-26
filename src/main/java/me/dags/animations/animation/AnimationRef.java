package me.dags.animations.animation;

import org.spongepowered.api.CatalogType;

import java.nio.file.Path;
import java.util.Optional;

public class AnimationRef implements CatalogType {

    private final Path path;
    private final String name;

    private long timeout = 0L;
    private Animation reference = null;

    public AnimationRef(Path path, String name) {
        this.path = path;
        this.name = name;
    }

    public AnimationRef(Path path, Animation animation) {
        this.path = path;
        this.name = animation.getId();
        this.reference = animation;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    public Optional<Animation> getAnimation() {
        if (reference == null) {
            Optional<Animation> animation = Animation.load(path);
            animation.ifPresent(a -> reference = a);
            return animation;
        }
        return Optional.of(reference);
    }
}
