package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.ui.client.hud.GuiHudDesigner;

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_NONE)
public class Interface extends Module {

    public Interface() {
        setState(true);
    }

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<Render2DEvent> onEvent = event -> {
        if (LiquidBounce.instance.hud == null || mc.currentScreen instanceof GuiHudDesigner)
            return;

        LiquidBounce.instance.hud.render();
    };
}
