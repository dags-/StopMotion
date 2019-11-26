package me.dags.animations.animation;

import me.dags.animations.frame.Frame;
import me.dags.animations.util.Translators;
import me.dags.animations.util.iterator.Direction;
import me.dags.animations.util.iterator.ForwardIterator;
import me.dags.animations.util.iterator.Iterator;
import me.dags.animations.util.worker.FrameWorker;
import me.dags.animations.util.worker.QueueWorker;
import me.dags.animations.util.worker.Worker;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Animation implements CatalogType {

    private final String name;
    private final List<Frame> frames;

    public Animation(String name, List<Frame> frames) {
        this.name = name;
        this.frames = new ArrayList<>(frames);
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public Worker getTimeline(Location<World> origin, List<Direction> timeline) {
        List<Worker> workers = new LinkedList<>();
        Iterator<Frame> iterator = new ForwardIterator<>(frames);
        for (Direction direction : timeline) {
            FrameWorker worker = new FrameWorker(origin, direction.wrap(iterator));
            workers.add(worker);
        }
        return new QueueWorker(workers);
    }

    public static Optional<Animation> load(Path path) {
        try (InputStream stream = new BufferedInputStream(new GZIPInputStream(Files.newInputStream(path)))) {
            DataView dataView = DataFormats.NBT.readFrom(stream);
            return Optional.of(Translators.ANIMATION.translate(dataView));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void save(Animation animation, Path path) {
        try (OutputStream stream = new BufferedOutputStream(new GZIPOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE)))) {
            DataView dataView = Translators.ANIMATION.translate(animation);
            DataFormats.NBT.writeTo(stream, dataView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
