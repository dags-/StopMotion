package me.dags.animations;

import me.dags.animations.instance.Instance;
import me.dags.animations.util.worker.Worker;
import me.dags.animations.util.worker.WorkerTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlaybackManager {

    private final Map<String, WorkerTask> playback = new HashMap<>();

    public synchronized void cancel(String id) {
        WorkerTask task = playback.get(id);
        if (task != null) {
            task.cancel();
        }
    }

    public synchronized void cancelAll() {
        List<WorkerTask> tasks = new LinkedList<>(playback.values());
        tasks.forEach(WorkerTask::cancel);
    }

    public synchronized void play(Instance instance) {
        if (!playback.containsKey(instance.getId())) {
            instance.getWorker().ifPresent(worker -> play(instance.getName(), worker));
        }
    }

    public synchronized void play(String name, Worker worker) {
        if (!playback.containsKey(name)) {
            WorkerTask task = new WorkerTask(name, worker, playback::remove);
            playback.put(name, task);
            task.start();
        }
    }
}
