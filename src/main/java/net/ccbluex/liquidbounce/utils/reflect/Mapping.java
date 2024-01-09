package net.ccbluex.liquidbounce.utils.reflect;

import net.ccbluex.liquidbounce.utils.reflect.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;

public class Mapping {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static double renderPosX = (double) ReflectionUtils.getFieldValue(mc.getRenderManager(), "renderPosX", "field_78725_b", "o");
    public static double renderPosY = (double) ReflectionUtils.getFieldValue(mc.getRenderManager(), "renderPosY", "field_78726_c", "p");
    public static double renderPosZ = (double) ReflectionUtils.getFieldValue(mc.getRenderManager(), "renderPosZ", "field_78728_n", "q");
    public static Timer timer = (Timer) ReflectionUtils.getFieldValue(mc, "timer", "field_71428_T", "Y");

    public static boolean getPressed(KeyBinding key) {
        return (boolean) ReflectionUtils.getFieldValue(key, "pressed", "field_74513_e", "h");
    }

    public static void setPressed(KeyBinding key, boolean pressed) {
        ReflectionUtils.setFieldValue(key, pressed, "pressed", "field_74513_e", "h");
    }

    public static void setSession(Session sb) {
        ReflectionUtils.setFieldValue(mc, sb, "session", "field_71449_j", "ae");
    }
    //

    public static void setItemInUseCount(int sb) {
        ReflectionUtils.setFieldValue(mc.thePlayer, sb, "itemInUseCount", "field_71072_f", "f");
    }

    public static void setGround(C03PacketPlayer sb, boolean sb1) {
        ReflectionUtils.setFieldValue(sb, sb1, "onGround", "field_149474_g", "g");
    }

    public static void setY(C03PacketPlayer sb, double sb1) {
        ReflectionUtils.setFieldValue(sb, sb1, "y", "field_149477_b", "b");
    }

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<Render3DEvent> onEvent = event -> {
        renderPosX = (double) ReflectionUtils.getFieldValue(mc.getRenderManager(), "renderPosX", "field_78725_b", "o");
        renderPosY = (double) ReflectionUtils.getFieldValue(mc.getRenderManager(), "renderPosY", "field_78726_c", "p");
        renderPosZ = (double) ReflectionUtils.getFieldValue(mc.getRenderManager(), "renderPosZ", "field_78728_n", "q");
        //currentPlayerItem = (int) ReflectionUtil.getFieldValue(mc.playerController,"currentPlayerItem","field_78777_l","l");
    };
}
