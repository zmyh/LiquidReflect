package net.ccbluex.liquidbounce.injection.transformers;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.MoveEvent;
import net.ccbluex.liquidbounce.injection.Transformer;

public class EntityTransformer extends Transformer {

    public EntityTransformer() {
        this.computeFrame = true;
    }

    @Override
    public Class<?> getTransformTarget() {
        return Entity.class;
    }

    @Override
    public void transform(ClassNode classNode) {

        // moveEntity
        MethodNode moveEntity = method(classNode, "moveEntity", "(DDD)V");
        LocalVariableNode moveVar = new LocalVariableNode("moveEntity", "L" + Type.getInternalName(MoveEvent.class) + ";", null, new LabelNode(), new LabelNode(), moveEntity.maxLocals);
        moveEntity.localVariables.add(moveVar);
        InsnList insnList = moveEntity.instructions;
        InsnList move = new InsnList();
        LabelNode L0 = new LabelNode();
        move.add(new VarInsnNode(ALOAD, 0));
        move.add(new VarInsnNode(DLOAD, 1));
        move.add(new VarInsnNode(DLOAD, 3));
        move.add(new VarInsnNode(DLOAD, 5));
        move.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "moveEntity", "(Ljava/lang/Object;DDD)L" + Type.getInternalName(MoveEvent.class) + ";"));
        move.add(new VarInsnNode(ASTORE, moveVar.index));
        move.add(new VarInsnNode(ALOAD, moveVar.index));
        move.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(MoveEvent.class), "isCancelled", "()Z"));
        move.add(new JumpInsnNode(IFEQ, L0));
        move.add(new InsnNode(RETURN));
        move.add(L0);
        move.add(new VarInsnNode(ALOAD, moveVar.index));
        move.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(MoveEvent.class), "getX", "()D"));
        move.add(new VarInsnNode(DSTORE, 1));
        move.add(new VarInsnNode(ALOAD, moveVar.index));
        move.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(MoveEvent.class), "getY", "()D"));
        move.add(new VarInsnNode(DSTORE, 3));
        move.add(new VarInsnNode(ALOAD, moveVar.index));
        move.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(MoveEvent.class), "getZ", "()D"));
        move.add(new VarInsnNode(DSTORE, 5));
        insnList.insert(move);
        moveEntity.instructions = insnList;
        // end moveEntity

    }

    public static MoveEvent moveEntity(Object ent, double x, double y, double z) {
        Entity entity = (Entity) ent;
        MoveEvent move = new MoveEvent(x, y, z);
        if (entity instanceof EntityPlayerSP) {
            LiquidBounce.instance.eventManager.call(move);
            return move;
        }
        return move;
    }
}
