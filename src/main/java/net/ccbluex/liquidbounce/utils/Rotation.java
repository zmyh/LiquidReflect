package net.ccbluex.liquidbounce.utils;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Rotations
 */
public class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Set rotations to [player]
     */
    public void toPlayer(EntityPlayer player) {
        if (Float.isNaN(yaw) || Float.isNaN(pitch)) return;

        player.rotationYaw = yaw;
        player.rotationPitch = pitch;
    }

    /**
     * Fix Minecraft gcd
     */
    public void fixGcd() {
        // TODO: Currently in testing

        float sensitivity = 0.91F;

        float oldYaw = yaw;
        float oldPitch = pitch;

        yaw -= yaw % sensitivity;
        pitch -= pitch % sensitivity;

        //  ChatUtil.displayChatMessage("§7sen: §8" + sensitivity + " §c- §7yaw: §8" + oldYaw + " §c-> §8" + yaw + " §c- §8pitch: §7" + oldPitch + " §c-> §8" + pitch);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}

