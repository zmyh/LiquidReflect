package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.utils.ChatUtil;
import net.ccbluex.liquidbounce.value.Value;

public class ValueCommand extends Command {

    public ValueCommand() {
        super("val");
    }

    @Override
    public void execute(String[] strings) {
        if (strings.length > 3) {
            final Module module = ModuleManager.getModule(strings[1]);

            if (module == null) {
                ChatUtil.displayChatMessage("§c§lError: §r§aThe entered module not exist.");
                return;
            }

            final Value value = module.getValue(strings[2]);

            if (value == null) {
                ChatUtil.displayChatMessage("§c§lError: §r§aThe entered value not exist.");
                return;
            }

            if (value.get() instanceof Float) {
                final float newValue = Float.parseFloat(strings[3]);
                value.set(newValue);
                ChatUtil.displayChatMessage("§cThe value of §a§l" + module.getName() + " §8(§a§l" + value.getName() + ") §c was set to §a§l" + newValue + "§c.");
            }
            return;
        }

        ChatUtil.displayChatMessage("§2Usage: §b.val <module> <valuename> <new_value>");
    }
}