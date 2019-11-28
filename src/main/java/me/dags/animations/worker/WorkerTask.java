package me.dags.animations.worker;

import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class WorkerTask implements Consumer<Task> {

    private final Worker worker;
    private final Runnable callback;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public WorkerTask(Worker worker, Runnable callback) {
        this.worker = worker;
        this.callback = callback;
    }

    @Override
    public void accept(Task task) {
        if (cancelled.get()) {
            dispose(task);
            return;
        }

        try {
            long now = System.currentTimeMillis();
            if (worker.hasWork(now)) {
                worker.work(now);
            } else {
                dispose(task);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            dispose(task);
        }
    }

    public void cancel() {
        cancelled.set(true);
    }

    private void dispose(Task task) {
        task.cancel();
        callback.run();
    }
}
