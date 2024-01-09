package net.ccbluex.liquidbounce.event;

@FunctionalInterface
public interface CallableEvent<T> {
    void call(T event);
}
