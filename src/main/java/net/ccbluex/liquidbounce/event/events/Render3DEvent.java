package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.misc.Event;

public class Render3DEvent implements Event {

    private final float renderPartialTicks;

    public Render3DEvent(float renderPartialTicks) {
        this.renderPartialTicks = renderPartialTicks;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
