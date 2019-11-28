package me.dags.animations.worker;

public interface Worker {

    boolean hasWork(long now);

    void work(long now);
}
