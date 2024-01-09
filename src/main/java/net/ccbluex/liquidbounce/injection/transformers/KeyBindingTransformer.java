package net.ccbluex.liquidbounce.injection.transformers;

import net.minecraft.client.settings.KeyBinding;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.KeyEvent;
import net.ccbluex.liquidbounce.injection.Transformer;

public class KeyBindingTransformer extends Transformer {
    @Override
    public Class<?> getTransformTarget() {
        return KeyBinding.class;
    }

    @Override
    public void transform(ClassNode classNode) {
        // onTick
        MethodNode onTick = method(classNode, "onTick", "(I)V");
        InsnList list = new InsnList();
        list.add(new VarInsnNode(ILOAD, 0));
        list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "onTick", "(I)V"));
        onTick.instructions.insertBefore(onTick.instructions.get(1), list);
        // end onTick
    }

    public static void onTick(int key) {
        LiquidBounce.instance.eventManager.call(new KeyEvent(key));
    }
}
