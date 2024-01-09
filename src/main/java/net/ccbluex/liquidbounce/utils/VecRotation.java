package net.ccbluex.liquidbounce.utils;

import net.minecraft.util.Vec3;

/**
 * Rotation with vector
 */
public class VecRotation {
    private Vec3 vec;
    private Rotation rotation;

    public VecRotation(Vec3 vec, Rotation rotation) {
        this.vec = vec;
        this.rotation = rotation;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Vec3 getVec() {
        return vec;
    }
}
