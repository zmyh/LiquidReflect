package net.ccbluex.liquidbounce.ui.font;

import net.ccbluex.liquidbounce.LiquidBounce;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Fonts {
    public static CFontRenderer font35;
    public static CFontRenderer font40;
    public static CFontRenderer.MCFont mc;

    public static void init() {
        font35 = new CFontRenderer(getFont(LiquidBounce.class.getResourceAsStream("/assets/minecraft/liquidbounce/font/Roboto-Medium.ttf"), 35 / 2f));
        font40 = new CFontRenderer(getFont(LiquidBounce.class.getResourceAsStream("/assets/minecraft/liquidbounce/font/Roboto-Medium.ttf"), 40 / 2f));
        mc = new CFontRenderer.MCFont();
    }

    private static Font getFont(InputStream is, float fontSize) {
        try {
            Font output = Font.createFont(Font.PLAIN, is);
            output = output.deriveFont(fontSize);
            return output;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<CFontRenderer> getFonts() {
        return Arrays.asList(font35, font40, mc);
    }
}
