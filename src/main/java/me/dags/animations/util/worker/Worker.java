package me.dags.animations.util.worker;

public interface Worker {

    boolean hasWork(long now);

    void work(long now);
}
