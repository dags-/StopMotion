package me.dags.animations.util.worker;

import me.dags.pitaya.util.PluginUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class WorkerTask implements Consumer<Task> {

    private final String name;
    private final Worker worker;
    private final Consumer<String> finisher;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public WorkerTask(String name, Worker worker, Consumer<String> finisher) {
        this.name = name;
        this.worker = worker;
        this.finisher = finisher;
    }

    public void cancel() {
        cancelled.set(true);
    }

    @Override
    public void accept(Task task) {
        if (cancelled.get()) {
            task.cancel();
            finisher.accept(name);
            return;
        }

        try {
            long now = System.currentTimeMillis();
            if (worker.hasWork(now)) {
                worker.work(now);
            } else {
                task.cancel();
                finisher.accept(name);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            task.cancel();
            finisher.accept(name);
        }
    }

    public void start() {
        Task.builder().intervalTicks(1).delayTicks(1).execute(this).submit(PluginUtils.getCurrentPluginInstance());
    }
}
