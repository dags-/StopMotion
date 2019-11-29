package me.dags.animations.animation;

import com.google.common.reflect.TypeToken;
import me.dags.animations.frame.Frame;
import me.dags.pitaya.util.Translators;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;

public class AnimationTranslator implements org.spongepowered.api.data.persistence.DataTranslator<Timeline> {

    private static final TypeToken<Timeline> TOKEN = TypeToken.of(Timeline.class);
    private static final DataQuery NAME = DataQuery.of("Name");
    private static final DataQuery FRAMES = DataQuery.of("Frames");

    @Override
    public TypeToken<Timeline> getToken() {
        return TOKEN;
    }

    @Override
    public Timeline translate(DataView view) throws InvalidDataException {
        String name = Translators.getString(view, NAME);
        List<Frame> frames = Translators.getList(view, FRAMES, Frame.TRANSLATOR);
        return new Timeline(name, frames);
    }

    @Override
    public DataContainer translate(Timeline animation) throws InvalidDataException {
        return DataContainer.createNew()
                .set(NAME, animation.getName())
                .set(FRAMES, Translators.toList(animation.getFrames(), Frame.TRANSLATOR));
    }

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getName() {
        return "animation";
    }
}
