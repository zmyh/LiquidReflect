package net.ccbluex.liquidbounce.injection.transformers;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.injection.Transformer;

public class EntityRendererTransformer extends Transformer {
    @Override
    public Class<?> getTransformTarget() {
        return EntityRenderer.class;
    }

    @Override
    public void transform(ClassNode classNode) {
        MethodNode node = method(classNode, "renderWorldPass", "(IFJ)V");
        InsnList insnList = new InsnList();
        insnList.add(new TypeInsnNode(NEW, Type.getInternalName(Render3DEvent.class)));
        insnList.add(new InsnNode(DUP));
        insnList.add(new VarInsnNode(FLOAD, 2));
        insnList.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(Render3DEvent.class), "<init>", "(F)V"));
        insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "renderWorldPass", "(" + getClassSignature(Render3DEvent.class) + ")V"));

        for (AbstractInsnNode abstractInsnNode : node.instructions) {
            if (abstractInsnNode instanceof MethodInsnNode) {
                MethodInsnNode tmp = (MethodInsnNode) abstractInsnNode;
                if (tmp.name.equals("disableFog")) {
                    node.instructions.insert(abstractInsnNode, insnList);
                }
            }
        }
    }

    public static void renderWorldPass(Render3DEvent event) {
        GlStateManager.pushMatrix();
        LiquidBounce.instance.eventManager.call(event);
        GlStateManager.popMatrix();
    }

}
