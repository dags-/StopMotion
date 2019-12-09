package me.dags.motion.frame;

import com.google.common.reflect.TypeToken;
import me.dags.pitaya.util.Translators;
import me.dags.pitaya.util.duration.Duration;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.schematic.Schematic;

public class FrameTranslator implements DataTranslator<Frame> {

    private static final TypeToken<Frame> TOKEN = TypeToken.of(Frame.class);
    private static final DataQuery DURATION = DataQuery.of("Duration");
    private static final DataQuery SCHEMATIC = DataQuery.of("Schematic");

    @Override
    public TypeToken<Frame> getToken() {
        return TOKEN;
    }

    @Override
    public Frame translate(DataView view) throws InvalidDataException {
        Schematic frame = Translators.get(view, SCHEMATIC, DataTranslators.SCHEMATIC);
        Duration duration = Translators.get(view, DURATION, Duration.TRANSLATOR);
        return new Frame(frame, duration);
    }

    @Override
    public DataContainer translate(Frame frame) throws InvalidDataException {
        return DataContainer.createNew()
                .set(DURATION, Duration.TRANSLATOR.translate(frame.getDuration()))
                .set(SCHEMATIC, DataTranslators.SCHEMATIC.translate(frame.getSchematic()));
    }

    @Override
    public String getId() {
        return "frame";
    }

    @Override
    public String getName() {
        return "frame";
    }
}
