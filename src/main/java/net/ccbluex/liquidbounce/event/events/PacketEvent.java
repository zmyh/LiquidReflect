package net.ccbluex.liquidbounce.event.events;

import net.ccbluex.liquidbounce.event.misc.CancellableEvent;
import net.minecraft.network.Packet;


public class PacketEvent extends CancellableEvent {

    private final Packet<?> packet;
    private final boolean out;

    public PacketEvent(Packet<?> packet, boolean out) {
        this.packet = packet;
        this.out = out;
    }

    public boolean isOutgoing() {
        return out;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}