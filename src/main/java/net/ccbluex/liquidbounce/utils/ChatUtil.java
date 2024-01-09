package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class ChatUtil {
    public static void displayChatMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            System.out.println(message);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);
        Minecraft.getMinecraft().thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
    }
}
