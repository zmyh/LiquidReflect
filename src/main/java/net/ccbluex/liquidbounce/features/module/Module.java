package net.ccbluex.liquidbounce.features.module;

import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.render.ChatColor;
import net.ccbluex.liquidbounce.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Module {

    private final String moduleName = getClass().getAnnotation(ModuleInfo.class).name();
    private final String moduleDescription = getClass().getAnnotation(ModuleInfo.class).description();
    private final ModuleCategory moduleCategory = getClass().getAnnotation(ModuleInfo.class).category();
    private final boolean canEnable = getClass().getAnnotation(ModuleInfo.class).canEnable();
    public float slide;
    private int keyBind = getClass().getAnnotation(ModuleInfo.class).keyBind();
    public final float hue = (float) Math.random();
    private boolean state;

    protected final Minecraft mc = Minecraft.getMinecraft();

    public String getName() {
        return moduleName;
    }

    public String getTagName() {
        return getName() + (getTag() == null ? "" : " §7" + getTag());
    }

    public String getColorlessTagName() {
        return getName() + (getTag() == null ? "" : " " + ChatColor.stripColor(getTag()));
    }

    public String getTag() {
        return null;
    }

    public String getDescription() {
        return moduleDescription;
    }

    public ModuleCategory getCategory() {
        return moduleCategory;
    }

    public boolean canEnable() {
        return canEnable;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setState(boolean state) {
        if (state && canEnable)
            this.state = state;
        else
            this.state = false;

        if (state) {
            LiquidBounce.instance.eventManager.register(this);
            onEnable();
        } else {
            LiquidBounce.instance.eventManager.unregister(this);
            onDisable();
        }
    }

    public boolean getState() {
        return state;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public Value getValue(final String valueName) {
        for (final Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(this);

                System.out.println(field.getName());

                if (o instanceof Value) {
                    final Value value = (Value) o;

                    if (value.getName().equalsIgnoreCase(valueName))
                        return value;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public List<Value> getValues() {
        final List<Value> values = new ArrayList<>();

        for (final Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(this);

                if (o instanceof Value)
                    values.add((Value) o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    public void toggle() {
        this.setState(!getState());
    }
}
