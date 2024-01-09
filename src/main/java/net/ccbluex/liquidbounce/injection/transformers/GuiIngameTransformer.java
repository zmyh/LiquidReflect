package net.ccbluex.liquidbounce.injection.transformers;

import net.minecraft.client.gui.GuiIngame;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.Render2DEvent;
import net.ccbluex.liquidbounce.injection.Transformer;

public class GuiIngameTransformer extends Transformer {
    @Override
    public Class<?> getTransformTarget() {
        return GuiIngame.class;
    }

    @Override
    public void transform(ClassNode classNode) {
        // renderTooltip
        MethodNode renderTooltip = method(classNode, "renderTooltip", "(Lnet/minecraft/client/gui/ScaledResolution;F)V");
        renderTooltip.instructions.insertBefore(renderTooltip.instructions.getLast().getPrevious(), new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "render2D", "()V"));
        // end renderTooltip
    }

    public static void render2D() {
        if (!LiquidBounce.init) {
            LiquidBounce.instance.startClient();
        } else {
            LiquidBounce.instance.eventManager.call(new Render2DEvent());
        }
    }
}
