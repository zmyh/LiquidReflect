package net.ccbluex.liquidbounce.event.events;


import net.ccbluex.liquidbounce.event.misc.Event;

public class MotionUpdateEvent implements Event {

    private State state;

    public MotionUpdateEvent(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public enum State {
        Pre, Post;
    }
}