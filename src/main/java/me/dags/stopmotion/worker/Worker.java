package me.dags.stopmotion.worker;

public interface Worker {

    boolean hasWork(long now);

    void work(long now);
}
