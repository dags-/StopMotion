import me.dags.animations.util.iterator.Direction;
import me.dags.animations.worker.*;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.*;
import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) throws Throwable {
        List<Timed> items = Arrays.asList(
                new Obj("one", 1000),
                new Obj("two", 500),
                new Obj("three", 500),
                new Obj("four", 1000)
        );

        Thread.sleep(2000L);

        Tsk task = new Tsk();
        List<Direction> directions = Arrays.asList(Direction.FORWARD, Direction.BACKWARD);
        WorkerTask worker = new WorkerTask(forwards(items, directions), () -> System.out.println("\nDone!"));
        while (task.running) {
            worker.accept(task);
        }
    }

    private static  <T extends Timed> Worker forwards(List<T> tasks, List<Direction> directions) {
        List<Worker> workers = new LinkedList<>();
        for (Direction direction : directions) {
            Iterator<T> iterator = direction.iterate(tasks);
            workers.add(new TestWorker<>(iterator));
        }
        return new QueueWorker(workers);
    }

    private static  <T extends Timed> Worker backwards(List<T> items, List<Direction> directions) {
        List<Worker> workers = new LinkedList<>();
        for (int i = directions.size() - 1; i >= 0; i--) {
            Direction direction = directions.get(i);
            Iterator<T> iterator = direction.iterate(items);
            workers.add(new TestWorker<>(iterator));
        }
        return new QueueWorker(workers);
    }

    private static class Obj implements Timed {

        private final Object value;
        private final long duration;

        private Obj(Object value, long duration) {
            this.value = value;
            this.duration = duration;
        }

        @Override
        public long getDurationMS() {
            return duration;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private static class TestWorker<T extends Timed> extends IteraterWorker<T> {

        private TestWorker(Iterator<T> iterator) {
            super(iterator);
        }

        @Override
        protected void apply(T t) {
            System.out.println("KeyFrame: " + t.toString());
        }

        @Override
        protected void applyTransient(T t) {
            System.out.println("Frame:    " + t.toString());
        }
    }

    private static class Tsk implements Task {

        private boolean running = true;

        @Override
        public String getName() {
            return null;
        }

        @Override
        public PluginContainer getOwner() {
            return null;
        }

        @Override
        public long getDelay() {
            return 0;
        }

        @Override
        public long getInterval() {
            return 0;
        }

        @Override
        public boolean cancel() {
            System.out.println("Stopping");
            running = false;
            return false;
        }

        @Override
        public Consumer<Task> getConsumer() {
            return null;
        }

        @Override
        public boolean isAsynchronous() {
            return false;
        }

        @Override
        public UUID getUniqueId() {
            return null;
        }
    }
}
