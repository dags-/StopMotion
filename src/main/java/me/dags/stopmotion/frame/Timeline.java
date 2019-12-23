package me.dags.stopmotion.frame;

import com.flowpowered.math.vector.Vector3i;
import me.dags.stopmotion.animation.Animation;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.util.Tuple;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Timeline {

    private final String name;
    private final List<Frame> frames;

    public Timeline(String name, List<Frame> frames) {
        this.name = name;
        this.frames = frames;
    }

    public String getName() {
        return name;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public Tuple<Vector3i, Vector3i> getBounds() {
        Vector3i min = Vector3i.ZERO;
        Vector3i max = Vector3i.ZERO;
        for (Frame frame : frames) {
            Vector3i mn = frame.getSchematic().getBlockMin();
            Vector3i mx = frame.getSchematic().getBlockMax();

            if (min == Vector3i.ZERO) {
                min = mn;
            } else {
                min = min.min(mn);
            }

            if (max == Vector3i.ZERO) {
                max = mx;
            } else {
                max = max.max(mx);
            }
        }
        return Tuple.of(min, max);
    }

    public static Optional<Timeline> load(Path path) {
        try (InputStream stream = new BufferedInputStream(new GZIPInputStream(Files.newInputStream(path)))) {
            DataView dataView = DataFormats.NBT.readFrom(stream);
            Timeline timeline = Animation.TRANSLATOR.translate(dataView);
            return Optional.of(timeline);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return Optional.empty();
    }

    public static void save(Timeline animation, Path path) {
        try (OutputStream stream = new BufferedOutputStream(new GZIPOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE)))) {
            DataView dataView = Animation.TRANSLATOR.translate(animation);
            DataFormats.NBT.writeTo(stream, dataView);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
