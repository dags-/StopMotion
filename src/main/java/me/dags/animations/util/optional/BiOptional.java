package me.dags.animations.util.optional;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BiOptional<A, B> {

    private final Optional<A> a;
    private final Optional<B> b;

    public BiOptional(Optional<A> a, Optional<B> b) {
        this.a = a;
        this.b = b;
    }

    public boolean isPresent() {
        return a.isPresent() && b.isPresent();
    }

    public void ifPresent(BiConsumer<A, B> consumer) {
        if (a.isPresent() && b.isPresent()) {
            consumer.accept(a.get(), b.get());
        }
    }

    public <T> BiOptional<A, B> filter(BiPredicate<A, B> filter) {
        if (a.isPresent() && b.isPresent() && filter.test(a.get(), b.get())) {
            return this;
        }
        return BiOptional.empty();
    }

    public <T> Optional<T> map(BiFunction<A, B, T> mapper) {
        if (a.isPresent() && b.isPresent()) {
            return Optional.of(mapper.apply(a.get(), b.get()));
        }
        return Optional.empty();
    }

    public static <A, B> BiOptional<A, B> of(Optional<A> a, Optional<B> b) {
        return new BiOptional<>(a, b);
    }

    public static <A, B> BiOptional<A, B> empty() {
        return new BiOptional<>(Optional.empty(), Optional.empty());
    }
}
