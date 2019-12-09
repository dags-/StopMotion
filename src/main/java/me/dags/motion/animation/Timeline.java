package me.dags.motion.animation;

import com.flowpowered.math.vector.Vector3i;
import me.dags.motion.frame.Frame;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataFormats;

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

    public Optional<Vector3i> getSize() {
        Vector3i min = null;
        Vector3i max = null;
        for (Frame frame : getFrames()) {
            Vector3i fmin = frame.getSchematic().getBlockMin();
            Vector3i fmax = frame.getSchematic().getBlockMax();
            if (min == null) {
                min = fmin;
            } else {
                min = min.min(fmin);
            }
            if (max == null) {
                max = fmax;
            } else {
                max = max.max(fmax);
            }
        }
        if (min == null || max == null) {
            return Optional.empty();
        }
        return Optional.of(max.sub(min));
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
