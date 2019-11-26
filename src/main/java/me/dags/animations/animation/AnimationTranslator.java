package me.dags.animations.animation;

import com.google.common.reflect.TypeToken;
import me.dags.animations.frame.Frame;
import me.dags.animations.util.Translators;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;

public class AnimationTranslator implements org.spongepowered.api.data.persistence.DataTranslator<Animation> {

    private static final TypeToken<Animation> TOKEN = TypeToken.of(Animation.class);
    private static final DataQuery NAME = DataQuery.of("Name");
    private static final DataQuery FRAMES = DataQuery.of("Frames");

    @Override
    public TypeToken<Animation> getToken() {
        return TOKEN;
    }

    @Override
    public Animation translate(DataView view) throws InvalidDataException {
        String name = Translators.getString(view, NAME);
        List<Frame> frames = Translators.getList(view, FRAMES, Translators.FRAME);
        return new Animation(name, frames);
    }

    @Override
    public DataContainer translate(Animation animation) throws InvalidDataException {
        return DataContainer.createNew()
                .set(NAME, animation.getName())
                .set(FRAMES, Translators.toList(animation.getFrames(), Translators.FRAME));
    }

    @Override
    public String getId() {
        return "animation";
    }

    @Override
    public String getName() {
        return "animation";
    }
}
