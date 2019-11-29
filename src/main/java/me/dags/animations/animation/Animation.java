package me.dags.animations.animation;

import me.dags.pitaya.util.duration.Duration;
import org.spongepowered.api.CatalogType;

import java.nio.file.Path;
import java.util.Optional;

public class Animation implements CatalogType {

    public static final AnimationTranslator TRANSLATOR = new AnimationTranslator();

    private final Path path;
    private final String name;
    private final Duration duration;

    private long timeout = 0L;
    private Timeline reference = null;

    public Animation(Path path, String name, Duration duration) {
        this.path = path;
        this.name = name;
        this.duration = duration;
    }

    public Animation(Path path, Timeline animation, Duration duration) {
        this.path = path;
        this.duration = duration;
        this.reference = animation;
        this.name = animation.getName();
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }

    public void refresh() {
        timeout = System.currentTimeMillis() + duration.getMS();
    }

    public void expire() {
        reference = null;
    }

    public boolean hasExpired(long now) {
        return now > timeout;
    }

    public Optional<Timeline> getTimeline() {
        refresh();
        if (reference == null) {
            Optional<Timeline> animation = Timeline.load(path);
            animation.ifPresent(a -> reference = a);
            return animation;
        }
        return Optional.of(reference);
    }
}
