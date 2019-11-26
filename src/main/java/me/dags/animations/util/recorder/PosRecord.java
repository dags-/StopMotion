package me.dags.animations.util.recorder;

import com.flowpowered.math.vector.Vector3i;

public class PosRecord {

    public String world = "";
    public Vector3i pos1 = Vector3i.ZERO;
    public Vector3i pos2 = Vector3i.ZERO;

    public void reset() {
        pos1 = Vector3i.ZERO;
        pos2 = Vector3i.ZERO;
        world = "";
    }
}
