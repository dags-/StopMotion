package me.dags.animations.entity;

import com.flowpowered.math.vector.Vector3d;
import me.dags.animations.trigger.rule.Time;
import org.spongepowered.api.entity.EntityArchetype;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EntityInstanceBuilder {

    public Time rule;
    public String name;
    public String link;
    public String world;
    public Vector3d origin;
    public List<UUID> state = Collections.emptyList();
    public List<EntityArchetype> entities = Collections.emptyList();
}
