package me.dags.animations.util.iterator;

import me.dags.pitaya.config.Node;

public enum Direction {
    FORWARD,
    BACKWARD {
        @Override
        public <T> Iterator<T> wrap(Iterator<T> timeline) {
            return timeline.reverse();
        }
    }
    ;

    public <T> Iterator<T> wrap(Iterator<T> timeline) {
        return timeline;
    }

    public static Direction deserialize(Node node) {
        return Direction.valueOf(node.get(""));
    }
}
