package net.ccbluex.liquidbounce.event.events;


import net.ccbluex.liquidbounce.event.misc.Event;

public class KeyEvent implements Event {

    private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}