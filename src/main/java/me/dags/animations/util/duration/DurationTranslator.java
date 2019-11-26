package me.dags.animations.util.duration;

import com.google.common.reflect.TypeToken;
import me.dags.animations.util.Translators;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.concurrent.TimeUnit;

public class DurationTranslator implements DataTranslator<Duration> {

    private static final TypeToken<Duration> TOKEN = TypeToken.of(Duration.class);
    private static final DataQuery TIME = DataQuery.of("duration");
    private static final DataQuery UNIT = DataQuery.of("unit");

    @Override
    public TypeToken<Duration> getToken() {
        return TOKEN;
    }

    @Override
    public Duration translate(DataView view) throws InvalidDataException {
        long duration = Translators.getLong(view, TIME);
        TimeUnit unit = Translators.getEnum(view, UNIT, TimeUnit.class);
        return new Duration(duration, unit);
    }

    @Override
    public DataContainer translate(Duration duration) throws InvalidDataException {
        return DataContainer.createNew()
                .set(TIME, duration.duration)
                .set(UNIT, duration.unit.toString());
    }

    @Override
    public String getId() {
        return "duration";
    }

    @Override
    public String getName() {
        return "duration";
    }
}
