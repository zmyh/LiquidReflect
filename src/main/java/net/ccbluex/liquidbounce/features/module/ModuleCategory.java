package net.ccbluex.liquidbounce.features.module;


public enum ModuleCategory {

    COMBAT("Combat"),
    PLAYER("Player"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    WORLD("World"),
    MISC("Misc"),
    EXPLOIT("Exploit"),
    FUN("Fun");

    private final String name;

    ModuleCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}