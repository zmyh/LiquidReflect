package net.ccbluex.liquidbounce.features.module.modules.render;


import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.RotationUtils;

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @author CCBlueX
 * @game Minecraft
 */
@ModuleInfo(name = "HeadRotations", description = "Allows you to see server-sided head rotations.", category = ModuleCategory.RENDER)
public class HeadRotations extends Module {

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<Render3DEvent> onEvent = event -> {
        if (RotationUtils.serverRotation != null) {
            mc.thePlayer.rotationYawHead = RotationUtils.serverRotation.getYaw();
        }
    };
}
