package net.ccbluex.liquidbounce.event.misc;

import net.ccbluex.liquidbounce.event.CallableEvent;

import java.lang.reflect.Type;

public class CallableData<T> {
    private Type type;
    private Object instance;
    private CallableEvent<T> callableEvent;
    private byte priority;

    public CallableData(Type type, Object instance, CallableEvent<T> callableEvent, byte priority) {
        this.type = type;
        this.instance = instance;
        this.callableEvent = callableEvent;
        this.priority = priority;
    }

    public Type getType() {
        return type;
    }

    public Object getInstance() {
        return instance;
    }

    public CallableEvent<T> getCallableEvent() {
        return callableEvent;
    }

    public byte getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallableData<?> that = (CallableData<?>) o;

        if (priority != that.priority) return false;
        if (!type.equals(that.type)) return false;
        if (!instance.equals(that.instance)) return false;
        return callableEvent.equals(that.callableEvent);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + instance.hashCode();
        result = 31 * result + callableEvent.hashCode();
        result = 31 * result + (int) priority;
        return result;
    }

    @Override
    public String toString() {
        return "CallableData{" +
                "type=" + type +
                ", instance=" + instance +
                ", callableEvent=" + callableEvent +
                ", priority=" + priority +
                '}';
    }
}
