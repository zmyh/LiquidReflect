package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.minecraft.entity.EntityLivingBase;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @author CCBlueX
 * @game Minecraft
 */
@ModuleInfo(name = "Teams", description = "Prevents Killaura from attacking team mates.", category = ModuleCategory.MISC)
public class Teams extends Module {

    private final BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    private final BoolValue colorValue = new BoolValue("Color", true);

    /**
     * Check if [entity] is in your own team using scoreboard or name color
     */
    public boolean isInYourTeam(EntityLivingBase entity) {
        if (mc.thePlayer == null) return false;

        if (scoreboardValue.get() && mc.thePlayer.getTeam() != null && entity.getTeam() != null &&
                mc.thePlayer.getTeam().isSameTeam(entity.getTeam())) {
            return true;
        }

        if (colorValue.get() && mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            return targetName.startsWith("§" + clientName.charAt(1));
        }

        return false;
    }
}
