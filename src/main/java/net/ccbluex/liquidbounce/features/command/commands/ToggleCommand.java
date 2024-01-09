package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.utils.ChatUtil;

/**
 * Copyright © 2015 - 2017 | CCBlueX | All rights reserved.
 * <p>
 * LiquidBase - By CCBlueX(Marco)
 */
public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("t");
    }

    @Override
    public void execute(String[] strings) {
        if (strings.length > 1) {
            final Module module = ModuleManager.getModule(strings[1]);

            if (module == null) {
                ChatUtil.displayChatMessage("§c§lError: §r§aThe entered module not exist.");
                return;
            }

            module.setState(!module.getState());
            ChatUtil.displayChatMessage("§cToggled module.");
            return;
        }

        ChatUtil.displayChatMessage("§2Usage: §b.t <module>");
    }
}
