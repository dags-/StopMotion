package me.dags.stopmotion.animation;

import me.dags.stopmotion.StopMotion;
import me.dags.stopmotion.frame.Timeline;
import me.dags.pitaya.task.Promise;
import me.dags.pitaya.util.duration.Duration;
import org.spongepowered.api.CatalogType;

import java.nio.file.Path;
import java.util.concurrent.Callable;

public class Animation implements CatalogType {

    public static final AnimationTranslator TRANSLATOR = new AnimationTranslator();

    private final Path path;
    private final String name;
    private final Duration duration;
    private final Object lock = new Object();

    private long timeout = 0L;
    private Timeline reference = null;

    public Animation(Path path, String name, Duration duration) {
        this.path = path;
        this.name = name;
        this.duration = duration;
    }

    public Animation(Path path, Timeline timeline, Duration duration) {
        this.path = path;
        this.duration = duration;
        this.reference = timeline;
        this.name = timeline.getName();
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
        synchronized (lock) {
            timeout = System.currentTimeMillis() + duration.getMS();
        }
    }

    public void expire() {
        synchronized (lock) {
            reference = null;
        }
    }

    public boolean hasExpired(long now) {
        return now > timeout;
    }

    public Promise<Timeline> getTimeline() {
        refresh();
        return Promise.of(getOrLoad());
    }

    private Callable<Timeline> getOrLoad() {
        return () -> {
            synchronized (lock) {
                if (reference == null) {
                    StopMotion.debug("Loading: {}, Thread: {}", path, Thread.currentThread());
                    reference = Timeline.load(path).orElseThrow(() -> new AnimationException("Failed to load " + path));
                }
                return reference;
            }
        };
    }
}
