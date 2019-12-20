package me.dags.stopmotion.frame;

import com.google.common.reflect.TypeToken;
import me.dags.pitaya.schematic.PitSchematic;
import me.dags.pitaya.schematic.SchemUtils;
import me.dags.pitaya.util.Translators;
import me.dags.pitaya.util.duration.Duration;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.InvalidDataException;

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
        DataView data = view.getView(SCHEMATIC).orElseThrow(() -> new InvalidDataException("Missing schematic"));
        PitSchematic frame = SchemUtils.translate(data);
        Duration duration = Translators.get(view, DURATION, Duration.TRANSLATOR);
        return new Frame(frame, duration);
    }

    @Override
    public DataContainer translate(Frame frame) throws InvalidDataException {
        return DataContainer.createNew()
                .set(SCHEMATIC, SchemUtils.translate(frame.getSchematic()))
                .set(DURATION, Duration.TRANSLATOR.translate(frame.getDuration()));
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
