package com.jupiter.managers.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TTFRenderer extends FontRenderer {

    public StringCache stringCache;
    int i11;

    public TTFRenderer(String fontName, int size) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        stringCache = new StringCache(getColorCode());
        stringCache.setDefaultFont(fontName,
                size, true);
    }

    private int renderString(String par1Str, int par2, int par3, int par4, boolean par5) {
        if (par1Str == null) {
            return 0;
        } else {
            if ((par4 & -67108864) == 0) {
                par4 |= -16777216;
            }
            if (par5) {
                par4 = ((par4 & 16579836) >> 2) | (par4 & -16777216);
            }
            setRed(((par4 >> 16) & 255) / 255.0F);
            setBlue(((par4 >> 8) & 255) / 255.0F);
            setGreen((par4 & 255) / 255.0F);
            setAlpha(((par4 >> 24) & 255) / 255.0F);
            GL11.glColor4f(getRed(), getBlue(), getGreen(), getAlpha());
            setPosX(par2);
            setPosY(par3);
            if (stringCache != null) {
                setPosX(getPosX() + stringCache.renderString(par1Str, par2, par3, par4, par5));
            }
            return (int) getPosX();
        }
    }

    private void resetStyles() {
        this.setRandomStyle(false);
        this.setBoldStyle(false);
        this.setItalicStyle(false);
        this.setUnderlineStyle(false);
        this.setStrikethroughStyle(false);
    }

    @Override
    public int drawString(String par1Str, int par2, int par3, int par4, boolean par5) {
        resetStyles();

        int var6;

        if (par5) {
            final int i;
            var6 = this.renderString(par1Str, par2 + 1, par3 + 1, par4, true);
            var6 = Math.max(var6, renderString(par1Str, par2, par3, par4, false));
        } else {
            var6 = renderString(par1Str, par2, par3, par4, false);
        }
        return var6;
    }

    public int drawString(String par1Str, int par2, int par3, int par4) {
        return this.drawString(par1Str, par2, par3, par4, false);
    }

    public void drawCenteredString(String par2Str, int par3, int par4, int par5) {
        this.drawStringWithShadow(par2Str, par3 - this.getStringWidth(par2Str) / 2, par4, par5);
    }

    @Override
    public int drawStringWithShadow(String string, int x, int y, int color) {
        return drawString(string, x, y, color);
    }

    public int getStringWidth(String string) {
        return stringCache.getStringWidth(string);
    }
}