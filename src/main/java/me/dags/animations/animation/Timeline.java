package me.dags.animations.animation;

import me.dags.animations.frame.Frame;
import me.dags.animations.util.Translators;
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

    public static Optional<Timeline> load(Path path) {
        try (InputStream stream = new BufferedInputStream(new GZIPInputStream(Files.newInputStream(path)))) {
            DataView dataView = DataFormats.NBT.readFrom(stream);
            Timeline timeline = Translators.ANIMATION.translate(dataView);
            return Optional.of(timeline);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return Optional.empty();
    }

    public static void save(Timeline animation, Path path) {
        try (OutputStream stream = new BufferedOutputStream(new GZIPOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE)))) {
            DataView dataView = Translators.ANIMATION.translate(animation);
            DataFormats.NBT.writeTo(stream, dataView);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
