package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.utils.block.PlaceInfo;

/**
 * Rotation with place info
 */
public class PlaceRotation {
    private PlaceInfo placeInfo;
    private Rotation rotation;

    public PlaceRotation(PlaceInfo placeInfo, Rotation rotation) {
        this.placeInfo = placeInfo;
        this.rotation = rotation;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public PlaceInfo getPlaceInfo() {
        return placeInfo;
    }
}
