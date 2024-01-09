package net.ccbluex.liquidbounce.features.command;

import net.ccbluex.liquidbounce.features.command.commands.BindCommand;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.ccbluex.liquidbounce.features.command.commands.ToggleCommand;
import net.ccbluex.liquidbounce.features.command.commands.ValueCommand;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.PacketEvent;

import java.util.ArrayList;
import java.util.List;


public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void registerCommands() {
        registerCommand(new BindCommand());
        registerCommand(new ValueCommand());
        registerCommand(new ToggleCommand());
    }

    public void registerCommand(final Command command) {
        commands.add(command);
    }

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<PacketEvent> onEvent = event -> {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            if (((C01PacketChatMessage) event.getPacket()).getMessage().startsWith(".")) {
                callCommand(((C01PacketChatMessage) event.getPacket()).getMessage());
                event.setCancelled(true);
            }
        }
    };

    public void callCommand(final String s) {
        final String[] strings = s.split(" ");
        commands.stream().filter(command -> strings[0].equalsIgnoreCase("." + command.getName())).forEach(command -> command.execute(strings));
    }
}