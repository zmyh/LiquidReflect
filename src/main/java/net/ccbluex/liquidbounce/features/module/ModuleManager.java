package net.ccbluex.liquidbounce.features.module;

import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.*;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.event.CallableEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.events.KeyEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final List<Module> modules = new ArrayList<>();

    public void registerModules() {
        registerModule(new NoFriends());
        registerModule(new KillAura());
        registerModule(new FastBow());
        registerModule(new Criticals());

        registerModule(new Sprint());

        registerModule(new Blink());

        registerModule(new Interface());
        registerModule(new ClickGUI());
        registerModule(new Fullbright());
        registerModule(new BlockOverlay());
        registerModule(new HeadRotations());
        registerModule(new FreeCam());

        registerModule(new Teams());
        registerModule(new AntiBot());

        registerModule(new Scaffold());
    }

    public void registerModule(final Module module) {
        modules.add(module);
    }

    public static Module getModule(final Class<? extends Module> targetModule) {
        synchronized (modules) {
            for (final Module currentModule : modules)
                if (currentModule.getClass() == targetModule)
                    return currentModule;
        }

        return null;
    }

    public static Module getModule(final String targetModule) {
        synchronized (modules) {
            for (final Module currentModule : modules)
                if (currentModule.getName().equalsIgnoreCase(targetModule))
                    return currentModule;
        }

        return null;
    }

    public static List<Module> getModules() {
        return modules;
    }

    @EventTarget
    @SuppressWarnings("unused")
    private final CallableEvent<KeyEvent> onEvent = event -> {
        synchronized (modules) {
            modules.stream().filter(module -> module.getKeyBind() == event.getKey()).forEach(module -> module.setState(!module.getState()));
        }
    };
}
