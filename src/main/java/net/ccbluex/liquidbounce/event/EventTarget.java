package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.misc.Priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EventTarget {
    byte value() default Priority.MEDIUM;
}
