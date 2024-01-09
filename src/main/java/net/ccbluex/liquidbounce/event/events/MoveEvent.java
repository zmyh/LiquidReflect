package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.misc.CancellableEvent;

public class MoveEvent extends CancellableEvent {

    private double x, y, z;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void zero() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }

    public void zeroXZ() {
        this.x = 0;
        this.z = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
