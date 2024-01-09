package net.ccbluex.liquidbounce.event;


import net.ccbluex.liquidbounce.event.misc.CallableData;
import net.ccbluex.liquidbounce.event.misc.Event;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {

    private final List<CallableData<Event>> callableData = new CopyOnWriteArrayList<>();

    public void register(Object instance) {
        for (Field declaredField : instance.getClass().getDeclaredFields()) {
            if (!declaredField.isAnnotationPresent(EventTarget.class)) continue;
            declaredField.setAccessible(true);
            final Type type = ((ParameterizedType) declaredField.getGenericType()).getActualTypeArguments()[0];
            try {
                //noinspection unchecked
                final CallableEvent<Event> callableEvent = (CallableEvent<Event>) declaredField.get(instance);
                final byte priority = declaredField.getAnnotation(EventTarget.class).value();
                callableData.add(new CallableData<>(type, instance, callableEvent, priority));
                callableData.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregister(Object instance) {
        callableData.removeIf(data -> data.getInstance().equals(instance));
    }

    public void call(Event event) {
        for (CallableData<Event> callableDatum : callableData) {
            try {
                if (callableDatum.getType() == event.getClass() || callableDatum.getType() == event.getClass().getSuperclass()) {
                    try {
                        callableDatum.getCallableEvent().call(event);
                    } catch (Throwable ignored) {

                    }
                }
            } catch (Exception e) {
                throw new NullPointerException("DEOBF?");
            }
        }
    }
}
