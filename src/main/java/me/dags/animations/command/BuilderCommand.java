package me.dags.animations.command;

import me.dags.pitaya.util.cache.IdCache;
import org.spongepowered.api.util.Identifiable;

import java.util.Collections;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BuilderCommand<T> {

    private final IdCache<T> cache;
    private final Function<UUID, T> func;

    protected BuilderCommand(long timeout, TimeUnit unit, Function<UUID, T> func) {
        this.cache = new IdCache<T>(timeout, unit);
        this.func = func;
    }

    protected BuilderCommand(long timeout, TimeUnit unit, Supplier<T> supplier) {
        this(timeout, unit, uuid -> supplier.get());
    }

    protected T must(Identifiable subject) {
        return must(subject.getUniqueId());
    }

    protected T must(UUID id) {
        return cache.compute(id, func);
    }

    protected void drain(Identifiable identifiable, Consumer<T> consumer) {
        cache.get(identifiable).ifPresent(consumer);
        cache.remove(identifiable);
    }

    protected void remove(Identifiable subject) {
        cache.remove(subject);
    }
}
