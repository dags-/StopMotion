package me.dags.animations.util.region;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3i;

import java.util.*;
import java.util.function.Consumer;

public class RegionMap<T extends Positioned> {

    private final int size;
    private final List<T> empty = Collections.emptyList();
    private final Map<Long, List<T>> map = new HashMap<>();

    public RegionMap(int regionSize) {
        this.size = regionSize;
    }

    public void add(T t, int radius) {
        int minX = radius - t.getPosition().getX() >> size;
        int minZ = radius - t.getPosition().getZ() >> size;
        int maxX = radius + t.getPosition().getX() >> size;
        int maxZ = radius + t.getPosition().getZ() >> size;
        for (int rz = minZ; rz <= maxZ; rz++) {
            for (int rx = minX; rx <= maxX; rx++) {
                long id = getId(rx, rz);
                map.computeIfAbsent(id, l -> new LinkedList<>()).add(t);
            }
        }
    }

    public void visit(Vector3i position, int radius, Consumer<T> consumer) {
        visit(position.getX(), position.getZ(), radius, consumer);
    }

    public void visit(Vector2i position, int radius, Consumer<T> consumer) {
        visit(position.getX(), position.getY(), radius, consumer);
    }

    public void visit(int x, int z, int radius, Consumer<T> consumer) {
        int regionX = x >> size;
        int regionZ = z >> size;
        int minX = regionX - radius;
        int minZ = regionZ - radius;
        int maxX = regionX + radius;
        int maxZ = regionZ + radius;
        Set<T> visited = new HashSet<>();
        for (int rz = minZ; rz <= maxZ; rz++) {
            for (int rx = minX; rx <= maxX; rx++) {
                long id = getId(rx, rz);
                List<T> list = map.getOrDefault(id, empty);
                for (T t : list) {
                    if (visited.add(t)) {
                        consumer.accept(t);
                    }
                }
            }
        }
    }

    public List<T> get(Vector3i position) {
        return get(position.getX(), position.getZ());
    }

    public List<T> get(Vector2i position) {
        return get(position.getX(), position.getY());
    }

    public List<T> get(int x, int z) {
        int regionX = x >> size;
        int regionZ = z >> size;
        long id = getId(regionX, regionZ);
        return map.getOrDefault(id, empty);
    }

    private static long getId(int x, int z) {
        return 0L;
    }
}
