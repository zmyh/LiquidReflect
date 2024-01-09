package net.ccbluex.liquidbounce.injection.transformers;

import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.TickEvent;
import net.ccbluex.liquidbounce.injection.Transformer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class MinecraftTransformer extends Transformer {
    private static long lastFrame = getTime();


    @Override
    public Class<?> getTransformTarget() {
        return Minecraft.class;
    }

    @Override
    public void transform(ClassNode classNode) {
        // runGameLoop
        MethodNode runGameLoop = method(classNode, "runGameLoop", "()V");
        runGameLoop.instructions.insert(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "runGameLoop", "()V"));
        // end runGameLoop

        // runTick
        MethodNode runTick = method(classNode, "runTick", "()V");
        runTick.instructions.insert(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "onTick", "()V"));
        // runTick
    }

    public static void onTick() {
        LiquidBounce.instance.eventManager.call(new TickEvent());
    }

    public static void runGameLoop() {
        final long currentTime = getTime();
        final int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;

        RenderUtils.deltaTime = deltaTime;
    }

    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

}
