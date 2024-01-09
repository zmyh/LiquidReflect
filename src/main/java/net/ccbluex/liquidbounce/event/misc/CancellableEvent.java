package net.ccbluex.liquidbounce.event.misc;

public class CancellableEvent implements Event {

    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.setCancelled(true);
    }

    public void cancelEvent() {
        setCancelled(true);
    }
}
