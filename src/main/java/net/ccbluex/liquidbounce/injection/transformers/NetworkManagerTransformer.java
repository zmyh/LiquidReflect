package net.ccbluex.liquidbounce.injection.transformers;


import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.injection.Transformer;

import java.util.ArrayList;
import java.util.List;


public class NetworkManagerTransformer extends Transformer {
    private static final List<Packet<?>> packets = new ArrayList<>();

    public NetworkManagerTransformer() {
        this.computeFrame = true;
    }

    @Override
    public Class<?> getTransformTarget() {
        return NetworkManager.class;
    }

    @Override
    public void transform(ClassNode classNode) {
        // channelRead0
        MethodNode channelRead0 = method(classNode, "channelRead0", "(Lio/netty/channel/ChannelHandlerContext;L" + Type.getInternalName(Packet.class) + ";)V");
        InsnList recievePacket = new InsnList();
        LabelNode L0 = new LabelNode();
        recievePacket.add(new VarInsnNode(ALOAD, 2));
        recievePacket.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "channelRead0", "(L" + Type.getInternalName(Packet.class) + ";)Z"));
        recievePacket.add(new JumpInsnNode(IFEQ, L0));
        recievePacket.add(new InsnNode(RETURN));
        recievePacket.add(L0);
        channelRead0.instructions.insert(recievePacket);
        channelRead0.maxLocals++;
        //end channelRead0

        // sendPacket
        MethodNode sendPacket = method(classNode, "sendPacket", "(L" + Type.getInternalName(Packet.class) + ";)V");
        InsnList send = new InsnList();
        LabelNode L1 = new LabelNode();
        send.add(new VarInsnNode(ALOAD, 1));
        send.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "sendPacket", "(L" + Type.getInternalName(Packet.class) + ";)Z"));
        send.add(new JumpInsnNode(IFEQ, L1));
        send.add(new InsnNode(RETURN));
        send.add(L1);
        sendPacket.instructions.insert(send);
        sendPacket.maxLocals++;
        //end sendPacket
    }

    public static boolean channelRead0(Packet<?> packet) {
        PacketEvent event = new PacketEvent(packet, false);
        LiquidBounce.instance.eventManager.call(event);

        return event.isCancelled();
    }


    public static boolean sendPacket(Packet<?> packet) {
        if (!packets.contains(packet)) {
            PacketEvent event = new PacketEvent(packet, true);
            LiquidBounce.instance.eventManager.call(event);
            return event.isCancelled();
        }
        return false;
    }
}
