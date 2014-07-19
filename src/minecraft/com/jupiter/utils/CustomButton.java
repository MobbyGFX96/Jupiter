package com.jupiter.utils;

import com.jupiter.Jupiter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by corey on 15/07/14.
 */
public class CustomButton extends GuiButton {

    public CustomButton(String text, int id, int x, int y) {
        super(id, x, y, 200, 20, text);
    }

    public CustomButton(String text, int id, int x, int y, int width, int height) {
        super(id, x, y, width, height, text);
    }

    public void drawButton(Minecraft mc, int mx, int my) {
        FontRenderer fontrenderer = Jupiter.getInstance().getFontManager().getMenuButtonFont();
        boolean inBounds = mx >= x && my >= y && mx < x + width && my < y + height;
        if (inBounds) {
            GuiUtils.getInstance().drawGradientRect(x, y, x + width, y + height, 0xFFEF8927, 0xFFE4531C);
            drawCenteredString(fontrenderer, displayString, x + width / 2, y + (height - 8) / 2, 0xFFFFFFFF);
        } else {
            GuiUtils.getInstance().drawRectangle(x, y, x + width, y + height, 0x99000000);
            drawCenteredString(fontrenderer, displayString, x + width / 2, y + (height - 8) / 2, 0x80FFFFFF);
        }
    }
}
