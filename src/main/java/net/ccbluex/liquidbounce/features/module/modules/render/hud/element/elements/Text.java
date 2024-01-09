package net.ccbluex.liquidbounce.features.module.modules.render.hud.element.elements;

import net.ccbluex.liquidbounce.features.module.modules.render.hud.GuiHudDesigner;
import net.ccbluex.liquidbounce.features.module.modules.render.hud.element.Element;
import net.ccbluex.liquidbounce.features.module.modules.render.hud.element.ElementInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.CFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.TextValue;

import java.awt.*;
import java.text.SimpleDateFormat;

@ElementInfo(name = "Text")
public class Text extends Element {

    private final TextValue displayString = new TextValue("DisplayText", "LiquidBounce");
    private final IntegerValue redValue = new IntegerValue("Red", 255, 0, 255);
    private final IntegerValue greenValue = new IntegerValue("Green", 255, 0, 255);
    private final IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255);
    private final BoolValue rainbow = new BoolValue("Rainbow", false);
    private final BoolValue shadow = new BoolValue("Shadow", true);
    private CFontRenderer fontRenderer = Fonts.font40;

    private boolean editMode = false;
    private int editTicks = 0;
    private long prevClick = 0L;

    private String displayText = getDisplay();

    private String getDisplay() {
        String textContent;
        if (displayString.get().isEmpty()) {
            textContent = "Text Element";
        } else {
            textContent = displayString.get();
        }

        if (textContent.contains("%")) {
            textContent = StringUtils.replace(textContent, "%username%", Minecraft.getMinecraft().getSession().getUsername());
            textContent = StringUtils.replace(textContent, "%clientName%", LiquidBounce.CLIENT_NAME);
            textContent = StringUtils.replace(textContent, "%clientVersion%", "b" + LiquidBounce.CLIENT_VERSION);
            textContent = StringUtils.replace(textContent, "%clientCreator%", LiquidBounce.CLIENT_AUTHOR);
            textContent = StringUtils.replace(textContent, "%fps%", Integer.toString(Minecraft.getDebugFPS()));
            textContent = StringUtils.replace(textContent, "%date%", new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
            textContent = StringUtils.replace(textContent, "%time%", new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));

            if (Minecraft.getMinecraft().thePlayer != null) {
                textContent = StringUtils.replace(textContent, "%x%", Double.toString(Minecraft.getMinecraft().thePlayer.posX));
                textContent = StringUtils.replace(textContent, "%y%", Double.toString(Minecraft.getMinecraft().thePlayer.posY));
                textContent = StringUtils.replace(textContent, "%z%", Double.toString(Minecraft.getMinecraft().thePlayer.posZ));
                //        textContent = StringUtil.replace(textContent, "%ping%", Integer.toString(EntityUtils.getPing(Minecraft.getMinecraft().thePlayer)));
            }
        }

        return textContent;
    }

    @Override
    public void drawElement() {
        int color = new Color(redValue.get(), greenValue.get(), blueValue.get()).getRGB();
        int[] location = getLocationFromFacing();

        fontRenderer.drawString(displayText, location[0], location[1], rainbow.get() ? ColorUtils.rainbow(400000000L).getRGB() : color, shadow.get());

        if (editMode && Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner && editTicks <= 40) {
            fontRenderer.drawString("_", location[0] + fontRenderer.getWidth(displayText) + 2F, location[1], rainbow.get() ? ColorUtils.rainbow(400000000L).getRGB() : color, shadow.get());
        }

        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect(location[0] - 2, location[1] - 2, location[0] + fontRenderer.getWidth(displayText) + 2, location[1] + fontRenderer.getHeight(), 3f, Integer.MIN_VALUE, 0);
        } else if (editMode) {
            editMode = false;
            updateElement();
        }
    }

    @Override
    public void destroyElement() {
    }

    @Override
    public void updateElement() {
        editTicks += 5;
        if (editTicks > 80) editTicks = 0;

        displayText = editMode ? displayString.get() : getDisplay();
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (isMouseOverElement(mouseX, mouseY) && mouseButton == 0) {
            if (System.currentTimeMillis() - prevClick <= 250L) {
                editMode = true;
            }

            prevClick = System.currentTimeMillis();
        } else {
            editMode = false;
        }
    }

    @Override
    public void handleKey(char c, int keyCode) {
        if (editMode && Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (!displayString.get().isEmpty()) {
                    displayString.set(displayString.get().substring(0, displayString.get().length() - 1));
                }

                updateElement();
                return;
            }

            if (ChatAllowedCharacters.isAllowedCharacter(c) || c == '§') {
                displayString.set(displayString.get() + c);
            }

            updateElement();
        }
    }

    @Override
    public boolean isMouseOverElement(int mouseX, int mouseY) {
        int[] location = getLocationFromFacing();
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + fontRenderer.getWidth(displayString.get().isEmpty() ? "Text Element" : displayText) && mouseY <= location[1] + fontRenderer.getHeight();
    }

    public Text setText(String s) {
        displayString.changeValue(s);
        return this;
    }

    public Text setColor(Color c) {
        redValue.set(c.getRed());
        greenValue.set(c.getGreen());
        blueValue.set(c.getBlue());
        return this;
    }

    public Text setRainbow(boolean b) {
        rainbow.set(b);
        return this;
    }

    public Text setShadow(boolean b) {
        shadow.set(b);
        return this;
    }

    public Text setFontRenderer(CFontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
}
