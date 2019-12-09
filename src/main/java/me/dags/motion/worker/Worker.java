package me.dags.motion.worker;

public interface Worker {

    boolean hasWork(long now);

    void work(long now);
}
