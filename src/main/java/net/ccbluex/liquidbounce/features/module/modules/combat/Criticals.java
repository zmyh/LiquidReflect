package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.ccbluex.liquidbounce.event.events.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.reflect.ReflectionUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name = "Criticals", description = "Automatically deals critical hits.", category = ModuleCategory.COMBAT)
public class Criticals extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Packet", "HypixelPacket", "NoGround", "Hop", "TPHop", "Jump", "LowJump"}, "packet");
    public final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 500);
    public final MSTimer msTimer = new MSTimer();

    public Criticals() {
    }

    @Override
    public void onEnable() {
        if (this.modeValue.get().equalsIgnoreCase("NoGround")) {
            mc.thePlayer.jump();
        }
    }

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<AttackEvent> onEvent = event -> {
        if (event.getEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            if (entity == null || !mc.thePlayer.onGround || mc.thePlayer.isOnLadder() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.ridingEntity != null || !this.msTimer.hasTimePassed(this.delayValue.get())) {
                return;
            }
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;
            final String lowerCase = this.modeValue.get().toLowerCase();
            switch (lowerCase) {
                case "packet": {
                    if (entity.hurtTime >= 7) {
                        break;
                    }
                    mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, true));
                    mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5, z, false));
                    mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    mc.thePlayer.onCriticalHit((Entity) entity);
                    break;
                }
                case "hypixelpacket": {
                    if (mc.thePlayer.ticksExisted % 9 == 0) {
                        mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y + RandomUtils.nextDouble(0.01, 0.06), z, false));
                        mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        mc.thePlayer.onCriticalHit((Entity) entity);
                        break;
                    }
                    break;
                }
                case "hop": {
                    mc.thePlayer.motionY = 0.10000000149011612;
                    mc.thePlayer.fallDistance = 0.1f;
                    mc.thePlayer.onGround = false;
                    break;
                }
                case "tphop": {
                    mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.02, z, false));
                    mc.getNetHandler().addToSendQueue((Packet) new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
                    mc.thePlayer.setPosition(x, y + 0.01, z);
                    break;
                }
                case "jump": {
                    mc.thePlayer.motionY = 0.41999998688697815;
                    break;
                }
                case "lowjump": {
                    mc.thePlayer.motionY = 0.3425000011920929;
                    break;
                }
            }
            this.msTimer.reset();
        }
    };

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<PacketEvent> onPacket = event -> {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && this.modeValue.get().equalsIgnoreCase("NoGround")) {
            ReflectionUtils.setFieldValue(((C03PacketPlayer) packet), false, "onGround");
        }
    };

    @Override
    public String getTag() {
        return this.modeValue.get();
    }
}
