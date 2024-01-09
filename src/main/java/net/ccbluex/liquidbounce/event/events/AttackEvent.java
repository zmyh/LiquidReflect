package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.misc.Event;
import net.minecraft.entity.Entity;

public class AttackEvent implements Event {
    private final Entity entity;

    public AttackEvent(Entity target) {
        this.entity = target;
    }

    public Entity getEntity() {
        return entity;
    }
}
