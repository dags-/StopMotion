package me.dags.stopmotion.worker;

import me.dags.pitaya.util.duration.Duration;

public class DelayWorker implements Worker {

    private final Runnable task;
    private final Duration wait;

    private boolean done = false;
    private long timestamp = -1L;

    public DelayWorker(Runnable task, Duration wait) {
        this.task = task;
        this.wait = wait;
    }

    @Override
    public boolean hasWork(long now) {
        return !done;
    }

    @Override
    public void work(long now) {
        if (timestamp < 0) {
            timestamp = now + wait.getMS();
            return;
        }

        if (now < timestamp) {
            return;
        }

        try {
            task.run();
        } finally {
            done = true;
        }
    }
}
