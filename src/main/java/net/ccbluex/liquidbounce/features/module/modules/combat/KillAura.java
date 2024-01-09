package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.utils.*;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldSettings;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.AttackEvent;
import net.ccbluex.liquidbounce.event.events.MotionUpdateEvent;
import net.ccbluex.liquidbounce.event.events.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.ToDoubleFunction;

@ModuleInfo(name = "KillAura", description = "Automatically attacks targets around you.", category = ModuleCategory.COMBAT, keyBind = 19)
public class KillAura extends Module {
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final FloatValue rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
    private final IntegerValue maxCPS = new IntegerValue("MaxCPS", 8, 1, 20) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = KillAura.this.minCPS.get();
            if (i > Integer.parseInt(String.valueOf(newValue))) {
                this.changeValue(i);
            }
            KillAura.this.attackDelay = TimeUtils.randomClickDelay(KillAura.this.minCPS.get(), KillAura.this.maxCPS.get());
        }
    };
    private final FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
    private final IntegerValue minCPS = new IntegerValue("MinCPS", 5, 1, 20) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = KillAura.this.maxCPS.get();
            if (i < Integer.parseInt(String.valueOf(newValue))) {
                this.changeValue(i);
            }
            KillAura.this.attackDelay = TimeUtils.randomClickDelay(KillAura.this.minCPS.get(), KillAura.this.maxCPS.get());
        }
    };
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime"}, "Distance");
    private final ListValue targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
    private final ListValue multiModeValue = new ListValue("MultiMode", new String[]{"Vanilla", "NCP"}, "NCP");
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue keepSprintValue = new BoolValue("KeepSprint", true);
    private final BoolValue autoBlockValue = new BoolValue("AutoBlock", false);
    private final BoolValue interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
    private final IntegerValue blockRate = new IntegerValue("BlockRate", 100, 1, 100);
    private final BoolValue aacValue = new BoolValue("AAC", false);
    private final BoolValue raycastValue = new BoolValue("RayCast", true);
    private final BoolValue raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
    private final BoolValue livingRaycastValue = new BoolValue("LivingRayCast", true);
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final BoolValue noInventoryAttackValue = new BoolValue("NoInvAttack", false);
    private final IntegerValue noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
    private final FloatValue failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
    private final IntegerValue limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 100);
    private final BoolValue fakeSwingValue = new BoolValue("FakeSwing", true);
    private final BoolValue silentRotationValue = new BoolValue("SilentRotation", true);
    private final FloatValue maxPredictSize = new FloatValue("MaxPredictSize", 1.0f, 0.1f, 5.0f) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            final float v = KillAura.this.minPredictSize.get();
            if (v > Float.parseFloat(String.valueOf(newValue))) {
                this.changeValue(v);
            }
        }
    };
    private final BoolValue randomCenterValue = new BoolValue("RandomCenter", true);
    private final BoolValue outborderValue = new BoolValue("Outborder", false);
    private final FloatValue minPredictSize = new FloatValue("MinPredictSize", 1.0f, 0.1f, 5.0f) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            final float v = KillAura.this.maxPredictSize.get();
            if (v < Float.parseFloat(String.valueOf(newValue))) {
                this.changeValue(v);
            }
        }
    };
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
    private final BoolValue markValue = new BoolValue("Mark", true);
    private final BoolValue fakeSharpValue = new BoolValue("FakeSharp", true);
    private final List<Integer> prevTargetEntities = new ArrayList<Integer>();
    private final MSTimer attackTimer = new MSTimer();
    public EntityLivingBase target;
    private final FloatValue maxTurnSpeed = new FloatValue("MaxTurnSpeed", 180.0f, 1.0f, 180.0f) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            final float turn = KillAura.this.minTurnSpeed.get();
            if (turn > Float.parseFloat(String.valueOf(newValue))) {
                this.changeValue(turn);
            }
        }
    };
    public boolean blocking;
    private EntityLivingBase currentTarget;
    private final FloatValue minTurnSpeed = new FloatValue("MinTurnSpeed", 180.0f, 1.0f, 180.0f) {
        @Override
        protected void onChanged(final Float oldValue, final Float newValue) {
            final float turn = KillAura.this.maxTurnSpeed.get();
            if (turn < Float.parseFloat(String.valueOf(newValue))) {
                this.changeValue(turn);
            }
        }
    };
    private boolean hitable;
    private long containerOpen = -1L;
    @EventTarget
    private final CallableEvent<Render3DEvent> onRender = event -> {
        if (mc.thePlayer == null || mc.thePlayer.isSpectator() || !this.isLiving(mc.thePlayer) || ModuleManager.getModule(Blink.class).getState()) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (this.noInventoryAttackValue.get() && (mc.currentScreen instanceof GuiContainer || System.currentTimeMillis() - this.containerOpen < this.noInventoryDelayValue.get())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (mc.currentScreen instanceof GuiContainer) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        this.updateTarget();
        if (this.target == null) {
            this.stopBlocking();
            return;
        }
        this.currentTarget = this.target;
        if (this.raycastValue.get()) {
            final Entity raycastedEntity = RaycastUtils.raycastEntity(this.rangeValue.get() - (mc.thePlayer.isSprinting() ? this.rangeSprintReducementValue.get() : 0.0f), entity -> (!this.livingRaycastValue.get() || (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))) && (this.isSelected(entity) || this.raycastIgnoredValue.get() || (this.aacValue.get() && !mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox()).isEmpty())));
            if (this.raycastValue.get() && raycastedEntity instanceof EntityLivingBase && (Objects.requireNonNull(ModuleManager.getModule(NoFriends.class)).getState() || !EntityUtils.isFriend(raycastedEntity))) {
                this.currentTarget = (EntityLivingBase) raycastedEntity;
            }
            this.hitable = this.currentTarget.equals(raycastedEntity);
        } else {
            this.hitable = RotationUtils.isFaced(this.currentTarget, this.getRange(this.currentTarget));
        }
        if (!this.targetModeValue.get().equalsIgnoreCase("Switch") && this.isSelected(this.currentTarget)) {
            this.target = this.currentTarget;
        }
        if (!this.targetModeValue.get().equalsIgnoreCase("Multi") && this.markValue.get()) {
            RenderUtils.drawPlatform(this.target, this.hitable ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
        }
    };

    public KillAura() {
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        this.updateTarget();
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.stopBlocking();
    }

    private void updateTarget() {
        this.target = null;
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Entity entity2 : mc.theWorld.loadedEntityList) {
            if (this.isSelected(entity2) && mc.thePlayer.getDistanceToEntity(entity2) <= this.getRange(entity2) && RotationUtils.getRotationDifference(entity2) <= this.fovValue.get() && ((EntityLivingBase) entity2).hurtTime <= this.hurtTimeValue.get()) {
                targets.add((EntityLivingBase) entity2);
            }
        }
        if (targets.isEmpty()) {
            return;
        }
        if (this.targetModeValue.get().equalsIgnoreCase("Switch")) {
            targets.removeIf(entityLivingBase -> this.prevTargetEntities.contains(entityLivingBase.getEntityId()));
            if (targets.isEmpty()) {
                this.prevTargetEntities.clear();
                this.updateTarget();
                return;
            }
        }
        final String lowerCase = this.priorityValue.get().toLowerCase();
        switch (lowerCase) {
            case "distance": {
                targets.sort(Comparator.comparingDouble(value -> mc.thePlayer.getDistanceToEntity(value)));
                break;
            }
            case "health": {
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case "direction": {
                targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>) RotationUtils::getRotationDifference));
                break;
            }
            case "livingtime": {
                targets.sort(Comparator.comparingInt(entity -> -entity.ticksExisted));
                break;
            }
        }
        if (!targets.isEmpty()) {
            this.target = targets.get(0);
        }
    }

    private void showCriticals(final EntityLivingBase target) {
        final Criticals criticals = (Criticals) ModuleManager.getModule(Criticals.class);
        for (int i = 0; i < 3; ++i) {
            if ((mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null) || (criticals.getState() && criticals.msTimer.hasTimePassed(criticals.delayValue.get()) && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava())) {
                mc.thePlayer.onCriticalHit(target);
            }
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), target.getCreatureAttribute()) > 0.0f || this.fakeSharpValue.get()) {
                mc.thePlayer.onEnchantmentCritical(target);
            }
        }
    }

    private boolean isSelected(final Entity entity) {
        if (!(entity instanceof EntityLivingBase) || (!EntityUtils.targetDead && !this.isLiving((EntityLivingBase) entity)) || entity.equals(mc.thePlayer) || (!EntityUtils.targetInvisible && entity.isInvisible())) {
            return false;
        }
        if (!EntityUtils.targetPlayer || !(entity instanceof EntityPlayer)) {
            return (EntityUtils.targetMobs && EntityUtils.isMob(entity)) || (EntityUtils.targetAnimals && EntityUtils.isAnimal(entity));
        }
        final EntityPlayer entityPlayer = (EntityPlayer) entity;
        if (AntiBot.isBot(entityPlayer)) {
            return false;
        }
        if (EntityUtils.isFriend(entityPlayer) && !ModuleManager.getModule(NoFriends.class).getState()) {
            return false;
        }
        if (entityPlayer.isSpectator()) {
            return false;
        }
        final Teams teams = (Teams) ModuleManager.getModule(Teams.class);
        return !teams.getState() || !teams.isInYourTeam(entityPlayer);
    }

    public void attackEntity(final EntityLivingBase targetEntity) {
        if (mc.thePlayer.isBlocking() || this.blocking) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.blocking = false;
        }
        LiquidBounce.instance.eventManager.call(new AttackEvent(targetEntity));
        if (this.swingValue.get()) {
            mc.thePlayer.swingItem();
        }
        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
        if (this.keepSprintValue.get()) {
            if (mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null) {
                mc.thePlayer.onCriticalHit(targetEntity);
            }
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), targetEntity.getCreatureAttribute()) > 0.0f) {
                mc.thePlayer.onEnchantmentCritical(targetEntity);
            }
        } else if (mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
            mc.thePlayer.attackTargetEntityWithCurrentItem(targetEntity);
        }
        if (mc.thePlayer.isBlocking() || (this.autoBlockValue.get() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            if (this.blockRate.get() <= 0 || RandomUtils.getRandom().nextInt(100) > this.blockRate.get()) {
                return;
            }
            if (this.interactAutoBlockValue.get()) {
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(targetEntity, targetEntity.getPositionVector()));
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
            }
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            this.blocking = true;
        }
    }

    private void stopBlocking() {
        if (this.blocking) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.blocking = false;
        }
    }

    private boolean isLiving(final EntityLivingBase entity) {
        return (entity.isEntityAlive() && entity.getHealth() > 0.0f) || (this.aacValue.get() && entity.hurtTime > 5);
    }

    private float getRange(final Entity entity) {
        return (mc.thePlayer.canEntityBeSeen(entity) ? this.rangeValue.get() : this.throughWallsRangeValue.get()) - (mc.thePlayer.isSprinting() ? this.rangeSprintReducementValue.get() : 0.0f);
    }

    @Override
    public String getTag() {
        return this.targetModeValue.get();
    }

    private long attackDelay = TimeUtils.randomClickDelay(this.minCPS.get(), this.maxCPS.get());


    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<MotionUpdateEvent> onEvent = event -> {
        if (mc.thePlayer == null || this.target == null || this.currentTarget == null || event.getState() == MotionUpdateEvent.State.Post || mc.thePlayer.isSpectator() || !this.isLiving(mc.thePlayer) || ModuleManager.getModule(Blink.class).getState()) {
            return;
        }
        AxisAlignedBB axisAlignedBB = this.target.getEntityBoundingBox();
        if (this.predictValue.get()) {
            axisAlignedBB = axisAlignedBB.offset((this.target.posX - this.target.prevPosX) * RandomUtils.nextFloat(this.minPredictSize.get(), this.maxPredictSize.get()), (this.target.posY - this.target.prevPosY) * RandomUtils.nextFloat(this.minPredictSize.get(), this.maxPredictSize.get()), (this.target.posZ - this.target.prevPosZ) * RandomUtils.nextFloat(this.minPredictSize.get(), this.maxPredictSize.get()));
        }
        final float[] rotations = RotationUtils.getNeededRotations(this.randomCenterValue.get() ? RotationUtils.getRandomCenter(axisAlignedBB, this.outborderValue.get() && !this.attackTimer.hasTimePassed(this.attackDelay / 2L)) : RotationUtils.getCenter(axisAlignedBB), this.predictValue.get());
        Rotation rotation = new Rotation(rotations[0], rotations[1]);
        rotation.fixGcd();
        if (this.silentRotationValue.get()) {
            RotationUtils.setTargetRotation(rotation);
        } else {
            mc.thePlayer.rotationYaw = rotations[0];
            mc.thePlayer.rotationPitch = rotations[1];
        }
        if (this.attackTimer.hasTimePassed(this.attackDelay) && this.currentTarget.hurtTime <= this.hurtTimeValue.get()) {
            final float failRate = this.failRateValue.get();
            final boolean swing = this.swingValue.get();
            final boolean multi = this.targetModeValue.get().equalsIgnoreCase("Multi");
            final boolean openInventory = this.aacValue.get() && mc.currentScreen instanceof GuiInventory;
            final boolean failHit = failRate > 0.0f && RandomUtils.getRandom().nextInt(100) <= failRate;
            if (openInventory) {
                mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
            }
            if (!this.hitable || failHit) {
                if (swing && (this.fakeSwingValue.get() || failHit)) {
                    mc.thePlayer.swingItem();
                }
            } else {
                if (!multi) {
                    this.attackEntity(this.currentTarget);
                    if (openInventory) {
                        mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    }
                    this.showCriticals(this.currentTarget);
                } else {
                    int targets = 0;
                    for (final Entity entity : mc.theWorld.loadedEntityList) {
                        if (this.isSelected(entity) && (!this.multiModeValue.get().equalsIgnoreCase("NCP") || RotationUtils.getRotationDifference(entity) <= 40.0) && mc.thePlayer.getDistanceToEntity(entity) <= this.getRange(entity)) {
                            final EntityLivingBase multiTarget = (EntityLivingBase) entity;
                            this.attackEntity(multiTarget);
                            this.showCriticals(multiTarget);
                            ++targets;
                            if (this.limitedMultiTargetsValue.get() > 0 && this.limitedMultiTargetsValue.get() <= targets) {
                                break;
                            }
                            continue;
                        }
                    }
                }
                this.prevTargetEntities.add(this.aacValue.get() ? this.target.getEntityId() : this.currentTarget.getEntityId());
                if (this.target.equals(this.currentTarget)) {
                    this.target = null;
                }
            }
            if (openInventory) {
                mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            this.attackTimer.reset();
            this.attackDelay = TimeUtils.randomClickDelay(this.minCPS.get(), this.maxCPS.get());
        }
    };


}
