package me.dags.animations;

import me.dags.animations.instance.Instance;
import me.dags.animations.worker.WorkerTask;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PlaybackManager {

    private final Animations plugin;
    private final Map<String, WorkerTask> workers = new HashMap<>();

    public PlaybackManager(Animations plugin) {
        this.plugin = plugin;
    }

    public synchronized void submit(Instance instance) {
        final String taskId = instance.getId();
        if (!instance.isLocked() && !workers.containsKey(taskId)) {
            // lock the instance from further invocations until it has either failed or successfully loaded
            instance.setLocked(true);

            instance.getWorker().handle((t, e) -> {
                e.printStackTrace();
                instance.setLocked(false);
            }).run(worker -> {
                instance.setLocked(false);
                WorkerTask task = new WorkerTask(worker, () -> remove(taskId));
                workers.put(instance.getId(), task);
                start(task);
            });
        }
    }

    public synchronized void cancel(String id) {
        WorkerTask task = workers.get(id);
        if (task != null) {
            task.cancel();
        }
    }

    public synchronized void cancelAll() {
        List<WorkerTask> tasks = new LinkedList<>(workers.values());
        tasks.forEach(WorkerTask::cancel);
    }

    private synchronized void remove(String taskId) {
        // remove from the worker
        workers.remove(taskId);

        // re-registering the instance will trigger a write to file, which will save the
        // instance's state (needed for push-pull animations)
        plugin.getAnimations().getById(taskId).ifPresent(plugin.getAnimations()::register);
    }

    private void start(Consumer<Task> task) {
        Task.builder().delayTicks(1).intervalTicks(1).execute(task).submit(plugin);
    }
}
