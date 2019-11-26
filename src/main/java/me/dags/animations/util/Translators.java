package me.dags.animations.util;

import com.flowpowered.math.vector.Vector3i;
import me.dags.animations.animation.AnimationTranslator;
import me.dags.animations.frame.FrameTranslator;
import me.dags.animations.util.duration.DurationTranslator;
import me.dags.pitaya.config.Node;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Translators {

    public static final FrameTranslator FRAME = new FrameTranslator();
    public static final DurationTranslator DURATION = new DurationTranslator();
    public static final AnimationTranslator ANIMATION = new AnimationTranslator();
    public static final DataTranslator<Schematic> SCHEMATIC = DataTranslators.SCHEMATIC;

    public static Vector3i vec3i(Node node) {
        int x = node.get("x", 0);
        int y = node.get("y", 0);
        int z = node.get("z", 0);
        return new Vector3i(x, y, z);
    }

    public static Node vec3i(Vector3i vec) {
        Node node = Node.create();
        node.set("x", vec.getX());
        node.set("y", vec.getY());
        node.set("z", vec.getZ());
        return node;
    }

    public static String getString(DataView view, DataQuery path) throws InvalidDataException {
        return view.getString(path).orElseThrow(() -> err(path));
    }

    public static long getLong(DataView view, DataQuery path) throws InvalidDataException {
        return view.getLong(path).orElseThrow(() -> err(path));
    }

    public static <T extends Enum<T>> T getEnum(DataView view, DataQuery path, Class<T> type) throws InvalidDataException {
        String value = getString(view, path);
        return Enum.valueOf(type, value);
    }

    public static <T> T get(DataView view, DataQuery path, DataTranslator<T> translator) throws InvalidDataException {
        DataView child = view.getView(path).orElseThrow(() -> err(path));
        return translator.translate(child);
    }

    public static <T> List<T> getList(DataView view, DataQuery path, DataTranslator<T> translator) throws InvalidDataException {
        List<DataView> data = view.getViewList(path).orElseThrow(() -> err(path));
        List<T> list = new ArrayList<>(data.size());
        for (DataView dataView : data) {
            list.add(translator.translate(dataView));
        }
        return list;
    }

    public static <T> List<DataContainer> toList(List<T> list, DataTranslator<T> translator) {
        List<DataContainer> data = new LinkedList<>();
        for (T t : list) {
            data.add(translator.translate(t));
        }
        return data;
    }

    public static InvalidDataException err(DataQuery query) {
        return new InvalidDataException("Missing data: " + query);
    }
}
