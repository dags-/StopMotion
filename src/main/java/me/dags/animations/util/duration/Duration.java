package me.dags.animations.util.duration;

import java.util.concurrent.TimeUnit;

public class Duration {

    public final long duration;
    public final TimeUnit unit;

    public Duration(long duration, TimeUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }

    public long getDurationMs() {
        return unit.toMillis(duration);
    }

    public static Duration mils(long time) {
        return new Duration(time, TimeUnit.MILLISECONDS);
    }

    public static Duration secs(long time) {
        return new Duration(time, TimeUnit.SECONDS);
    }

    public static Duration mins(long time) {
        return new Duration(time, TimeUnit.MINUTES);
    }

    public static Duration hrs(long time) {
        return new Duration(time, TimeUnit.HOURS);
    }
}
