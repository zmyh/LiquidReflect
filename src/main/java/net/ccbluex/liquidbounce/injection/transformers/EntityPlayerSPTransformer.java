package net.ccbluex.liquidbounce.injection.transformers;

import net.minecraft.client.entity.EntityPlayerSP;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.MotionUpdateEvent;
import net.ccbluex.liquidbounce.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.injection.Transformer;

public class EntityPlayerSPTransformer extends Transformer {
    @Override
    public Class<?> getTransformTarget() {
        return EntityPlayerSP.class;
    }

    @Override
    public void transform(ClassNode classNode) {
        // onUpdateWalkingPlayer
        MethodNode onUpdateWalkingPlayer = method(classNode, "onUpdateWalkingPlayer", "()V");
        onUpdateWalkingPlayer.instructions.insert(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "preUpdate", "()V"));
        onUpdateWalkingPlayer.instructions.insertBefore(onUpdateWalkingPlayer.instructions.getLast().getPrevious(), new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "postUpdate", "()V"));
        // end onUpdateWalkingPlayer

        // onUpdate
        MethodNode onUpdate = method(classNode, "onUpdate", "()V");
        for (AbstractInsnNode instruction : onUpdate.instructions) {
            if (!(instruction instanceof MethodInsnNode)) {
                continue;
            }
            MethodInsnNode min = (MethodInsnNode) instruction;
            if (min.name.equals("onUpdate")) {
                onUpdate.instructions.insertBefore(min.getPrevious(), new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "onUpdate", "()V", false));
            }
        }
        // end onUpdate
    }

    public static void onUpdate() {
        LiquidBounce.instance.eventManager.call(new UpdateEvent());
    }

    public static void preUpdate() {
        LiquidBounce.instance.eventManager.call(new MotionUpdateEvent(MotionUpdateEvent.State.Pre));
    }

    public static void postUpdate() {
        LiquidBounce.instance.eventManager.call(new MotionUpdateEvent(MotionUpdateEvent.State.Post));
    }
}
