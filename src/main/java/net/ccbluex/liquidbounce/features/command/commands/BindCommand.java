package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.utils.ChatUtil;


public class BindCommand extends Command {

    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(String[] strings) {
        if (strings.length > 2) {
            final Module module = ModuleManager.getModule(strings[1]);

            if (module == null) {
                ChatUtil.displayChatMessage("§c§lError: §r§aThe entered module not exist.");
                return;
            }

            final int key = Keyboard.getKeyIndex(strings[2].toUpperCase());

            module.setKeyBind(key);
            ChatUtil.displayChatMessage("§cThe keybind of §a§l" + module.getName() + " §r§cwas set to §a§l" + Keyboard.getKeyName(key) + "§c.");
            return;
        }

        ChatUtil.displayChatMessage("§2Usage: §b.bind <module> <key>");
    }
}