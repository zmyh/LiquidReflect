
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.CFontRenderer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.entity.player.EntityPlayer;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.Minecraft;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import java.awt.Color;

@ElementInfo(name = "Text")
public class Text extends Element
{
    private final TextValue displayString;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final BoolValue rainbow;
    private final BoolValue shadow;
    private CFontRenderer fontRenderer;
    private boolean editMode;
    private float editTicks;
    private long prevClick;

    public Text() {
        this.displayString = new TextValue("DisplayText", "");
        final Color c = Color.WHITE;
        this.redValue = new IntegerValue("Red", c.getRed(), 0, 255);
        this.greenValue = new IntegerValue("Green", c.getGreen(), 0, 255);
        this.blueValue = new IntegerValue("Blue", c.getBlue(), 0, 255);
        this.rainbow = new BoolValue("Rainbow", false);
        this.shadow = new BoolValue("Shadow", true);
        this.fontRenderer = Fonts.font40;
    }

    @Override
    public void drawElement() {
        this.editTicks += 0.1f * RenderUtils.deltaTime;
        if (this.editTicks > 80.0f) {
            this.editTicks = 0.0f;
        }
        final int color = new Color(this.redValue.get(), this.greenValue.get(), this.blueValue.get()).getRGB();
        final int[] location = this.getLocationFromFacing();
        this.fontRenderer.drawString(this.editMode ? this.displayString.get() : this.getDisplay(), (float)location[0], (float)location[1], this.rainbow.get() ? ColorUtils.rainbow(400000000L).getRGB() : color, this.shadow.get());
        if (this.editMode && Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner && this.editTicks <= 40.0f) {
            this.fontRenderer.drawString("_", (float)(location[0] + this.fontRenderer.getWidth(this.displayString.get()) + 2), (float)location[1], this.rainbow.get() ? ColorUtils.rainbow(400000000L).getRGB() : color, this.shadow.get());
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            RenderUtils.drawBorderedRect((float)(location[0] - 2), (float)(location[1] - 2), (float)(location[0] + this.fontRenderer.getWidth(this.editMode ? this.displayString.get() : this.getDisplay()) + 2), (float)(location[1] + this.fontRenderer.getHeight()), 3.0f, Integer.MIN_VALUE, 0);
        }
    }

    @Override
    public void destroyElement() {
    }

    @Override
    public void updateElement() {

    }

    @Override
    public void handleMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isMouseOverElement(mouseX, mouseY) && mouseButton == 0) {
            if (System.currentTimeMillis() - this.prevClick <= 250L) {
                this.editMode = true;
            }
            this.prevClick = System.currentTimeMillis();
        }
        else {
            this.editMode = false;
        }
    }

    @Override
    public void handleKey(final char c, final int keyCode) {
        if (this.editMode && Minecraft.getMinecraft().currentScreen instanceof GuiHudDesigner) {
            if (keyCode == 14) {
                if (!this.displayString.get().isEmpty()) {
                    this.displayString.set(this.displayString.get().substring(0, this.displayString.get().length() - 1));
                }
                return;
            }
            if (ChatAllowedCharacters.isAllowedCharacter(c) || c == '§') {
                this.displayString.set(this.displayString.get() + c);
            }
        }
    }

    @Override
    public boolean isMouseOverElement(final int mouseX, final int mouseY) {
        final int[] location = this.getLocationFromFacing();
        return mouseX >= location[0] && mouseY >= location[1] && mouseX <= location[0] + this.fontRenderer.getWidth(this.displayString.get().isEmpty() ? "Text Element" : this.getDisplay()) && mouseY <= location[1] + this.fontRenderer.getHeight();
    }

    private String getDisplay() {
        String s = (this.displayString.get().isEmpty() && !this.editMode) ? "Text Element" : this.displayString.get();
        if (s.contains("%")) {
            s = StringUtils.replace(s, "%username%", Minecraft.getMinecraft().getSession().getUsername());
            s = StringUtils.replace(s, "%clientName%", LiquidBounce.CLIENT_NAME);
            s = StringUtils.replace(s, "%clientVersion%", "b" + LiquidBounce.CLIENT_VERSION);
            s = StringUtils.replace(s, "%clientCreator%", LiquidBounce.CLIENT_AUTHOR);
            s = StringUtils.replace(s, "%fps%", String.valueOf(Minecraft.getDebugFPS()));
            if (Minecraft.getMinecraft().thePlayer != null) {
                s = StringUtils.replace(s, "%x%", String.valueOf((int)Minecraft.getMinecraft().thePlayer.posX));
                s = StringUtils.replace(s, "%y%", String.valueOf((int)Minecraft.getMinecraft().thePlayer.posY));
                s = StringUtils.replace(s, "%z%", String.valueOf((int)Minecraft.getMinecraft().thePlayer.posZ));
                s = StringUtils.replace(s, "%ping%", String.valueOf(EntityUtils.getPing((EntityPlayer)Minecraft.getMinecraft().thePlayer)));
            }
        }
        return s;
    }

    public Text setText(final String s) {
        this.displayString.set(s);
        return this;
    }

    public Text setColor(final Color c) {
        this.redValue.set(c.getRed());
        this.greenValue.set(c.getGreen());
        this.blueValue.set(c.getBlue());
        return this;
    }

    public Text setRainbow(final boolean b) {
        this.rainbow.set(b);
        return this;
    }

    public Text setShadow(final boolean b) {
        this.shadow.set(b);
        return this;
    }

    public Text setFontRenderer(final CFontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        return this;
    }
}
