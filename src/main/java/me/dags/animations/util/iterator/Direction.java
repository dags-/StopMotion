package me.dags.animations.util.iterator;

import me.dags.pitaya.config.Node;

import java.util.List;

public enum Direction {
    FORWARD {
        @Override
        public <T> Iterator<T> iterate(List<T> list) {
            return new ForwardIterator<>(list);
        }
    },
    BACKWARD {
        @Override
        public <T> Iterator<T> iterate(List<T> list) {
            return new BackwardIterator<>(list);
        }
    }
    ;

    public abstract <T> Iterator<T> iterate(List<T> list);

    public static Direction deserialize(Node node) {
        return Direction.valueOf(node.get(""));
    }
}
