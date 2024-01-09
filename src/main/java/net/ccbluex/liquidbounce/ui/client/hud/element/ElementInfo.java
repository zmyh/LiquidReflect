package net.ccbluex.liquidbounce.ui.client.hud.element;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ElementInfo {
    String name();
}
