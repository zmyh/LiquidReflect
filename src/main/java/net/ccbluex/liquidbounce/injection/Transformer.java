package net.ccbluex.liquidbounce.injection;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Transformer implements Opcodes {

    public boolean computeFrame = false;

    public abstract Class<?> getTransformTarget();

    public abstract void transform(ClassNode classNode);

    protected MethodNode method(ClassNode classNode, String name, String desc) {
        return classNode.methods.stream().filter(mn -> mn.name.equals(name) && mn.desc.equals(desc)).findFirst().orElse(null);
    }

    protected String getClassSignature(Class<?> clazz) {
        return "L" + Type.getInternalName(clazz) + ";";
    }
}
